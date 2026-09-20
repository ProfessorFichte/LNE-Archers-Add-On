package com.lne_archers.forge;

import com.lne_archers.LNE_ArchersMod;
import com.lne_archers.compat.LootNExplore;
import com.lne_archers.effects.Effects;
import com.lne_archers.entity.ModEntitiesRegistry;
import com.lne_archers.forge.client.ForgeClientMod;
import com.lne_archers.item.WeaponsRegister;
import com.lne_archers.sounds.LneArchersSounds;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.resource.PathPackResources;
import net.spell_engine.Platform;

@Mod(LNE_ArchersMod.MOD_ID)
public final class ForgeMod {
    private static final String COMPAT_PACK_ID = "lne_archers_archers_expansion_compat";

    @SuppressWarnings("removal")
    public ForgeMod() {
        LNE_ArchersMod.init();
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class,
                ForgeMod::buildTabContents);
        modBus.addListener(EventPriority.NORMAL, false, AddPackFindersEvent.class, ForgeMod::addPackFinders);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    // Goes through the helper on purpose, on Forge 47.0-47.3 a plain Registry.register throws "Can not register to a locked registry".
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper ->
                LneArchersSounds.soundsToRegister().forEach(helper::register));

        event.register(RegistryKeys.ENTITY_TYPE, helper ->
                ModEntitiesRegistry.entityTypesToRegister().forEach(helper::register));

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            LNE_ArchersMod.effectConfig.refresh();
            Effects.effectsToRegister(LNE_ArchersMod.effectConfig.value).forEach(helper::register);
            net.spell_engine.api.effect.Effects.linkEntries(Effects.entries);
            Effects.installBehaviours();
            LNE_ArchersMod.effectConfig.save();
        });

        event.register(RegistryKeys.ITEM, helper -> {
            if (Platform.util().isModLoaded(LootNExplore.MOD_ID)) {
                LNE_ArchersMod.itemConfig.refresh();
                WeaponsRegister.itemsToRegister(LNE_ArchersMod.itemConfig.value.ranged_weapons,
                                LNE_ArchersMod.itemConfig.value.melee_weapons)
                        .forEach(helper::register);
                LNE_ArchersMod.itemConfig.save();
            }
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(WeaponsRegister.tabKey)) {
            return;
        }
        for (var entry : WeaponsRegister.rangedEntries) {
            var item = entry.item();
            event.accept(() -> item);
        }
    }

    private static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != ResourceType.SERVER_DATA) {
            return;
        }
        if (!ModList.get().isLoaded(LNE_ArchersMod.ARCHERS_EXPANSION_MOD_ID)) {
            return;
        }
        var modFile = ModList.get().getModFileById(LNE_ArchersMod.MOD_ID);
        if (modFile == null) {
            return;
        }
        var packRoot = modFile.getFile().findResource(LNE_ArchersMod.ARCHERS_EXPANSION_COMPAT_PACK_PATH);
        event.addRepositorySource(profileAdder -> {
            var profile = ResourcePackProfile.create(
                    COMPAT_PACK_ID,
                    Text.literal("LNE Archers - Archers Expansion Compat"),
                    true,
                    name -> new PathPackResources(name, true, packRoot),
                    ResourceType.SERVER_DATA,
                    ResourcePackProfile.InsertionPosition.TOP,
                    ResourcePackSource.BUILTIN);
            if (profile != null) {
                profileAdder.accept(profile);
            } else {
                LNE_ArchersMod.LOGGER.warn("Built-in data pack '{}' could not be read from {}", COMPAT_PACK_ID, packRoot);
            }
        });
    }
}
