package com.lne_archers.datagen;

import com.lne_archers.item.WeaponsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        if (!FabricLoader.getInstance().isModLoaded("spell_engine")) {
            return;
        }
        for (var entry : WeaponsRegister.meleeEntries) {
            var weaponType = RPGSeriesItemTags.WeaponType.get(entry.category());
            getOrCreateTagBuilder(weaponType).addOptional(entry.id());

            int tier = entry.lootProperties().tier();
            if (tier >= 0) {
                getOrCreateTagBuilder(RPGSeriesItemTags.LootTiers.get(tier, RPGSeriesItemTags.LootCategory.WEAPONS))
                    .addOptional(entry.id());
            }

            String lootTheme = entry.lootProperties().theme();
            if (lootTheme != null && !lootTheme.isEmpty()) {
                getOrCreateTagBuilder(RPGSeriesItemTags.LootThemes.get(lootTheme))
                    .addOptional(entry.id());
            }
        }

        // Note: Ranged weapons (bows/crossbows) use a different system via RangedConfig
        // and don't need item tags for their weapon type
    }
}
