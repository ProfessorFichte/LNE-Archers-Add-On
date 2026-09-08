package com.lne_archers.neoforge.client;

import com.lne_archers.LNE_ArchersMod;
import com.lne_archers.client.LNE_ArchersClient;
import com.lne_archers.client.entity.InfiltratorsArrowRenderer;
import com.lne_archers.client.entity.WintersGripRenderer;
import com.lne_archers.client.render.FrozenSlaveOverlayFeatureRenderer;
import com.lne_archers.entity.InfiltratorsArrowProjectile;
import com.lne_archers.entity.WintersGripEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = LNE_ArchersMod.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LNE_ArchersClient.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, parent) -> new ConfigMenuScreen(parent));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WintersGripEntity.ENTITY_TYPE, WintersGripRenderer::new);
        event.registerEntityRenderer(InfiltratorsArrowProjectile.ENTITY_TYPE, InfiltratorsArrowRenderer::new);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (var entityType : event.getEntityTypes()) {
            if (event.getRenderer(entityType) instanceof LivingEntityRenderer<?, ?> livingRenderer) {
                registerFrozenSlaveOverlay(livingRenderer);
            }
        }
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void registerFrozenSlaveOverlay(
            LivingEntityRenderer<?, ?> entityRenderer) {
        @SuppressWarnings("unchecked")
        var typedRenderer = (LivingEntityRenderer<T, M>) entityRenderer;
        typedRenderer.addFeature(new FrozenSlaveOverlayFeatureRenderer<>(typedRenderer));
    }
}
