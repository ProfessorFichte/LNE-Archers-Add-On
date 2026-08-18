package com.lne_archers.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class LneArchersSounds {
    public record Entry(Identifier id, SoundEvent soundEvent, int variants) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(String name, int variants) {
        var id = Identifier.of(MOD_ID, name);
        var soundEvent = SoundEvent.of(id);
        var entry = new Entry(id, soundEvent, variants);
        entries.add(entry);
        return entry;
    }

    public static final Entry FROZEN_SLAVE_POP = add("frozen_slave_pop", 1);

    public static void register() {
        for (var entry : entries) {
            Registry.register(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());

        }
    }
}
