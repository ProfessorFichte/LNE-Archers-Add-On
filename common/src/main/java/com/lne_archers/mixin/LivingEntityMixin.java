package com.lne_archers.mixin;

import com.lne_archers.effects.Effects;
import com.lne_archers.effects.FrozenSlaveVisualAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.more_rpg_classes.entity.ControlledOwnerAccess;
import net.spell_engine.internals.target.EntityRelations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.Box;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements FrozenSlaveVisualAccess {

    @Unique
    private static final int FROZEN_SLAVE_DURATION_TICKS = 300;

    @Unique
    private static final TagKey<EntityType<?>> BOSSES = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("c", "bosses"));

    // Mob status effects aren't synced to clients, so the ice overlay renderer can't just check hasStatusEffect(FROZEN_SLAVE) client-side - this tracked flag is the synced substitute.
    @Unique
    private static final TrackedData<Boolean> FROZEN_SLAVE_VISUAL = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void lneArchers$initFrozenSlaveVisual(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(FROZEN_SLAVE_VISUAL, false);
    }

    @Override
    public boolean lneArchers$isFrozenSlaveVisual() {
        LivingEntity entity = (LivingEntity)(Object)this;
        return entity.getDataTracker().get(FROZEN_SLAVE_VISUAL);
    }

    private static boolean lneArchers$isProtected(Entity target, LivingEntity attacker) {
        var relation = EntityRelations.getRelation(attacker, target);
        switch (relation) {
            case ALLY, FRIENDLY -> {
                return true;
            }
            case NEUTRAL, MIXED, HOSTILE -> {
                return false;
            }
        }
        return false;
    }


    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeath$wintersGrip(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.hasStatusEffect(Effects.getEntry(Effects.WINTERS_GRASP))) {
            float range = 3.0F;
            Box radius = new Box(entity.getX() + range,
                    entity.getY() + (float) range / 3,
                    entity.getZ() + range,
                    entity.getX() - range,
                    entity.getY() - (float) range / 3,
                    entity.getZ() - range);
            for(Entity entities : entity.getEntityWorld().getOtherEntities(entity, radius, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if (entities instanceof LivingEntity targets && !lneArchers$isProtected(targets, entity)) {
                        targets.addStatusEffect(new StatusEffectInstance(MRPGCEffects.FROZEN_SOLID.entry, 60, 0, false, false, true));
                    }
                }
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void lneArchers$wintersGripRevive(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.getWorld().isClient) return;
        if (!(entity instanceof MobEntity) || !(entity instanceof ControlledOwnerAccess access)) return;
        if (entity.getType().isIn(BOSSES)) return;

        var wintersGrip = Effects.getEntry(Effects.WINTERS_GRASP);
        if (!entity.hasStatusEffect(wintersGrip)) return;
        if (entity.hasStatusEffect(Effects.getEntry(Effects.FROZEN_SLAVE))) return;
        if (entity.getHealth() - amount > 0F) return;
        if (!(source.getAttacker() instanceof LivingEntity attacker) || attacker == entity) return;

        entity.removeStatusEffect(wintersGrip);
        entity.setHealth(entity.getMaxHealth());
        access.mrpg$setControlOwner(attacker.getUuid());
        entity.addStatusEffect(new StatusEffectInstance(
                Effects.getEntry(Effects.FROZEN_SLAVE),
                FROZEN_SLAVE_DURATION_TICKS, 0, false, false, true));
        entity.getDataTracker().set(FROZEN_SLAVE_VISUAL, true);
        cir.setReturnValue(false);
    }

}
