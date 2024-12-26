package com.lne_archers.entity.projectile;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

import java.util.List;
import java.util.Objects;

import static com.lne_archers.LNE_ArchersMod.tweaksConfig;
import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

public class WitherArrowEntity extends ArrowEntity {
    public WitherArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    private static final ParticleBatch particles = new ParticleBatch(
            "smoke",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            20,
            0.1F,
            0.3F,
            0);

    protected void onHit(LivingEntity target) {
        super.onHit(target);
        List<StatusEffectInstance> list = target.getStatusEffects().stream().toList();
        int negativeEffectsAmount = 0;
        if (!list.isEmpty()) {
            for (StatusEffectInstance statusEffectInstance : list) {
                StatusEffect statusEffect = statusEffectInstance.getEffectType();
                if (!statusEffect.isBeneficial()) {
                    negativeEffectsAmount++;
                }
            }
        }
        PlayerEntity playerEntity = (PlayerEntity) this.getOwner();
        float ranged_damage = (float) Objects.requireNonNull(playerEntity).getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.attribute);
        applyStatusEffect(target,0,10, StatusEffects.WITHER,0,
                false,true,false,0);
        float multiplication_per_effect = tweaksConfig.value.wither_arrow_percent_magic_damage_per_negative_effect;
        float magicDamage = ranged_damage * (multiplication_per_effect * negativeEffectsAmount);
        target.damage(target.getDamageSources().magic(), magicDamage);
        ParticleHelper.sendBatches(target, new ParticleBatch[]{particles});
    }
}
