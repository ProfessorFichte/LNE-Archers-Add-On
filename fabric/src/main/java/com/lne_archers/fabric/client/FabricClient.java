package com.lne_archers.fabric.client;

import com.lne_archers.client.LNE_ArchersClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LNE_ArchersClient.init();

    }
}
