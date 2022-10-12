package github.erb3.mod.simplexp.item.custom;

import github.erb3.mod.simplexp.entity.XpBottleEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class XpBottleItem extends Item {

    private final int xpAmount;
    private final int color;

    public XpBottleItem(int xpAmount, int color, float variation, float force, Settings settings) {
        super(settings);
        this.xpAmount = xpAmount;
        this.color = color;

        DispenserBlock.registerBehavior(this, new ProjectileDispenserBehavior() {

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                XpBottleEntity bottleEntity = new XpBottleEntity(position.getX(), position.getY(), position.getZ(), world);
                return Util.make(bottleEntity, entity -> adjustXPBottleEntity(entity, stack));
            }

            @Override
            protected float getVariation() {
                return super.getVariation() * 0.5f * variation;
            }

            @Override
            protected float getForce() {
                return super.getForce() * force;
            }
        });
    }

    public void adjustXPBottleEntity(XpBottleEntity bottleEntity, ItemStack stack) {
        bottleEntity.setItem(stack);
        bottleEntity.setXPpoints(xpAmount);
        bottleEntity.setColor(color);
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
            XpBottleEntity bottleEntity = new XpBottleEntity(world, player);
            adjustXPBottleEntity(bottleEntity, itemStack);
            bottleEntity.setVelocity(player, player.getPitch(), player.getYaw(), -20.0f, 0.7f, 1.0f);
            world.spawnEntity(bottleEntity);
        }

        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
