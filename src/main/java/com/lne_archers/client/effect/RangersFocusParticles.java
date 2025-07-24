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
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                null, particleCount,0.01F, 0.03F, 0,1.0F).invert().color(1728014079);
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var scaledParticles = new ParticleBatch(particles);
        scaledParticles.count = (5);
        ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
    }
}
