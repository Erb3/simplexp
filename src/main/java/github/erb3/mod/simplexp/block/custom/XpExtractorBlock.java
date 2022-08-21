package github.erb3.mod.simplexp.block.custom;

import github.erb3.mod.simplexp.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class XpExtractorBlock extends Block implements InventoryProvider {

    public static final EnumProperty<XpExtractorBottleType> BOTTLE = EnumProperty.of("bottle", XpExtractorBottleType.class);
    public static final BooleanProperty FULL = BooleanProperty.of("full");

    public XpExtractorBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(BOTTLE, XpExtractorBottleType.NONE).with(FULL, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var bottleType = state.get(BOTTLE);
        var full = state.get(FULL);

        if (bottleType != XpExtractorBottleType.NONE) {
            if (full) {
                player.getInventory().offerOrDrop(new ItemStack(bottleType.getFullItem()));
                if (!world.isClient) {
                    removeBottle(state, world, pos);
                }
                return ActionResult.success(world.isClient);
            }
            return ActionResult.PASS;
        }

        var stackInHand = player.getStackInHand(hand);
        Item itemInHand = stackInHand.getItem().asItem();

        if (XpExtractorBottleType.EMPTY_BOTTLES.contains(itemInHand)) {
            ItemStack toPut = stackInHand.copy();
            toPut.setCount(1);
            addBottle(state, world, pos, toPut);
            player.incrementStat(Stats.USED.getOrCreateStat(stackInHand.getItem()));
            if (!player.getAbilities().creativeMode) {
                stackInHand.decrement(1);
            }
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isPlayer()) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;

        var bottleType = state.get(BOTTLE);
        if (bottleType == XpExtractorBottleType.NONE || state.get(FULL)) {
            return;
        }

        var experience = bottleType.getExperience();
        if (player.totalExperience < experience) {
            return;
        }

        player.addExperience(-experience);
        fillBottle(state, world, pos);
        if (world.isClient) {
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.6F, 0.3F + player.getRandom()
                    .nextFloat() / 3F);
        } else if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, (double) pos.getX() + 0.5, (double) pos.getY() + 0.9, (double) pos.getZ() + 0.5, 20, 0.2, 0, 0.2, 2);
        }
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOTTLE, FULL);
    }

    static BlockState removeBottle(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState newState = state.with(BOTTLE, XpExtractorBottleType.NONE).with(FULL, false);
        world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        return newState;
    }

    static BlockState addBottle(BlockState state, WorldAccess world, BlockPos pos, ItemStack stack) {
        BlockState newState = state.with(BOTTLE, XpExtractorBottleType.TYPES.get(stack.getItem()))
                .with(FULL, false);
        world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        return newState;
    }

    static BlockState fillBottle(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState newState = state.with(FULL, true);
        world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        return newState;
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        var type = state.get(BOTTLE);
        var full = state.get(FULL);
        if (type == XpExtractorBottleType.NONE) {
            return new NoBottleInventory(state, world, pos);
        } else if (full) {
            return new FullBottleInventory(state, world, pos, new ItemStack(type.getFullItem()));
        } else {
            return new EmptyBottleInventory();
        }
    }


    static class FullBottleInventory extends SimpleInventory implements SidedInventory {
        private final BlockState state;
        private final WorldAccess world;
        private final BlockPos pos;
        private boolean dirty;

        public FullBottleInventory(BlockState state, WorldAccess world, BlockPos pos, ItemStack outputItem) {
            super(outputItem);
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        public int getMaxCountPerStack() {
            return 1;
        }

        public int[] getAvailableSlots(Direction side) {
            return new int[]{0};
        }

        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }

        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return !this.dirty && XpExtractorBottleType.EMPTY_BOTTLES.contains(stack.getItem());
        }

        public void markDirty() {
            world.setBlockState(pos, XpExtractorBlock.removeBottle(state, world, pos), Block.NOTIFY_ALL);
            this.dirty = true;
        }
    }

    static class NoBottleInventory extends SimpleInventory implements SidedInventory {
        private final BlockState state;
        private final WorldAccess world;
        private final BlockPos pos;
        private boolean dirty;

        public NoBottleInventory(BlockState state, WorldAccess world, BlockPos pos) {
            super(1);
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        public int getMaxCountPerStack() {
            return 1;
        }

        public int[] getAvailableSlots(Direction side) {
            return new int[]{0};
        }

        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return !this.dirty && ModItems.BOTTLES.contains(stack.getItem());
        }

        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return false;
        }

        public void markDirty() {
            ItemStack stack = this.getStack(0);
            if (!stack.isEmpty()) {
                this.dirty = true;
                BlockState newState = addBottle(state, world, pos, stack);
                this.world.syncWorldEvent(1500, this.pos, newState != state ? 1 : 0);
            }
        }
    }

    static class EmptyBottleInventory extends SimpleInventory implements SidedInventory {
        public EmptyBottleInventory() {
            super(0);
        }

        public int[] getAvailableSlots(Direction side) {
            return new int[0];
        }

        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }

        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return false;
        }
    }
}
