package com.lne_archers.client;

import com.lne_archers.client.effect.RangersFocusParticles;
import com.lne_archers.client.entity.InfiltratorsArrowRenderer;
import com.lne_archers.client.entity.WintersGripRenderer;
import com.lne_archers.client.render.FrozenSlaveOverlayFeatureRenderer;
import com.lne_archers.effects.Effects;
import com.lne_archers.entity.InfiltratorsArrowProjectile;
import com.lne_archers.entity.WintersGripEntity;
import com.lne_archers.spells.ArchersSpells;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.client.gui.SpellTooltip;

public class LNE_ArchersClient{
    public static void init() {
        for (var entry : ArchersSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }
        CustomParticleStatusEffect.register(Effects.RANGERS_FOCUS.effect, new RangersFocusParticles(15));

        EntityRendererRegistry.register(WintersGripEntity.ENTITY_TYPE, WintersGripRenderer::new);
        EntityRendererRegistry.register(InfiltratorsArrowProjectile.ENTITY_TYPE, InfiltratorsArrowRenderer::new);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) ->
                registerFrozenSlaveOverlay(entityRenderer, registrationHelper));
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void registerFrozenSlaveOverlay(
            LivingEntityRenderer<?, ?> entityRenderer,
            LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper registrationHelper) {
        @SuppressWarnings("unchecked")
        var typedRenderer = (LivingEntityRenderer<T, M>) entityRenderer;
        registrationHelper.register(new FrozenSlaveOverlayFeatureRenderer<>(typedRenderer));
    }
}
