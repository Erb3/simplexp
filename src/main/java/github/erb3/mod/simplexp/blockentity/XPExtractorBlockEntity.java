package github.erb3.mod.simplexp.blockentity;

import github.erb3.mod.simplexp.block.custom.XPExtractorBottleType;
import github.erb3.mod.simplexp.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static github.erb3.mod.simplexp.block.custom.XPExtractor.bottle;

public class XPExtractorBlockEntity extends BlockEntity implements ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final BlockState state;
    private final BlockPos pos;

    public XPExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntites.XP_EXTRACTOR_BE, pos, state);
        this.pos = pos;
        this.state = state;
    }

    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        if (slot != 0) return false;

        if (!items.get(0).isEmpty()) return false;

        Item i = stack.getItem();
        return i == ModItems.SMALl_BOTTLE || i == ModItems.MEDIUM_BOTTLE || i == ModItems.LARGE_BOTTLE;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return slot == 1;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

        getItems().set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }

        Item itemInHand = stack.getItem().asItem();
        if(world == null || world.isClient()) {
            return;
        }

        if (itemInHand == ModItems.SMALl_BOTTLE) {
            world.setBlockState(pos, state.with(bottle, XPExtractorBottleType.SMALL));
        } else if (itemInHand == ModItems.MEDIUM_BOTTLE) {
            world.setBlockState(pos, state.with(bottle, XPExtractorBottleType.MEDIUM));
        } else if (itemInHand == ModItems.LARGE_BOTTLE) {
            world.setBlockState(pos, state.with(bottle, XPExtractorBottleType.LARGE));
        } else if (itemInHand == ModItems.BEST_XP_BOTTLE) {
            world.setBlockState(pos, state.with(bottle, XPExtractorBottleType.SMALL_FILLED));
        }
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (world != null && !world.isClient) {
            world.setBlockState(pos, state.with(bottle, XPExtractorBottleType.NONE));
        }

        return Inventories.removeStack(getItems(), slot);
    }

}
