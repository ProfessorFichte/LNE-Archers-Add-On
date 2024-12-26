package com.lne_archers.entity.projectile;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.util.HelperMethods;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

public class GlacialArrowEntity extends ArrowEntity {
    public GlacialArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    private static final ParticleBatch particles = new ParticleBatch(
            "spell_engine:frost_hit",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            20,
            0.3F,
            0.5F,
            0);

    protected void onHit(LivingEntity target) {
        super.onHit(target);
        applyStatusEffect(target,0,6, Effects.FREEZING,1,
                true,true,false,0);
        HelperMethods.stackFreezeStacks(target,5);
        ParticleHelper.sendBatches(target, new ParticleBatch[]{particles});
    }
}
