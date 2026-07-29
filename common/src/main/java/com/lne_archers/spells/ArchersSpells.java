package com.lne_archers.spells;

import com.lne_archers.effects.Effects;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class ArchersSpells {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Entry rangers_focus = add(rangers_focus());
    private static Entry rangers_focus() {
        var id = Identifier.of(MOD_ID, "rangers_focus");
        var title = "Ranger´s Focus";
        var description = "Charge up while standing still, then release to gain {attribute_bonuses} for {stash_duration} seconds, " +
                "scaling with charge time. Also creates a magic area impact on arrow hit.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0.0F;
        spell.tier = 5;

        var charge = SpellBuilder.Casting.charge(spell, 2F);
        charge.min_release_ratio = 0.34F;
        charge.bonus.stash_amplifier_add = 3;

        spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_area_charge");
        spell.active.cast.sound = Sound.withVolume(SpellEngineSounds.GENERIC_WIND_CASTING.id(), 0.2F);
        spell.active.cast.movement_speed = 0F;
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        25.0F, 0.05F, 0.1F
                ).preSpawnTravel(8).color(Color.NATURE.toRGBA())
        };


        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");
        spell.release.sound = new Sound(Identifier.of("archers:magic_arrow_impact"));

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = Effects.RANGERS_FOCUS.id.toString();
        spell.deliver.stash_effect.amplifier = 0;
        spell.deliver.stash_effect.duration = 12.0F;
        spell.deliver.stash_effect.consume = 0;
        var shootTrigger = new Spell.Trigger();
        shootTrigger.type = Spell.Trigger.Type.ARROW_SHOT;
        spell.deliver.stash_effect.triggers = List.of(
                shootTrigger
        );
        spell.deliver.stash_effect.impact_mode = Spell.Delivery.StashEffect.ImpactMode.TRANSFER;

        var damage = SpellBuilder.Impacts.damage(0.3F);
        damage.school = MoreSpellSchools.NATURE;
        damage.attribute = "ranged_weapon:damage";
        var particle1 = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
            10.0F, 0.02F, 0.1F
        ).color(Color.NATURE.toRGBA());
        damage.particles = new ParticleBatch[]{ particle1};
        damage.sound = new Sound(Identifier.of("archers:magic_arrow_impact"));

        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 3.0F;
        spell.area_impact.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.5F).color(Color.NATURE.toRGBA()).extent(3.0F)
        };

        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.bypass_iframes = true;
        spell.arrow_perks.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 20, 0.2F, 0.22F, 0).roll(5).color(Color.NATURE.toRGBA())
        };

        SpellBuilder.Cost.cooldown(spell, 33.0F);
        spell.cost.exhaust = 0.5F;

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var attributes = Effects.RANGERS_FOCUS.config().attributes();
            var damageBonus = attributes.get(0);
            var pullTimeBonus = attributes.get(1);
            var velocityBonus = attributes.get(2);
            var bonuses = SpellTooltip.bonus(damageBonus.value, damageBonus.operation) + " Ranged Damage, "
                    + SpellTooltip.bonus(pullTimeBonus.value, pullTimeBonus.operation) + " Pull Time, "
                    + SpellTooltip.bonus(velocityBonus.value, velocityBonus.operation) + " Velocity";
            return args.description().replace("{attribute_bonuses}", bonuses);
        };

        return new Entry(id, spell, title, description, mutator);
    }
}
