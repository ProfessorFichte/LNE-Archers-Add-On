package com.lne_archers.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lne_archers.item.WeaponsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Generate models for ranged weapons (bows and crossbows)
        for (var entry : WeaponsRegister.rangedEntries) {
            Item item = entry.item();
            if (item == null) continue;

            Identifier itemId = Registries.ITEM.getId(item);
            String name = itemId.getPath();

            // Determine if it's a bow or crossbow
            boolean isBow = name.contains("_bow") && !name.contains("cross");
            boolean isCrossbow = name.contains("crossbow");

            if (isBow) {
                generateBowModel(itemModelGenerator, itemId, name);
            } else if (isCrossbow) {
                generateCrossbowModel(itemModelGenerator, itemId, name);
            }
        }

        // Generate models for melee weapons (spears) - simple handheld
        for (var entry : WeaponsRegister.meleeEntries) {
            Item item = entry.item();
            if (item == null) continue;

            Identifier itemId = Registries.ITEM.getId(item);
            String name = itemId.getPath();
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

            JsonObject json = new JsonObject();
            json.addProperty("parent", "archers:item/base/spear_28");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", MOD_ID + ":item/" + name);
            json.add("textures", textures);

            itemModelGenerator.writer.accept(modelId, () -> json);
        }
    }

    /**
     * Generates bow model with pulling animations
     */
    private void generateBowModel(ItemModelGenerator itemModelGenerator, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        if(name.contains("long_bow") ){
            json.addProperty("parent", "archers:item/base/bow_20");
        }
        if(name.contains("_bow")&& !name.contains("long_bow")){
            json.addProperty("parent", "archers:item/base/bow_16");
        }


        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name);
        json.add("textures", textures);


        // Add display settings for  long bows
        JsonObject display = new JsonObject();
        if(name.contains("long_bow") ){
            JsonObject gui = new JsonObject();
            JsonArray rotation = new JsonArray();
            JsonArray translation = new JsonArray();
            JsonArray scale = new JsonArray();
            rotation.add(0);
            rotation.add(0);
            rotation.add(0);
            gui.add("rotation", rotation);
            translation.add(0);
            translation.add(0);
            translation.add(0);
            gui.add("translation", translation);
            scale.add(1.2);
            scale.add(1.2);
            scale.add(1);
            gui.add("scale", scale);

            display.add("gui", gui);
            json.add("display", display);
        }

        // Add pulling animation overrides
        JsonArray overrides = new JsonArray();

        // Pulling override
        JsonObject pullingOverride = new JsonObject();
        JsonObject pullingPredicate = new JsonObject();
        pullingPredicate.addProperty("pulling", 1);
        pullingOverride.add("predicate", pullingPredicate);
        pullingOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_0");
        overrides.add(pullingOverride);

        // Pull states (0, 1, 2)
        for (int i = 0; i <= 2; i++) {
            JsonObject pullOverride = new JsonObject();
            JsonObject pullPredicate = new JsonObject();
            pullPredicate.addProperty("pulling", 1);
            pullPredicate.addProperty("pull", (i + 1) * 0.333);
            pullOverride.add("predicate", pullPredicate);
            pullOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_" + i);
            overrides.add(pullOverride);
        }

        json.add("overrides", overrides);
        itemModelGenerator.writer.accept(modelId, () -> json);

        // Generate pulling state models
        for (int i = 0; i <= 2; i++) {
            Identifier pullingModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_pulling_" + i);
            JsonObject pullingJson = new JsonObject();
            pullingJson.addProperty("parent", MOD_ID + ":item/" + name);
            JsonObject pullingTextures = new JsonObject();
            pullingTextures.addProperty("layer0", MOD_ID + ":item/bow_pulling/" + name + "_pulling_" + i);
            pullingJson.add("textures", pullingTextures);
            itemModelGenerator.writer.accept(pullingModelId, () -> pullingJson);
        }
    }

    /**
     * Generates crossbow model with loading animations
     */
    private void generateCrossbowModel(ItemModelGenerator itemModelGenerator, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        if(name.contains("rapid_crossbow")){
            json.addProperty("parent", "archers:item/base/crossbow_16");
        }
        if(name.contains("heavy_crossbow")){
            json.addProperty("parent", "archers:item/base/crossbow_18");
        }

        // Add display settings for  heavy crossbows
        JsonObject display = new JsonObject();
        double scaling = 1.6;
        if(name.contains("elder_guardian_heavy_crossbow") ) {
            scaling = 1.4;
        }
        if(name.contains("heavy_crossbow") ){
            JsonObject gui = new JsonObject();
            JsonArray rotation = new JsonArray();
            JsonArray translation = new JsonArray();
            JsonArray scale = new JsonArray();
            rotation.add(0);
            rotation.add(0);
            rotation.add(0);
            gui.add("rotation", rotation);
            translation.add(0);
            translation.add(0);
            translation.add(0);
            gui.add("translation", translation);
            scale.add(scaling);
            scale.add(scaling);
            scale.add(1);
            gui.add("scale", scale);

            display.add("gui", gui);
            json.add("display", display);
        }

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name);
        json.add("textures", textures);

        // Add overrides for crossbow states
        JsonArray overrides = new JsonArray();

        // Pulling override
        JsonObject pullingOverride = new JsonObject();
        JsonObject pullingPredicate = new JsonObject();
        pullingPredicate.addProperty("pulling", 1);
        pullingOverride.add("predicate", pullingPredicate);
        pullingOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_0");
        overrides.add(pullingOverride);

        // Pull states
        for (int i = 0; i <= 2; i++) {
            JsonObject pullOverride = new JsonObject();
            JsonObject pullPredicate = new JsonObject();
            pullPredicate.addProperty("pulling", 1);
            pullPredicate.addProperty("pull", (i + 1) * 0.333);
            pullOverride.add("predicate", pullPredicate);
            pullOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_" + i);
            overrides.add(pullOverride);
        }

        // Charged override
        JsonObject chargedOverride = new JsonObject();
        JsonObject chargedPredicate = new JsonObject();
        chargedPredicate.addProperty("charged", 1);
        chargedOverride.add("predicate", chargedPredicate);
        chargedOverride.addProperty("model", MOD_ID + ":item/" + name + "_arrow");
        overrides.add(chargedOverride);

        // Firework override
        JsonObject fireworkOverride = new JsonObject();
        JsonObject fireworkPredicate = new JsonObject();
        fireworkPredicate.addProperty("charged", 1);
        fireworkPredicate.addProperty("firework", 1);
        fireworkOverride.add("predicate", fireworkPredicate);
        fireworkOverride.addProperty("model", MOD_ID + ":item/" + name + "_firework");
        overrides.add(fireworkOverride);

        json.add("overrides", overrides);
        itemModelGenerator.writer.accept(modelId, () -> json);

        // Generate pulling state models
        for (int i = 0; i <= 2; i++) {
            Identifier pullingModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_pulling_" + i);
            JsonObject pullingJson = new JsonObject();
            pullingJson.addProperty("parent", MOD_ID + ":item/" + name);
            JsonObject pullingTextures = new JsonObject();
            pullingTextures.addProperty("layer0", MOD_ID + ":item/bow_pulling/" + name + "_pulling_" + i);
            pullingJson.add("textures", pullingTextures);
            itemModelGenerator.writer.accept(pullingModelId, () -> pullingJson);
        }

        // Generate charged (arrow) model
        Identifier arrowModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_arrow");
        JsonObject arrowJson = new JsonObject();
        arrowJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject arrowTextures = new JsonObject();
        arrowTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_arrow");
        arrowJson.add("textures", arrowTextures);
        itemModelGenerator.writer.accept(arrowModelId, () -> arrowJson);

        // Generate firework model
        Identifier fireworkModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_firework");
        JsonObject fireworkJson = new JsonObject();
        fireworkJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject fireworkTextures = new JsonObject();
        fireworkTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_firework");
        fireworkJson.add("textures", fireworkTextures);
        itemModelGenerator.writer.accept(fireworkModelId, () -> fireworkJson);
    }
}
