package com.lne_archers.compat;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

// Loot & Explore has no Forge build, so its tab key is spelled out by id instead of importing anything from more_rpg_loot.
public class LootNExplore {
    public static final String MOD_ID = "loot_n_explore";

    public static final RegistryKey<ItemGroup> GROUP_KEY =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "loot.generic"));
}
