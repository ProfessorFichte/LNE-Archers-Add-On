package com.lne_archers.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lne_archers.item.WeaponsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.spell_engine.rpg_series.item.Equipment;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class WeaponAttributesGenerator implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final FabricDataOutput output;

    public WeaponAttributesGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        // Generate weapon attributes for ranged weapons
        for (var entry : WeaponsRegister.rangedEntries) {
            Identifier id = entry.id();
            String parent = getParentForWeaponType(entry.weaponType);
            if (parent != null) {
                futures.add(generateWeaponAttribute(writer, id, parent));
            }
        }

        /*
        // Generate weapon attributes for melee weapons (spears)
        for (var entry : WeaponsRegister.meleeEntries) {
            Identifier id = entry.id();
            String parent = getParentForWeaponType(entry.type());
            if (parent != null) {
                futures.add(generateWeaponAttribute(writer, id, parent));
            }
        }

         */

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private String getParentForWeaponType(Equipment.WeaponType weaponType) {
        return switch (weaponType) {
            case SHORT_BOW -> "bettercombat:bow";
            case LONG_BOW -> "bettercombat:bow_two_handed_heavy";
            case RAPID_CROSSBOW -> "bettercombat:crossbow";
            case HEAVY_CROSSBOW -> "bettercombat:crossbow_two_handed_heavy";
            case SPEAR -> "bettercombat:spear";
            default -> null;
        };
    }

    private CompletableFuture<?> generateWeaponAttribute(DataWriter writer, Identifier id, String parent) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", parent);

        Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "weapon_attributes").resolveJson(id);
        return DataProvider.writeToPath(writer, json, path);
    }

    @Override
    public String getName() {
        return "Weapon Attributes - " + MOD_ID;
    }
}
