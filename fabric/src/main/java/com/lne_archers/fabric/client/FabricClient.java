package com.lne_archers.fabric.client;

import com.lne_archers.client.LNE_ArchersClient;
import com.lne_archers.client.entity.InfiltratorsArrowRenderer;
import com.lne_archers.client.entity.WintersGripRenderer;
import com.lne_archers.client.render.FrozenSlaveOverlayFeatureRenderer;
import com.lne_archers.entity.InfiltratorsArrowProjectile;
import com.lne_archers.entity.WintersGripEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LNE_ArchersClient.init();

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
