package com.lne_archers.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

public class RangersFocusParticles implements CustomParticleStatusEffect.Spawner {
    private final ParticleGroup particles;

    public RangersFocusParticles(int particleCount) {
        this.particles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.FLOAT)
                .color(1728014079L)
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                        .count(particleCount).speed(0.01F, 0.03F)
                        .extent(1.0F).invert(true));
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var scaledParticles = particles.copy();
        scaledParticles.batch.count = 5;
        ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
    }
}
