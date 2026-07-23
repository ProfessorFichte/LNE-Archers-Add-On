package com.lne_archers.client;

import com.lne_archers.client.effect.RangersFocusParticles;
import com.lne_archers.client.entity.FrozenSlaveRenderer;
import com.lne_archers.client.entity.WintersGripRenderer;
import com.lne_archers.effects.Effects;
import com.lne_archers.entity.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.spell_engine.api.effect.CustomParticleStatusEffect;

public class LNE_ArchersClient{
    public static void init() {
        CustomParticleStatusEffect.register(Effects.RANGERS_FOCUS.effect, new RangersFocusParticles(15));
        EntityRendererRegistry.register(ModEntities.WINTERS_GRIP_TYPE, WintersGripRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROZEN_SLAVE_TYPE, FrozenSlaveRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFILTRATOR_ARROW_TYPE, ArrowEntityRenderer::new);
    }
}
