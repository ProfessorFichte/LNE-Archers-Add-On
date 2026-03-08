package com.lne_archers.datagen;

import com.lne_archers.spells.ArchersSpells;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.datagen.SpellGenerator;

import java.util.concurrent.CompletableFuture;

public class ArchersAbilityDatagen extends SpellGenerator {
    public ArchersAbilityDatagen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateSpells(Builder builder) {
        for (var entry : ArchersSpells.entries) {
            builder.add(entry.id(), entry.spell());
        }
    }
}
