package github.erb3.mod.simplexp.item.custom;

import github.erb3.mod.simplexp.entity.XPBottleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class xpBottle extends Item {

    private final int xpAmount;
    private final int color;

    public xpBottle(int ixpAmount, int icolor, Settings settings) {
        super(settings);
        xpAmount = ixpAmount;
        color = icolor;
    }

    @Override
    public boolean hasGlint(ItemStack item) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL,
                0.5f, 0.45f / (world.getRandom().nextFloat() * 0.35f + 0.9f));

        if (!world.isClient()) {
            XPBottleEntity bottleEntity = new XPBottleEntity(world, player);
            bottleEntity.setItem(itemStack);
            bottleEntity.setVelocity(player, player.getPitch(), player.getYaw(), -20.0f, 0.7f, 1.0f);
            bottleEntity.setXPpoints(xpAmount);
            bottleEntity.setColor(color);
            world.spawnEntity(bottleEntity);
        }

        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
