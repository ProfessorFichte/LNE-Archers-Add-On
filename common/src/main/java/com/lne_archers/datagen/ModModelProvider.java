package com.lne_archers.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lne_archers.item.WeaponsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.loader.api.FabricLoader;
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
        // No blocks in this mod
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
            json.addProperty("parent", "item/handheld");
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
        json.addProperty("parent", "item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name);
        json.add("textures", textures);

        // Add display settings for bow
        JsonObject display = new JsonObject();

        JsonObject thirdperson_righthand = new JsonObject();
        JsonArray rotation = new JsonArray();
        rotation.add(-80);
        rotation.add(260);
        rotation.add(-40);
        thirdperson_righthand.add("rotation", rotation);

        JsonArray translation = new JsonArray();
        translation.add(-1);
        translation.add(-2);
        translation.add(2.5);
        thirdperson_righthand.add("translation", translation);

        JsonArray scale = new JsonArray();
        scale.add(0.9);
        scale.add(0.9);
        scale.add(0.9);
        thirdperson_righthand.add("scale", scale);

        display.add("thirdperson_righthand", thirdperson_righthand);

        JsonObject firstperson_righthand = new JsonObject();
        JsonArray fpRotation = new JsonArray();
        fpRotation.add(0);
        fpRotation.add(-90);
        fpRotation.add(25);
        firstperson_righthand.add("rotation", fpRotation);

        JsonArray fpTranslation = new JsonArray();
        fpTranslation.add(1.13);
        fpTranslation.add(3.2);
        fpTranslation.add(1.13);
        firstperson_righthand.add("translation", fpTranslation);

        JsonArray fpScale = new JsonArray();
        fpScale.add(0.68);
        fpScale.add(0.68);
        fpScale.add(0.68);
        firstperson_righthand.add("scale", fpScale);

        display.add("firstperson_righthand", firstperson_righthand);
        json.add("display", display);

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
            pullingJson.addProperty("parent", "item/generated");
            JsonObject pullingTextures = new JsonObject();
            pullingTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_pulling_" + i);
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
        json.addProperty("parent", "item/generated");

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
            pullingJson.addProperty("parent", "item/generated");
            JsonObject pullingTextures = new JsonObject();
            pullingTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_pulling_" + i);
            pullingJson.add("textures", pullingTextures);
            itemModelGenerator.writer.accept(pullingModelId, () -> pullingJson);
        }

        // Generate charged (arrow) model
        Identifier arrowModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_arrow");
        JsonObject arrowJson = new JsonObject();
        arrowJson.addProperty("parent", "item/generated");
        JsonObject arrowTextures = new JsonObject();
        arrowTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_arrow");
        arrowJson.add("textures", arrowTextures);
        itemModelGenerator.writer.accept(arrowModelId, () -> arrowJson);

        // Generate firework model
        Identifier fireworkModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_firework");
        JsonObject fireworkJson = new JsonObject();
        fireworkJson.addProperty("parent", "item/generated");
        JsonObject fireworkTextures = new JsonObject();
        fireworkTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_firework");
        fireworkJson.add("textures", fireworkTextures);
        itemModelGenerator.writer.accept(fireworkModelId, () -> fireworkJson);
    }
}
