package com.lne_archers.spells;

import com.lne_archers.effects.Effects;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Easing;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;

import java.util.ArrayList;
import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class ArchersSpells {
    public record Entry(Identifier id, Spell spell, String title, String description) {
    }

    public static final List<Entry> entries = new ArrayList<>();


    private static Entry add(Entry entry) {
        if (entry == null) return null;
        entries.add(entry);
        return entry;
    }

    private static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityType;
        var modifier = new Spell.Impact.TargetModifier();
        modifier.conditions = List.of(condition);
        return modifier;
    }

    private static void bossImmuneDeny(Spell.Impact impact) {
        var modifier = createImpactModifier("#c:bosses");
        modifier.execute = net.spell_engine.api.util.TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }

    public static Entry rangers_focus = add(rangers_focus());
    private static Entry rangers_focus() {
        var id = Identifier.of(MOD_ID, "rangers_focus");
        var title = "Ranger´s Focus";
        var effect = Effects.RANGERS_FOCUS;
        var description = "Charge up while standing still, then release to gain "
                + TooltipTokens.effect(effect.id, 0, EntityAttributes_RangedWeapon.DAMAGE.id) + " Ranged Damage, "
                + TooltipTokens.effect(effect.id, 0, EntityAttributes_RangedWeapon.HASTE.id) + " Haste, "
                + TooltipTokens.effect(effect.id, 0, EntityAttributes_RangedWeapon.VELOCITY.id) + " Velocity"
                + " for {stash_duration} seconds, scaling with charge time. Also creates a magic area impact on arrow hit.";

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
        spell.active.cast.particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST, Color.NATURE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .count(25F).speed(0.05F, 0.1F)
                                .preTravel(8F)));


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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND, Color.NATURE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10F).speed(0.02F, 0.1F)));
        damage.sound = new Sound(Identifier.of("archers:magic_arrow_impact"));

        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 3.0F;
        spell.area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND, Color.NATURE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10).speed(0.2F, 0.5F).extent(3.0F)));

        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.bypass_iframes = true;
        spell.arrow_perks.travel_particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND, Color.NATURE)
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(20).speed(0.2F, 0.22F).roll(5F)));

        SpellBuilder.Cost.cooldown(spell, 33.0F);
        spell.cost.exhaust = 0.5F;

        return new Entry(id, spell, title, description);
    }
    public static final Entry fan_of_fire = add(fan_of_fire());
    private static Entry fan_of_fire() {
        var id = Identifier.of(MOD_ID, "fan_of_fire");
        var spell = SpellBuilder.createSpellActive();
        var title = "Fan of Fire";
        var description = "Calls explosive arrows, in an area dealing {damage} and setting enemies on fire.";
        spell.school = MoreSpellSchools.FIRE_RANGED;
        spell.range = 32;
        spell.tier = 5;

        spell.active.cast.duration = 1.0F;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:archery_upwards_pull");
        spell.active.cast.sound = new Sound("archers:bow_pull");

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.release.animation = PlayerAnimation.of("spell_engine:archery_upwards_release");
        spell.release.sound = new Sound("minecraft:item.crossbow.shoot");

        spell.deliver.type = Spell.Delivery.Type.METEOR;
        var meteor = new Spell.Delivery.Meteor();
        meteor.launch_height = 15;
        meteor.launch_radius = 5.0F;
        meteor.launch_properties = new Spell.LaunchProperties();
        meteor.launch_properties.velocity = 2.0F;
        meteor.launch_properties.extra_launch_count = 25;
        meteor.launch_properties.extra_launch_delay = 4;
        meteor.projectile = new Spell.ProjectileData();
        meteor.projectile.divergence = 0F;
        meteor.projectile.client_data = new Spell.ProjectileData.Client();
        meteor.projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.of("minecraft:large_smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(3).speed(0F, 0F)));
        meteor.projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("lne_archers:spell_projectile/smoldering_arrow", 1.3F);
        spell.deliver.meteor = meteor;

        var damage = SpellBuilder.Impacts.damage(0.3F, 0F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.fire_explosion)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(1).speed(0.2F, 0.5F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.3F).preTravel(2F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.2F, 0.5F).preTravel(4F)));
        damage.sound = Sound.withRandomness(Identifier.of("entity.generic.explode"),1.2F);

        var fire = SpellBuilder.Impacts.fire(5);

        spell.impacts = List.of(damage, fire);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 3.0F;
        spell.area_impact.area = new Spell.Target.Area();
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.area_impact.sound = new Sound("minecraft:entity.arrow.hit");

        SpellBuilder.Cost.cooldown(spell, 28);
        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.item(spell, "minecraft:arrow", 1);

        return new Entry(id, spell, title, description);
    }
    public static final Entry winters_grip = add(winters_grip());
    private static Entry winters_grip() {
        var id = Identifier.of(MOD_ID, "winters_grip");
        var spell = SpellBuilder.createSpellActive();
        var title = "Winters Grip";
        var description = "Spawns a winter totem, slowing enemies, freezing other enemies around on death.";
        spell.school = MoreSpellSchools.FROST_RANGED;
        spell.range = 0;
        spell.tier = 5;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 10.0F;
        cloud.volume.area = new Spell.Target.Area();
        cloud.volume.area.vertical_range_multiplier = 1.5F;
        cloud.volume.sound = Sound.withVolume(Identifier.of("spell_engine:generic_wind_charging"),0.1F);
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 10;
        cloud.spawn_ticks = 8;
        cloud.despawn_ticks = 6;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 14;
        int wintersGripTotalTicks = (int) (cloud.time_to_live_seconds * 20);
        float wintersGripScale = 1.5F;
        cloud.client_data.model_fx = List.of(
                ModelEffectBuilder.create("lne_archers:spell_effect/winters_grip")
                        .light(LightEmission.RADIATE)
                        .positioning(0F)
                        .scale(wintersGripScale)
                        .initialTranslateY(0.5F * (wintersGripScale - 1F) + 0.2F)
                        .duration(wintersGripTotalTicks)
                        .scaleIn(0, cloud.spawn_ticks, Easing.EASE_OUT_BOUNCE)
                        .scaleOut(wintersGripTotalTicks - cloud.despawn_ticks, wintersGripTotalTicks, Easing.EASE_IN_CUBIC)
                        .build()
        );
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(20).speed(0.05F, 0.1F)),
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(20).speed(0.25F, 0.4F)));
        cloud.placement = new Spell.EntityPlacement();
        cloud.placement.force_onto_ground = true;
        cloud.placement.location_offset_y = 0F;
        spell.deliver.clouds = List.of(cloud);

        var slow = SpellBuilder.Impacts.effectSet(Effects.WINTERS_GRASP.id.toString(), 1, 0);
        slow.action.status_effect.show_particles = false;
        bossImmuneDeny(slow);
        slow.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.BURST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.2F, 0.4F)));

        spell.impacts = List.of(slow);

        SpellBuilder.Cost.cooldown(spell, 25);
        spell.cost.cooldown.haste_affected = true;
        SpellBuilder.Cost.exhaust(spell, 0.4F);

        return new Entry(id, spell, title, description);
    }
    public static final Entry infiltrators_arrow = add(infiltrators_arrow());
    private static Entry infiltrators_arrow() {
        var id = Identifier.of(MOD_ID, "infiltrators_arrow");
        var spell = SpellBuilder.createSpellActive();
        var title = "Infiltrators Arrow";
        var description = "Shoots a short-range invisible arrow dealing {damage}. On impact, teleports you to the arrow and grants you invisibility.";
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 8;
        spell.tier = 5;

        var charge = SpellBuilder.Casting.charge(spell, 3.0F);
        charge.min_release_ratio = 0.3F;
        var bonus = charge.bonus;
        bonus.range_add = 22;

        spell.active.cast.animation = PlayerAnimation.of("spell_engine:archery_pull");
        spell.active.cast.sound = new Sound("archers:bow_pull");

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.release.animation = PlayerAnimation.of("spell_engine:archery_release");
        spell.release.sound = new Sound("entity.arrow.shoot");

        spell.deliver.type = Spell.Delivery.Type.CUSTOM;
        spell.deliver.custom = new Spell.Delivery.Custom();
        spell.deliver.custom.handler = "lne_archers:infiltrators_arrow";

        var damage = SpellBuilder.Impacts.damage(0.8F, 1.0F);

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 42);
        spell.cost.cooldown.haste_affected = false;
        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.item(spell, "minecraft:arrow", 1);

        return new Entry(id, spell, title, description);
    }
}
