package com.lne_archers.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.more_rpg_classes.effect.ControlEnemyStatusEffect;
import net.more_rpg_classes.entity.ControlledOwnerAccess;
import net.spell_engine.internals.target.EntityRelations;

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
