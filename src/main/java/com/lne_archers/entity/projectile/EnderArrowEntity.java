package com.lne_archers.entity.projectile;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

import java.util.Objects;

public class EnderArrowEntity extends ArrowEntity {
    public EnderArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    private static final ParticleBatch particles = new ParticleBatch(
            "spell_engine:arcane_hit",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            25,
            0.3F,
            0.7F,
            0);

    protected void onHit(LivingEntity target) {
        super.onHit(target);
        PlayerEntity playerEntity = (PlayerEntity) this.getOwner();
        float ranged_damage = (float) Objects.requireNonNull(playerEntity).getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.attribute);
        float multiplicator = 0.2F;
        if(this.isShotFromCrossbow()){
            multiplicator = 0.40F;
        }
        float magicDamage = ranged_damage * multiplicator;
        target.damage(target.getDamageSources().magic(), magicDamage);
        ParticleHelper.sendBatches(target, new ParticleBatch[]{particles});

    }
}
