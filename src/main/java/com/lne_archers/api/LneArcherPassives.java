package com.lne_archers.api;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.util.HelperMethods;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;

import java.util.List;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;


public class LneArcherPassives {

    public static void dragonBow(LivingEntity attacker, LivingEntity target){
        float damage = (float) attacker.getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.attribute) * 0.15F;
        target.timeUntilRegen = 0;
        target.damage(target.getDamageSources().dragonBreath(), damage);
    }
    public static void glacialBow(LivingEntity target){
        applyStatusEffect(target, 0, 6, Effects.FREEZING, 1,
                true, true, false, 0);
        HelperMethods.stackFreezeStacks(target, 20);
    }
    public static void elderGuardianBow(World world, LivingEntity target, float ranged_damage){
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            float amplifier_multiplier = 0.15F;
            int effect_duration = 120;
            int amplifier = (int) (ranged_damage * amplifier_multiplier);
            target.addStatusEffect(new StatusEffectInstance(MRPGCEffects.BLEEDING, effect_duration, amplifier,
                    false, false, true));
        } else {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 6, 0,
                    false, false, true));
        }
    }
    public static void witherBow(LivingEntity target){
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
        applyStatusEffect(target, negativeEffectsAmount, 5, StatusEffects.WITHER, 0,
                false, true, false, 0);
    }
}
