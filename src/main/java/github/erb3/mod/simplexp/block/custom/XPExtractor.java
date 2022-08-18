package github.erb3.mod.simplexp.block.custom;

import github.erb3.mod.simplexp.blockentity.XPExtractorBlockEntity;
import github.erb3.mod.simplexp.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class XPExtractor extends Block implements BlockEntityProvider {

    public static final EnumProperty<XPExtractorBottleType> bottle = EnumProperty.of("bottle", XPExtractorBottleType.class);

    public XPExtractor(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(bottle, XPExtractorBottleType.NONE));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XPExtractorBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) return ActionResult.SUCCESS;

        Inventory blockEntity = (Inventory) world.getBlockEntity(blockPos);

        if (blockEntity == null) {
            return ActionResult.SUCCESS;
        }

        if (!blockEntity.getStack(1).isEmpty()) {
            player.getInventory().offerOrDrop(blockEntity.getStack(1));
            blockEntity.removeStack(1);
            world.setBlockState(blockPos, blockState.with(bottle, XPExtractorBottleType.NONE));
        } else if (!blockEntity.getStack(0).isEmpty()) {
            player.getInventory().offerOrDrop(blockEntity.getStack(0));
            blockEntity.removeStack(0);
            world.setBlockState(blockPos, blockState.with(bottle, XPExtractorBottleType.NONE));
        } else {
            Item itemInHand = player.getStackInHand(hand).getItem().asItem();

            if (itemInHand == ModItems.SMALl_BOTTLE || itemInHand == ModItems.MEDIUM_BOTTLE || itemInHand == ModItems.LARGE_BOTTLE) {
                ItemStack toPut = player.getStackInHand(hand).copy();
                player.getStackInHand(hand).setCount(toPut.getCount() - 1);
                toPut.setCount(1);
                blockEntity.setStack(0, toPut);
                ServerPlayerEntity p = (ServerPlayerEntity) player;
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world.isClient()) return;
        if (!entity.isPlayer()) return;
        PlayerEntity p = (PlayerEntity) entity;

        Inventory blockEntity = (Inventory) world.getBlockEntity(pos);
        if(blockEntity == null) return;

        if (blockEntity.getStack(0).isEmpty()) return;
        if (blockEntity.getStack(1).getCount() == 1) return;

        Item i = blockEntity.getStack(0).getItem().asItem();
        if (i == ModItems.SMALl_BOTTLE) {
            if (!(p.totalExperience >= 55)) {
                return;
            }
            p.addExperience(-55);

            blockEntity.setStack(1, ModItems.BASIC_XP_BOTTLE.getDefaultStack());
            blockEntity.removeStack(0);
        } else if (i == ModItems.MEDIUM_BOTTLE) {
            if (!(p.totalExperience >= 550)) {
                return;
            }
            p.addExperience(-550);

            blockEntity.setStack(1, ModItems.BETTER_XP_BOTTLE.getDefaultStack());
            blockEntity.removeStack(0);
        } else if (i == ModItems.LARGE_BOTTLE) {
            if (!(p.totalExperience >= 2920)) {
                return;
            }
            p.addExperience(-2920);

            blockEntity.setStack(1, ModItems.BEST_XP_BOTTLE.getDefaultStack());
            blockEntity.removeStack(0);
        }
    }



    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(bottle);
    }
}
