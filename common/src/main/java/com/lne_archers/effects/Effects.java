package com.lne_archers.effects;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;

import java.util.ArrayList;
import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class Effects {
    public static final List<net.spell_engine.api.effect.Effects.Entry> entries = new ArrayList<>();

    private static net.spell_engine.api.effect.Effects.Entry add(net.spell_engine.api.effect.Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static net.spell_engine.api.effect.Effects.Entry RANGERS_FOCUS = add(new net.spell_engine.api.effect.Effects.Entry(
            Identifier.of(MOD_ID, "rangers_focus"),
            "Ranger´s Focus",
            "Increases Ranged Damage & Velocity, reduces movement speed and pull time.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x993333),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.PULL_TIME.id.toString(),
                                    -0.15F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.VELOCITY.id.toString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static void register(ConfigFile.Effects config) {
        for (var entry : entries) {
            Synchronized.configure(entry.effect, true);
        }

        net.spell_engine.api.effect.Effects.register(entries, config.effects);
    }
}
