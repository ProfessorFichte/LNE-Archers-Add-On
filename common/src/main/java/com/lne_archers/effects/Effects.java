package com.lne_archers.effects;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.entity.ControlledOwnerAccess;
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
        if (entry == null) return null;
        entries.add(entry);
        return entry;
    }

    public static RegistryEntry<StatusEffect> getEntry(net.spell_engine.api.effect.Effects.Entry entry) {
        return Registries.STATUS_EFFECT.getEntry(entry.id).orElseThrow();
    }

    public static net.spell_engine.api.effect.Effects.Entry RANGERS_FOCUS = add(new net.spell_engine.api.effect.Effects.Entry(
            Identifier.of(MOD_ID, "rangers_focus"),
            "Ranger´s Focus",
            "Increases Ranged Damage & Velocity, reduces pull time.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x993333),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.PULL_TIME.id.toString(),
                                    -0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.VELOCITY.id.toString(),
                                    0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static final net.spell_engine.api.effect.Effects.Entry WINTERS_GRASP = add(new net.spell_engine.api.effect.Effects.Entry(
            Identifier.of(MOD_ID, "winters_grip"),
            "Winters Grip",
            "Freezes nearby targets solid on death.",
            new WintersGraspEffect(StatusEffectCategory.HARMFUL, 0x805e4d),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                            -0.15F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            ))
    ));

    public static final net.spell_engine.api.effect.Effects.Entry FROZEN_SLAVE = add(new net.spell_engine.api.effect.Effects.Entry(
            Identifier.of(MOD_ID, "frozen_slave"),
            "Frozen Slave",
            "Revived by the frost, bound to fight for its icy master until it fades.",
            new FrozenSlaveEffect(StatusEffectCategory.NEUTRAL, 0x99ccff),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                            0.2F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new AttributeModifier(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                            0.2F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            ))
    ));

    public static void register(ConfigFile.Effects config) {
        for (var entry : entries) {
            Synchronized.configure(entry.effect, true);
        }

        net.spell_engine.api.effect.Effects.register(entries, config.effects);

        OnRemoval.configure(FROZEN_SLAVE.effect, (context) -> {
            var entity = context.entity();
            if (!entity.isAlive()) return;

            LivingEntity owner = null;
            if (entity instanceof ControlledOwnerAccess access) {
                var ownerId = access.mrpg$getControlOwner();
                if (ownerId != null && entity.getWorld() instanceof ServerWorld serverWorld) {
                    owner = serverWorld.getEntity(ownerId) instanceof LivingEntity living && living.isAlive() ? living : null;
                }
                access.mrpg$setControlOwner(null);
            }

            if (owner == null) {
                entity.kill();
                return;
            }

            var source = owner instanceof PlayerEntity player
                    ? entity.getDamageSources().playerAttack(player)
                    : entity.getDamageSources().mobAttack(owner);
            entity.setHealth(1.0F);
            entity.damage(source, 1000F);
        });
    }
}
