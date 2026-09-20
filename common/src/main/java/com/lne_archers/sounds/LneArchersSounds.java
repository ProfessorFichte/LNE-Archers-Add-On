package com.lne_archers.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class LneArchersSounds {
    public record Entry(Identifier id, SoundEvent soundEvent, int variants) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(String name, int variants) {
        var id = new Identifier(MOD_ID, name);
        var soundEvent = SoundEvent.of(id);
        var entry = new Entry(id, soundEvent, variants);
        entries.add(entry);
        return entry;
    }

    public static final Entry FROZEN_SLAVE_POP = add("frozen_slave_pop", 1);

    public static Map<Identifier, SoundEvent> soundsToRegister() {
        var sounds = new LinkedHashMap<Identifier, SoundEvent>();
        for (var entry : entries) {
            sounds.put(entry.id(), entry.soundEvent());
        }
        return sounds;
    }

    public static void register() {
        soundsToRegister().forEach((id, soundEvent) -> Registry.register(Registries.SOUND_EVENT, id, soundEvent));
    }
}
