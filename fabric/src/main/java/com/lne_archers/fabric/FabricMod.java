package com.lne_archers.fabric;

import com.lne_archers.LNE_ArchersMod;
import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        LNE_ArchersMod.init();
        LNE_ArchersMod.registerEntityAttributes();
        LNE_ArchersMod.registerItems();
        LNE_ArchersMod.registerEffects();
    }
}
