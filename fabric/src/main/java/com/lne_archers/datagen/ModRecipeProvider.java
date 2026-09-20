package com.lne_archers.datagen;

import com.lne_archers.item.WeaponsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.datagen.SmithingRecipeGenerator;

public class ModRecipeProvider extends SmithingRecipeGenerator {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output, "lne_archers");
    }

    @Override
    public void generate() {
        var dragonTemplate = new Identifier("loot_n_explore", "dragon_upgrade_smithing_template");
        var guardianTemplate = new Identifier("loot_n_explore", "guardian_upgrade_smithing_template");
        var witherTemplate = new Identifier("loot_n_explore", "wither_upgrade_smithing_template");
        var frostMonarchTemplate = new Identifier("loot_n_explore", "frostmonarch_upgrade_smithing_template");

        var dragonScales = new Identifier("loot_n_explore", "ender_dragon_scales");
        var guardianEye = new Identifier("loot_n_explore", "elder_guardian_eye");
        var witherSpine = new Identifier("loot_n_explore", "wither_spine");
        var frozenSoul = new Identifier("loot_n_explore", "frozen_soul");

        var netheriteShortbow = new Identifier("archers", "netherite_shortbow");
        var netheriteLongbow = new Identifier("archers", "netherite_longbow");
        var netheriteRapidCrossbow = new Identifier("archers", "netherite_rapid_crossbow");
        var netheriteHeavyCrossbow = new Identifier("archers", "netherite_heavy_crossbow");
        var netheriteSpear = new Identifier("archers", "netherite_spear");

        var entries = WeaponsRegister.rangedEntries;
        var meleeEntries = WeaponsRegister.meleeEntries;

        createSmithingTransformRecipe(
            "ender_dragon_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_archers", "ender_dragon_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "ender_dragon_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_archers", "ender_dragon_long_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "ender_dragon_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_archers", "ender_dragon_rapid_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "ender_dragon_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_archers", "ender_dragon_heavy_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "ender_dragon_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_archers", "ender_dragon_spear")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "elder_guardian_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(new Identifier("lne_archers", "elder_guardian_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "elder_guardian_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(new Identifier("lne_archers", "elder_guardian_long_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "elder_guardian_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(new Identifier("lne_archers", "elder_guardian_rapid_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "elder_guardian_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(new Identifier("lne_archers", "elder_guardian_heavy_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "elder_guardian_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(new Identifier("lne_archers", "elder_guardian_spear")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "wither_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_archers", "wither_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "wither_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_archers", "wither_long_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "wither_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_archers", "wither_rapid_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "wither_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_archers", "wither_heavy_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "wither_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_archers", "wither_spear")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "glacial_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(new Identifier("lne_archers", "glacial_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "glacial_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(new Identifier("lne_archers", "glacial_long_bow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "glacial_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(new Identifier("lne_archers", "glacial_rapid_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "glacial_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(new Identifier("lne_archers", "glacial_heavy_crossbow")),
            "loot_n_explore"
        );

        createSmithingTransformRecipe(
            "glacial_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(new Identifier("lne_archers", "glacial_spear")),
            "loot_n_explore"
        );
    }
}
