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
        // Templates and additions from loot_n_explore mod
        var dragonTemplate = Identifier.of("loot_n_explore", "dragon_upgrade_smithing_template");
        var guardianTemplate = Identifier.of("loot_n_explore", "guardian_upgrade_smithing_template");
        var witherTemplate = Identifier.of("loot_n_explore", "wither_upgrade_smithing_template");
        var frostMonarchTemplate = Identifier.of("loot_n_explore", "frostmonarch_upgrade_smithing_template");

        var dragonScales = Identifier.of("loot_n_explore", "ender_dragon_scales");
        var guardianEye = Identifier.of("loot_n_explore", "elder_guardian_eye");
        var witherSpine = Identifier.of("loot_n_explore", "wither_spine");
        var frozenSoul = Identifier.of("loot_n_explore", "frozen_soul");

        // Base items from archers mod
        var netheriteShortbow = Identifier.of("archers", "netherite_shortbow");
        var netheriteLongbow = Identifier.of("archers", "netherite_longbow");
        var netheriteRapidCrossbow = Identifier.of("archers", "netherite_rapid_crossbow");
        var netheriteHeavyCrossbow = Identifier.of("archers", "netherite_heavy_crossbow");
        var netheriteSpear = Identifier.of("archers", "netherite_spear");

        // Get result items from registry
        var entries = WeaponsRegister.rangedEntries;
        var meleeEntries = WeaponsRegister.meleeEntries;

        // ENDER DRAGON WEAPONS - Short Bows
        createSmithingTransformRecipe(
            "ender_dragon_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(Identifier.of("lne_archers", "ender_dragon_bow")),
            "loot_n_explore"
        );

        // ENDER DRAGON WEAPONS - Long Bows
        createSmithingTransformRecipe(
            "ender_dragon_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(Identifier.of("lne_archers", "ender_dragon_long_bow")),
            "loot_n_explore"
        );

        // ENDER DRAGON WEAPONS - Rapid Crossbows
        createSmithingTransformRecipe(
            "ender_dragon_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(Identifier.of("lne_archers", "ender_dragon_rapid_crossbow")),
            "loot_n_explore"
        );

        // ENDER DRAGON WEAPONS - Heavy Crossbows
        createSmithingTransformRecipe(
            "ender_dragon_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(Identifier.of("lne_archers", "ender_dragon_heavy_crossbow")),
            "loot_n_explore"
        );

        // ENDER DRAGON WEAPONS - Spears
        createSmithingTransformRecipe(
            "ender_dragon_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(Identifier.of("lne_archers", "ender_dragon_spear")),
            "loot_n_explore"
        );

        // ELDER GUARDIAN WEAPONS - Short Bows
        createSmithingTransformRecipe(
            "elder_guardian_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(Identifier.of("lne_archers", "elder_guardian_bow")),
            "loot_n_explore"
        );

        // ELDER GUARDIAN WEAPONS - Long Bows
        createSmithingTransformRecipe(
            "elder_guardian_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(Identifier.of("lne_archers", "elder_guardian_long_bow")),
            "loot_n_explore"
        );

        // ELDER GUARDIAN WEAPONS - Rapid Crossbows
        createSmithingTransformRecipe(
            "elder_guardian_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(Identifier.of("lne_archers", "elder_guardian_rapid_crossbow")),
            "loot_n_explore"
        );

        // ELDER GUARDIAN WEAPONS - Heavy Crossbows
        createSmithingTransformRecipe(
            "elder_guardian_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(Identifier.of("lne_archers", "elder_guardian_heavy_crossbow")),
            "loot_n_explore"
        );

        // ELDER GUARDIAN WEAPONS - Spears
        createSmithingTransformRecipe(
            "elder_guardian_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(Identifier.of("lne_archers", "elder_guardian_spear")),
            "loot_n_explore"
        );

        // WITHER WEAPONS - Short Bows
        createSmithingTransformRecipe(
            "wither_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(Identifier.of("lne_archers", "wither_bow")),
            "loot_n_explore"
        );

        // WITHER WEAPONS - Long Bows
        createSmithingTransformRecipe(
            "wither_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(Identifier.of("lne_archers", "wither_long_bow")),
            "loot_n_explore"
        );

        // WITHER WEAPONS - Rapid Crossbows
        createSmithingTransformRecipe(
            "wither_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(Identifier.of("lne_archers", "wither_rapid_crossbow")),
            "loot_n_explore"
        );

        // WITHER WEAPONS - Heavy Crossbows
        createSmithingTransformRecipe(
            "wither_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(Identifier.of("lne_archers", "wither_heavy_crossbow")),
            "loot_n_explore"
        );

        // WITHER WEAPONS - Spears
        createSmithingTransformRecipe(
            "wither_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(Identifier.of("lne_archers", "wither_spear")),
            "loot_n_explore"
        );

        // GLACIAL WEAPONS - Short Bows
        createSmithingTransformRecipe(
            "glacial_bow_smithing",
            Registries.ITEM.get(netheriteShortbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(Identifier.of("lne_archers", "glacial_bow")),
            "loot_n_explore"
        );

        // GLACIAL WEAPONS - Long Bows
        createSmithingTransformRecipe(
            "glacial_long_bow_smithing",
            Registries.ITEM.get(netheriteLongbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(Identifier.of("lne_archers", "glacial_long_bow")),
            "loot_n_explore"
        );

        // GLACIAL WEAPONS - Rapid Crossbows
        createSmithingTransformRecipe(
            "glacial_rapid_crossbow_smithing",
            Registries.ITEM.get(netheriteRapidCrossbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(Identifier.of("lne_archers", "glacial_rapid_crossbow")),
            "loot_n_explore"
        );

        // GLACIAL WEAPONS - Heavy Crossbows
        createSmithingTransformRecipe(
            "glacial_heavy_crossbow_smithing",
            Registries.ITEM.get(netheriteHeavyCrossbow),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(Identifier.of("lne_archers", "glacial_heavy_crossbow")),
            "loot_n_explore"
        );

        // GLACIAL WEAPONS - Spears
        createSmithingTransformRecipe(
            "glacial_spear_smithing",
            Registries.ITEM.get(netheriteSpear),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(Identifier.of("lne_archers", "glacial_spear")),
            "loot_n_explore"
        );
    }
}
