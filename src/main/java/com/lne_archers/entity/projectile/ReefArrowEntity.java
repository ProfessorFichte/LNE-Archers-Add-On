package com.lne_archers.entity.projectile;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

import java.util.Objects;


public class ReefArrowEntity extends ArrowEntity {
    public ReefArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }
    protected float getDragInWater() {
        return 0.99F;
    }

    private static final ParticleBatch particles = new ParticleBatch(
            "more_rpg_classes:big_splash",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            10,
            0.3F,
            0.5F,
            0);

    protected void onHit(LivingEntity target) {
        super.onHit(target);
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            PlayerEntity playerEntity = (PlayerEntity) this.getOwner();
            float ranged_damage = (float) Objects.requireNonNull(playerEntity).getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.attribute);
            float amplifier_multiplier = 0.15F;
            int effect_duration = 120;
            int amplifier = (int) (ranged_damage * amplifier_multiplier);
            target.addStatusEffect(new StatusEffectInstance(MRPGCEffects.BLEEDING, effect_duration, amplifier, false, false, true));
            ParticleHelper.sendBatches(target, new ParticleBatch[]{particles});
        }

    }
}
