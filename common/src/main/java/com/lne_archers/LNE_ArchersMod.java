package com.lne_archers;

import com.lne_archers.config.Default;
import com.lne_archers.config.ItemConfig;
import com.lne_archers.effects.Effects;
import com.lne_archers.entity.ModEntitiesRegistry;
import com.lne_archers.item.WeaponsRegister;
import com.lne_archers.sounds.LneArchersSounds;
import com.lne_archers.spells.CustomSpellImpacts;
import com.lne_archers.config.TweaksConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LNE_ArchersMod{
	public static final String MOD_ID = "lne_archers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<ItemConfig>
			("items_v2", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<TweaksConfig>
			("tweaks", new TweaksConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
			("effects_v0", new ConfigFile.Effects())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	public static final String ARCHERS_EXPANSION_MOD_ID = "archers_expansion";
	public static final String ARCHERS_EXPANSION_COMPAT_PACK_PATH = "resourcepacks/archers_expansion_compat";

	public static void init() {
		CustomSpellImpacts.registerCustomDeliveries();
		tweaksConfig.refresh();
	}
	public static void registerEntityAttributes() {
	}
	public static void registerEntities() {
		ModEntitiesRegistry.registerEntities();
	}
	public static void registerItems(){
		if(FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
			itemConfig.refresh();
			WeaponsRegister.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
			itemConfig.save();
		}
	}
	public static void registerSounds() {
		LneArchersSounds.register();
	}
	public static void registerEffects(){
		effectConfig.refresh();
		Effects.register(effectConfig.value);
		effectConfig.save();
	}
}