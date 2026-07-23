package com.lne_archers.spells;

import com.lne_archers.effects.Effects;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class ArchersSpells {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable net.spell_engine.client.gui.SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }
    private static Spell.Impact.TargetModifier createDenyModifier(String entityTypeTag) {
        var modifier = new Spell.Impact.TargetModifier();
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityTypeTag;
        modifier.conditions = List.of(condition);
        modifier.execute = TriState.DENY;
        return modifier;
    }

    public static Entry dragon_breath = add(dragon_breath());
    private static Entry dragon_breath() {
        var id = Identifier.of(MOD_ID, "dragon_breath");
        var title = "Ender Dragon's Breath";
        var description = "On arrow hit: {trigger_chance} chance to create a dragon breath cloud, dealing {damage} damage per second.";

        var spell = new Spell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0.0F;
        spell.tier = 8;
        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        trigger.chance = 0.3F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver = new Spell.Delivery();
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        spell.deliver.delay = 5;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 5.0F;
        cloud.volume.area = new Spell.Target.Area();
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.volume.sound = new Sound(Identifier.of("entity.ender_dragon.ambient"));
        cloud.time_to_live_seconds = 4.0F;
        cloud.impact_tick_interval = 8;

        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = new ParticleBatch[]{
            new ParticleBatch(
                "dragon_breath",
                ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                20.0F, 0.0F, 0.0F
            ),
            new ParticleBatch(
                    SpellEngineParticles.MagicParticles.get(
                            SpellEngineParticles.MagicParticles.Shape.ARCANE,
                            SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                10.0F, 0.1F, 0.5F
            ).color(Color.ARCANE.toRGBA())
        };

        cloud.spawn = new Spell.Delivery.Cloud.Spawn();
        cloud.spawn.sound = new Sound(Identifier.of("entity.ender_dragon.shoot"));

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.5F);
        damage.attribute = "ranged_weapon:damage";
        damage.action.damage.knockback = 0.5F;
        damage.particles = new ParticleBatch[]{
            new ParticleBatch(
                    SpellEngineParticles.MagicParticles.get(
                            SpellEngineParticles.MagicParticles.Shape.SPELL,
                            SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                20.0F, 0.05F, 0.15F
            ).color(Color.ARCANE.toRGBA())
        };

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 8.0F);
        spell.cost.batching = true;
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }

    public static Entry reef_arrows = add(reef_arrows());
    private static Entry reef_arrows() {
        var id = Identifier.of(MOD_ID, "reef_arrows");
        var title = "Coral Reef Arrows";
        var description = "On arrow hit: {trigger_chance} chance to inflict bleeding for {effect_duration} seconds and dealing additional {damage} damage.";

        var spell = new Spell();
        spell.school = MoreSpellSchools.WATER;
        spell.range = 0.0F;
        spell.tier = 8;
        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var bleedingEffect = SpellBuilder.Impacts.effectSet(SpellEngineEffects.BLEED.id.toString(), 5.0F, 0);
        bleedingEffect.attribute = "ranged_weapon:damage";
        bleedingEffect.action.status_effect.amplifier_power_multiplier = 0.2F;
        bleedingEffect.action.status_effect.amplifier_cap = 2;
        bleedingEffect.action.status_effect.show_particles = false;
        bleedingEffect.particles = new ParticleBatch[]{
            new ParticleBatch(
                SpellEngineParticles.dripping_blood.id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                10.0F, 0.05F, 0.3F
            )
        };

        var damage = SpellBuilder.Impacts.damage(0.5F);
        damage.attribute = "ranged_weapon:damage";
        damage.action.damage.knockback = 0.5F;
        damage.particles = new ParticleBatch[]{
            new ParticleBatch(
                "more_rpg_classes:big_splash",
                ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                20.0F, 0.05F, 0.2F
            ),
            new ParticleBatch(
                "more_rpg_classes:splash",
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                15.0F, 0.05F, 0.2F
            )
        };
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:water_magic_impact1"), 0.4F);

        spell.impacts = List.of(bleedingEffect, damage);

        SpellBuilder.Cost.cooldown(spell, 4.0F);
        spell.cost.batching = true;
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }

    public static Entry glacial_splitter = add(glacial_splitter());
    private static Entry glacial_splitter() {
        var id = Identifier.of(MOD_ID, "glacial_splitter");
        var title = "Glacial Splitter";
        var description = "Defeating Enemies spawns glacial projectiles that deal {damage} damage and freeze enemies for {effect_duration} seconds.";

        var spell = new Spell();
        spell.school = SpellSchools.FROST;
        spell.range = 32.0F;
        spell.tier = 8;
        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        var targetCondition = new Spell.TargetCondition();
        targetCondition.health_percent_below = 0.0F;
        trigger.target_conditions = List.of(targetCondition);
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = "DAMAGE";
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver = new Spell.Delivery();
        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;

        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        var centerOffset = new Spell.Delivery.ShootProjectile.DirectionOffset();
        var offset1 = new Spell.Delivery.ShootProjectile.DirectionOffset();
        offset1.yaw = 20.0F;
        var offset2 = new Spell.Delivery.ShootProjectile.DirectionOffset();
        offset2.yaw = 35.0F;
        var offset3 = new Spell.Delivery.ShootProjectile.DirectionOffset();
        offset3.yaw = -20.0F;
        var offset4 = new Spell.Delivery.ShootProjectile.DirectionOffset();
        offset4.yaw = -35.0F;
        spell.deliver.projectile.direction_offsets = new Spell.Delivery.ShootProjectile.DirectionOffset[]{
            centerOffset,
            offset1,
            offset2,
            offset3,
            offset4
        };
        spell.deliver.projectile.direct_towards_target = true;
        spell.deliver.projectile.launch_properties.velocity = 1.2F;
        spell.deliver.projectile.launch_properties.extra_launch_count = 4;
        spell.deliver.projectile.launch_properties.extra_launch_delay = 0;
        spell.deliver.projectile.launch_properties.sound = Sound.withVolume(
            Identifier.of("spell_engine:generic_frost_impact"), 0.6F
        );

        spell.deliver.projectile.projectile = new Spell.ProjectileData();
        spell.deliver.projectile.projectile.homing_angle = 0.0F;

        spell.deliver.projectile.projectile.client_data = new Spell.ProjectileData.Client();
        var glacialArrowModel = SpellBuilder.ProjectileModels.model("lne_archers:spell_projectile/glacial_arrow", 2.0F);
        glacialArrowModel.rotate_degrees_per_tick = 0.0F;
        spell.deliver.projectile.projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(glacialArrowModel);

        spell.release.particles = new ParticleBatch[]{
            new ParticleBatch(
                    SpellEngineParticles.MagicParticles.get(
                            SpellEngineParticles.MagicParticles.Shape.FROST,
                            SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                25.0F, 0.2F, 0.7F
            ).color(Color.FROST.toRGBA())
        };
        spell.release.sound = new Sound(Identifier.of("spell_engine:generic_frost_impact"));

        var damage = SpellBuilder.Impacts.damage(0.35F);
        damage.attribute = "ranged_weapon:damage";
        damage.action.damage.knockback = 0.5F;
        damage.particles = new ParticleBatch[]{
            new ParticleBatch(
                    SpellEngineParticles.MagicParticles.get(
                            SpellEngineParticles.MagicParticles.Shape.FROST,
                            SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                25.0F, 0.2F, 0.7F
            ).color(Color.FROST.toRGBA())
        };
        damage.sound = new Sound(Identifier.of("spell_engine:generic_frost_impact"));

        var frostedEffect = SpellBuilder.Impacts.effectSet("more_rpg_classes:frosted", 5.0F, 0);
        frostedEffect.target_modifiers = List.of(
            createDenyModifier("#minecraft:freeze_immune_entity_types")
        );
        frostedEffect.action.status_effect.show_particles = false;
        frostedEffect.particles = new ParticleBatch[]{
            new ParticleBatch(
                    SpellEngineParticles.MagicParticles.get(
                            SpellEngineParticles.MagicParticles.Shape.FROST,
                            SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                5.0F, 0.1F, 0.35F
            ).color(Color.FROST.toRGBA())
        };

        spell.impacts = List.of(damage, frostedEffect);

        SpellBuilder.Cost.cooldown(spell, 4.0F);
        spell.cost.batching = true;
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }
    public static Entry cursed_wither_bolt = add(cursed_wither_bolt());
    private static Entry cursed_wither_bolt() {
        var id = Identifier.of(MOD_ID, "cursed_wither_bolt");
        var title = "Cursed Wither Bolts";
        var description = "On arrow hit: {trigger_chance} chance to inflict wither's curse for {effect_duration} seconds and dealing additional {damage} damage.";

        var spell = new Spell();
        spell.school = SpellSchools.SOUL;
        spell.range = 0.0F;
        spell.tier = 8;
        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        trigger.chance = 0.35F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var witherEffect = SpellBuilder.Impacts.effectSet("loot_n_explore:withers_curse", 10.0F, 0);
        witherEffect.action.status_effect.show_particles = true;
        witherEffect.particles = new ParticleBatch[]{
            new ParticleBatch(
                    SpellEngineParticles.MagicParticles.get(
                            SpellEngineParticles.MagicParticles.Shape.SKULL,
                            SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                15.0F, 0.2F, 0.25F
            ).color(858993663L),
            new ParticleBatch(
                "sculk_soul",
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                20.0F, 0.3F, 0.4F
            )
        };
        witherEffect.sound = new Sound(Identifier.of("entity.wither.ambient"));

        var damage = SpellBuilder.Impacts.damage(0.2F);
        damage.attribute = "ranged_weapon:damage";

        spell.impacts = List.of(witherEffect, damage);

        SpellBuilder.Cost.cooldown(spell, 6.0F);
        spell.cost.batching = true;
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }

    public static Entry rangers_focus = add(rangers_focus());
    private static Entry rangers_focus() {
        var id = Identifier.of(MOD_ID, "rangers_focus");
        var title = "Ranger´s Focus";
        var description = "Charge up while standing still, then release to gain increased ranged damage & velocity and decreased pull time for {stash_duration} seconds, with a stronger effect the longer you charged. Creates a magic area impact on arrow impact.";

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
        var arrowGlowModel = SpellBuilder.ProjectileModels.model(null);
        arrowGlowModel.fx.light_emission = LightEmission.RADIATE;
        spell.arrow_perks.composite_model = SpellBuilder.ProjectileModels.composite(arrowGlowModel);
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

        return new Entry(id, spell, title, description, null);
    }
}
