package com.lne_archers.effects;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;

import java.util.ArrayList;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class Effects {
    private static final ArrayList<Entry> entries = new ArrayList<Entry>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(MOD_ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(MOD_ID, "effect." + id.getPath());
        }
    }

    public static final Entry RANGERS_FOCUS =  new Effects.Entry("rangers_focus",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x993333));

    public static void register() {
        RANGERS_FOCUS.effect.addAttributeModifier(EntityAttributes_RangedWeapon.DAMAGE.entry, RANGERS_FOCUS.modifierId(),
                0.4F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(EntityAttributes_RangedWeapon.PULL_TIME.entry, RANGERS_FOCUS.modifierId(),
                        -0.3F, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(EntityAttributes_RangedWeapon.VELOCITY.entry, RANGERS_FOCUS.modifierId(),
                        0.3F, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, RANGERS_FOCUS.modifierId(),
                        -0.75F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        Synchronized.configure(RANGERS_FOCUS.effect, true);


        for (Entry entry: entries) {
            entry.register();
        }
    }
}
