package com.lne_archers;

import com.lne_archers.datagen.*;
import com.lne_archers.item.WeaponsRegister;
import com.lne_archers.sounds.LneArchersSounds;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.datagen.SimpleSoundGeneratorV2;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;

import java.util.concurrent.CompletableFuture;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class Lne_archersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Register all datagen providers
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(WeaponAttributesGenerator::new);
		pack.addProvider(ArchersAbilityDatagen::new);
		pack.addProvider(SoundGen::new);
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

	public static class SoundGen extends SimpleSoundGeneratorV2 {
		public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSounds(Builder builder) {
			builder.entries.add(new Entry(MOD_ID,
							LneArchersSounds.entries.stream()
									.map(entry -> SoundEntry.withVariants(entry.id().getPath(), entry.variants()))
									.toList()
					)
			);
		}
	}
}
