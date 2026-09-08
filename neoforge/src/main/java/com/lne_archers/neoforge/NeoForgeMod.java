package com.lne_archers.neoforge;

import com.lne_archers.LNE_ArchersMod;
import com.lne_archers.item.WeaponsRegister;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(LNE_ArchersMod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        LNE_ArchersMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        modBus.addListener(EntityAttributeCreationEvent.class, NeoForgeMod::registerAttributes);
        modBus.addListener(AddPackFindersEvent.class, NeoForgeMod::addPackFinders);
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
    }
    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!ModList.get().isLoaded("loot_n_explore")) {
            return;
        }
        if (!event.getTabKey().equals(WeaponsRegister.tabKey)) {
            return;
        }
        for (var entry : WeaponsRegister.rangedEntries) {
            event.add(entry.item());
        }
    }
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != ResourceType.SERVER_DATA) {
            return;
        }
        if (!ModList.get().isLoaded(LNE_ArchersMod.ARCHERS_EXPANSION_MOD_ID)) {
            return;
        }
        event.addPackFinders(
                Identifier.of(LNE_ArchersMod.MOD_ID, LNE_ArchersMod.ARCHERS_EXPANSION_COMPAT_PACK_PATH),
                ResourceType.SERVER_DATA,
                Text.literal("LNE Archers - Archers Expansion Compat"),
                ResourcePackSource.BUILTIN,
                true,
                ResourcePackProfile.InsertionPosition.TOP);
    }
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            LNE_ArchersMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            LNE_ArchersMod.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            LNE_ArchersMod.registerEffects();
        });
        event.register(RegistryKeys.ENTITY_TYPE, reg -> {
            LNE_ArchersMod.registerEntities();
        });
    }
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        LNE_ArchersMod.registerEntityAttributes();
    }
}
