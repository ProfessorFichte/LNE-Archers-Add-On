package com.lne_archers.fabric;

import com.lne_archers.LNE_ArchersMod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        LNE_ArchersMod.init();
        LNE_ArchersMod.registerEntities();
        LNE_ArchersMod.registerEntityAttributes();
        LNE_ArchersMod.registerItems();
        LNE_ArchersMod.registerEffects();
        LNE_ArchersMod.registerSounds();
        registerArchersExpansionCompatPack();
    }

    private static void registerArchersExpansionCompatPack() {
        if (!FabricLoader.getInstance().isModLoaded(LNE_ArchersMod.ARCHERS_EXPANSION_MOD_ID)) {
            return;
        }
        FabricLoader.getInstance().getModContainer(LNE_ArchersMod.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Identifier.of(LNE_ArchersMod.MOD_ID, "archers_expansion_compat"),
                        container,
                        ResourcePackActivationType.ALWAYS_ENABLED));
    }
}
