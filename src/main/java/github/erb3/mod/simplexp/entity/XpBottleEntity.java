package github.erb3.mod.simplexp.entity;

import github.erb3.mod.simplexp.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class XpBottleEntity extends ThrownItemEntity {
    private int xpPoints = 7;
    private int particleColor = PotionUtil.getColor(Potions.LUCK);

    public XpBottleEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public XpBottleEntity(World world, LivingEntity owner) {
        super(EntityType.EXPERIENCE_BOTTLE, owner, world);
    }

    public void setXPpoints(int amount) {
        xpPoints = amount;
    }

    public void setColor(int color) {
        particleColor = color;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BASIC_XP_BOTTLE;
    }

    @Override
    protected float getGravity() {
        return 0.07f;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.world instanceof ServerWorld) {
            this.world.syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, this.getBlockPos(), particleColor);
            ExperienceOrbEntity.spawn((ServerWorld) this.world, this.getPos(), xpPoints);
            this.discard();
        }
    }
}
