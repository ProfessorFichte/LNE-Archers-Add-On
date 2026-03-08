package com.lne_archers.datagen;

import com.lne_archers.effects.Effects;
import com.lne_archers.item.WeaponsRegister;
import com.lne_archers.spells.ArchersSpells;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {

    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder builder) {

        // STATUS EFFECTS
        Effects.entries.forEach(entry -> {
            builder.add(entry.effect.getTranslationKey(), entry.title);
            builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
        });

        // SPELLS
        for (var entry : ArchersSpells.entries) {
            var id = entry.id();
            builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name", entry.title());
            builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description", entry.description());
        }

        // RANGED WEAPONS
        for (var entry : WeaponsRegister.rangedEntries) {
            builder.add("item." + entry.id().getNamespace() + "." + entry.id().getPath(), entry.translatedName());
        }

        // MELEE WEAPONS (Spears)
        for (var entry : WeaponsRegister.meleeEntries) {
            builder.add(entry.item().getTranslationKey(), entry.translatedName());
        }

        // Treasure Scrolls
        builder.add("item.archers.archer.treasure_spell_scroll", "Treasure Archery Scroll");
        builder.add("item.archers_expansion.deadeye.treasure_spell_scroll", "Treasure Deadeye Scroll");
        builder.add("item.archers_expansion.tundra_hunter.treasure_spell_scroll", "Treasure Tundra Hunter Scroll");
        builder.add("item.archers_expansion.war_archer.treasure_spell_scroll", "Treasure War Archer Scroll");

        // Maps
        builder.add("filled_map.loot_n_explore.archers_outposts", "Hidden Ranger Outpost Map");
        builder.add("filled_map.loot_n_explore.archers_camps", "Hidden Ranger Camp Map");
    }
}
