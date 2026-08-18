package com.lne_archers.effects;

import com.lne_archers.sounds.LneArchersSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.client.particle.PopupParticleEffect;
import net.more_rpg_classes.effect.ControlEnemyStatusEffect;
import net.more_rpg_classes.entity.ControlledOwnerAccess;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_engine.utils.SoundHelper;

public class FrozenSlaveEffect extends ControlEnemyStatusEffect {
    protected FrozenSlaveEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    protected boolean isValidTarget(LivingEntity controlled, Entity target) {
        if (target == controlled || !(target instanceof LivingEntity livingTarget)) return false;
        var owner = owner(controlled);
        return owner != null && !isProtected(owner, livingTarget);
    }

    public void onApplied(LivingEntity livingEntity, int amplifier) {
        super.onApplied(livingEntity, amplifier);
        if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    new PopupParticleEffect(MoreParticles.POPUP, Effects.FROZEN_SLAVE.id, false, livingEntity.getId()),
                    livingEntity.getX(), livingEntity.getEyeY() + 0.2, livingEntity.getZ(),
                    1, 0, 0, 0, 0);
            SoundHelper.playSoundEvent(livingEntity.getWorld(), livingEntity, LneArchersSounds.FROZEN_SLAVE_POP.soundEvent());
        }
    }

    private static LivingEntity owner(LivingEntity controlled) {
        if (!(controlled instanceof ControlledOwnerAccess access)) return null;
        var ownerId = access.mrpg$getControlOwner();
        if (ownerId == null || !(controlled.getWorld() instanceof ServerWorld serverWorld)) return null;
        return serverWorld.getEntity(ownerId) instanceof LivingEntity living && living.isAlive() ? living : null;
    }

    private static boolean isProtected(LivingEntity owner, Entity other) {
        var relation = EntityRelations.getRelation(owner, other);
        return switch (relation) {
            case ALLY, FRIENDLY -> true;
            case MIXED, HOSTILE, NEUTRAL -> false;
        };
    }
}
