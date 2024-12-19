package com.lne_archers;

import com.lne_archers.config.Default;
import com.lne_archers.config.ItemConfig;
import com.lne_archers.item.WeaponRegister;
import net.fabricmc.api.ModInitializer;

import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LNE_ArchersMod implements ModInitializer {
	public static final String MOD_ID = "lne_archers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<ItemConfig>
			("items", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	@Override
	public void onInitialize() {
		itemConfig.refresh();
		WeaponRegister.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
		itemConfig.save();
	}
}