package com.lne_archers.neoforge;

import com.lne_archers.LNE_ArchersMod;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(LNE_ArchersMod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        LNE_ArchersMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        modBus.addListener(EntityAttributeCreationEvent.class, NeoForgeMod::registerAttributes);
    }
    public static void register(RegisterEvent event) {
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
