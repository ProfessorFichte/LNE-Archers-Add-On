package com.lne_archers.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;

public class RangersFocusParticles implements CustomParticleStatusEffect.Spawner{
    private final ParticleBatch particles;

    public RangersFocusParticles(int particleCount) {
        this.particles = new ParticleBatch(
                "spell_engine:magic_nature_spark_float",
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                null, particleCount,0.01F, 0.03F, 0,0.5F);
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var scaledParticles = new ParticleBatch(particles);
        scaledParticles.count = (1);
        ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
    }
}
