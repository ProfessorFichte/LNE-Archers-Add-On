package com.lne_archers.effects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.tag.EntityTypeTags;
import net.more_rpg_classes.util.CustomMethods;

public class WintersGraspEffect extends StatusEffect {
    protected WintersGraspEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        EntityType<?> type = livingEntity.getType();
        if(!type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            CustomMethods.freezeDamageTicks(livingEntity);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity livingEntity, AttributeContainer attributes, int amplifier) {
        super.onApplied(livingEntity, attributes, amplifier);
        if (livingEntity.hasStatusEffect(Effects.FROZEN_SLAVE.effect)) {
            livingEntity.removeStatusEffect(Effects.WINTERS_GRASP.effect);
        }
    }
}
