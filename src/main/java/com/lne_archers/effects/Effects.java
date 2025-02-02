package com.lne_archers.effects;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;
import net.spell_engine.api.event.CombatEvents;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class Effects {
    public static StatusEffect RANGERS_FOCUS = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x993333);

    public static void register() {
        RANGERS_FOCUS.addAttributeModifier(EntityAttributes_RangedWeapon.DAMAGE.attribute, "213fa5bf-6933-4a97-8eb2-a349d8e321fd",
                0.5F, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                //.addAttributeModifier(EntityAttributes_RangedWeapon.HASTE.attribute, "dacb968b-2692-40a5-871f-04935c6172f6",
                //        -0.2F, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "5e58808d-6042-45c6-bb4d-f5fcd82f485e",
                        -0.5F, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        Synchronized.configure(RANGERS_FOCUS, true);

        CombatEvents.ENTITY_ATTACK.register((args) -> {
            var attacker = args.attacker();
            if (attacker.hasStatusEffect(RANGERS_FOCUS)) {
                attacker.removeStatusEffect(RANGERS_FOCUS);
            }
        });

        int ID = 20400;
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "rangers_focus").toString(), RANGERS_FOCUS);
    }
}
