package com.lne_archers.forge.client;

import com.lne_archers.client.LNE_ArchersClient;
import com.lne_archers.client.entity.InfiltratorsArrowRenderer;
import com.lne_archers.client.entity.WintersGripRenderer;
import com.lne_archers.client.render.FrozenSlaveOverlayFeatureRenderer;
import com.lne_archers.entity.InfiltratorsArrowProjectile;
import com.lne_archers.entity.WintersGripEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.registry.Registries;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.spell_engine.client.gui.ConfigMenuScreen;

public class ForgeClientMod {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClientMod::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class,
                ForgeClientMod::onRegisterRenderers);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.AddLayers.class,
                ForgeClientMod::onAddLayers);
    }

    @SuppressWarnings("removal")
    private static void onClientSetup(FMLClientSetupEvent event) {
        LNE_ArchersClient.init();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new ConfigMenuScreen(parent)));
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WintersGripEntity.ENTITY_TYPE, WintersGripRenderer::new);
        event.registerEntityRenderer(InfiltratorsArrowProjectile.ENTITY_TYPE, InfiltratorsArrowRenderer::new);
    }

    private static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (var entityType : Registries.ENTITY_TYPE) {
            @SuppressWarnings("unchecked")
            var livingType = (EntityType<? extends LivingEntity>) entityType;
            if (!DefaultAttributeRegistry.hasDefinitionFor(livingType)) {
                continue;
            }
            LivingEntityRenderer<?, ?> renderer = event.getRenderer(livingType);
            if (renderer != null) {
                registerFrozenSlaveOverlay(renderer);
            }
        }
        for (var skin : event.getSkins()) {
            var renderer = event.getSkin(skin);
            if (renderer != null) {
                registerFrozenSlaveOverlay(renderer);
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
