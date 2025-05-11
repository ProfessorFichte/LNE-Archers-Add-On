package com.lne_archers;

import com.lne_archers.item.WeaponsRegister;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;

import java.util.concurrent.CompletableFuture;

public class Lne_archersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ItemTagGenerator::new);
	}

	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateWeaponTags(WeaponsRegister.meleeEntries);
			var bowEntries = WeaponsRegister.rangedEntries.stream().map(entry ->
					new RPGSeriesDataGen.BowEntry(entry.id(), entry.weaponType, entry.lootProperties)
			).toList();
			generateBowTags(bowEntries);
		}
	}
}
