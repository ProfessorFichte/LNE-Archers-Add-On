package com.lne_archers.spell;

import com.lne_archers.entity.InfiltratorArrowEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellPower;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class CustomSpellDeliveries {

    public static void register() {
        SpellHandlers.registerCustomDelivery(
            Identifier.of(MOD_ID, "infiltrators_arrow"),
            (world, spellEntry, caster, targets, context, targetLocation) -> {
                if (world.isClient) return false;

                var spell = spellEntry.value();
                var impactContext = context;
                if (impactContext.power() == null) {
                    impactContext = impactContext.power(SpellPower.getSpellPower(spell.school, caster));
                }

                var launchPoint = SpellHelper.launchPoint(caster);
                var arrow = InfiltratorArrowEntity.create(world, caster);
                arrow.setPosition(launchPoint);

                var dirToTarget = targetLocation.subtract(launchPoint).normalize();
                // MARK: Adjust arc strength here - higher value = more arc (more upward angle)
                double arcFactor = Math.min(launchPoint.distanceTo(targetLocation) * 0.025, 0.6);
                var velocity = new net.minecraft.util.math.Vec3d(
                    dirToTarget.x,
                    dirToTarget.y + arcFactor,
                    dirToTarget.z
                ).normalize().multiply(1.5F);

                arrow.setVelocity(velocity.x, velocity.y, velocity.z);
                world.spawnEntity(arrow);
                return true;
            }
        );
    }
}
