package com.lne_archers.item;

import com.lne_archers.LNE_ArchersMod;
import com.lne_archers.item.weapons.*;
import more_rpg_loot.item.Group;
import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.CustomRangedWeapon;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class WeaponRegister {
    public static final ArrayList<RangedEntry> rangedEntries = new ArrayList<>();
    public static final ArrayList<Weapon.Entry> meleeEntries = new ArrayList<>();
    public record RangedEntry(Identifier id, Item item, RangedConfig defaults) { }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = new Identifier(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    //RANGED
    private static RangedEntry addRanged(Identifier id, Item item, RangedConfig defaults) {
        var entry = new RangedEntry(id, item, defaults);
        rangedEntries.add(entry);
        return entry;
    }
    //BOWS
    private static RangedEntry bow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new CustomBow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }
    private static RangedEntry dragonBow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new DragonBow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }
    private static RangedEntry guardianBow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new ElderGuardianBow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }
    private static RangedEntry guardianCrossbow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new ElderGuardianCrossbow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }
    private static RangedEntry glacialBow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new GlacialBow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }
    private static RangedEntry glacialCrossbow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new GlacialCrossbow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }

    private static RangedEntry crossbow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new CustomCrossbow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(LNE_ArchersMod.MOD_ID, name), item, defaults);
    }
    private static final int archers_pullTime_shortBow = 16;
    private static final int archers_pullTime_longBow = 30;
    private static final int archers_pullTime_rapidCrossbow = 20;
    private static final int archers_pullTime_heavyCrossbow = 35;
    public static int vanilla_bow_pull_time = 20;

    public static float bow_velocity = 0F;
    public static float short_bow_damage = 9.5F;
    public static float long_bow_damage = 12.5F;
    public static float rapid_crossbow_damage = 10.0F;
    public static float heavy_crossbow_damage = 16.0F;
    private static final int durabilityBows = ToolMaterials.NETHERITE.getDurability();

    //MELEE
    private static Weapon.Entry entryMelee(String requiredMod, String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(LNE_ArchersMod.MOD_ID, name, material, item, defaults, null);
        if (entry.isRequiredModInstalled()) {
            meleeEntries.add(entry);
        }
        return entry;
    }
    //SPEARS
    private static final float archers_spearAttackSpeed = -2.6F;
    private static final float spearAttackDamage = 7.5F;
    private static final float weaponSpellPower = 3.0F;
    private static Weapon.Entry spearDragon(String name, Weapon.CustomMaterial material) {
        return spearDragon(null, name, material);
    }
    private static Weapon.Entry spearDragon(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonSpear(material, settings);
        return entryMelee(requiredMod, name, material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }
    private static Weapon.Entry spearElderGuardian(String name, Weapon.CustomMaterial material) {
        return spearElderGuardian(null, name, material);
    }
    private static Weapon.Entry spearElderGuardian(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianSpear(material, settings);
        return entryMelee(requiredMod, name, material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }
    private static Weapon.Entry spearWither(String name, Weapon.CustomMaterial material) {
        return spearWither(null, name, material);
    }
    private static Weapon.Entry spearWither(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherSpear(material, settings);
        return entryMelee(requiredMod, name, material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }
    private static Weapon.Entry spearGlacial(String name, Weapon.CustomMaterial material) {
        return spearGlacial(null, name, material);
    }
    private static Weapon.Entry spearGlacial(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new GlacialSpear(material, settings);
        return entryMelee(requiredMod, name, material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }

    //Registration
    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, ItemConfig.Weapon> meleeConfig) {
        if (FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
            var dragonRepair = ingredient("loot_n_explore:ender_dragon_scales",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var elderGuardianRepair = ingredient("loot_n_explore:elder_guardian_eye",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var frostMonarchRepair = ingredient("loot_n_explore:frozen_soul",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var witherRepair = ingredient("minecraft:nether_star",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            //SPEARS
            spearDragon("ender_dragon_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, dragonRepair))
                            .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, weaponSpellPower));
            spearElderGuardian("elder_guardian_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, elderGuardianRepair))
                    .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));
            spearWither("wither_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, witherRepair))
                    .attribute(ItemConfig.Attribute.bonus(SpellSchools.SOUL.id, weaponSpellPower));
            spearGlacial("glacial_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, frostMonarchRepair))
                    .attribute(ItemConfig.Attribute.bonus(SpellSchools.FROST.id, weaponSpellPower));
            //SHORT BOWS
            dragonBow("ender_dragon_bow", durabilityBows, dragonRepair,
                    new RangedConfig(archers_pullTime_shortBow, short_bow_damage, bow_velocity));
            guardianBow("elder_guardian_bow", durabilityBows, elderGuardianRepair,
                    new RangedConfig(archers_pullTime_shortBow, short_bow_damage, bow_velocity));
            bow("wither_bow", durabilityBows, witherRepair,
                    new RangedConfig(archers_pullTime_shortBow, short_bow_damage, bow_velocity));
            glacialBow("glacial_bow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(archers_pullTime_shortBow, short_bow_damage, bow_velocity));
            //LONG BOWS
            dragonBow("ender_dragon_long_bow", durabilityBows, dragonRepair,
                    new RangedConfig(archers_pullTime_longBow, long_bow_damage, bow_velocity));
            guardianBow("elder_guardian_long_bow", durabilityBows, elderGuardianRepair,
                    new RangedConfig(archers_pullTime_longBow, long_bow_damage, bow_velocity));
            bow("wither_long_bow", durabilityBows, witherRepair,
                    new RangedConfig(archers_pullTime_longBow, long_bow_damage, bow_velocity));
            glacialBow("glacial_long_bow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(archers_pullTime_longBow, long_bow_damage, bow_velocity));
            //RAPID CROSSBOWS
            crossbow("ender_dragon_rapid_crossbow", durabilityBows, dragonRepair,
                    new RangedConfig(archers_pullTime_rapidCrossbow, rapid_crossbow_damage, bow_velocity));
            guardianCrossbow("elder_guardian_rapid_crossbow", durabilityBows, elderGuardianRepair,
                    new RangedConfig(archers_pullTime_rapidCrossbow, rapid_crossbow_damage, bow_velocity));
            crossbow("wither_rapid_crossbow", durabilityBows, witherRepair,
                    new RangedConfig(archers_pullTime_rapidCrossbow, rapid_crossbow_damage, bow_velocity));
            glacialCrossbow("glacial_rapid_crossbow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(archers_pullTime_rapidCrossbow, rapid_crossbow_damage, bow_velocity));
            //HEAVY CROSSBOWS
            crossbow("ender_dragon_heavy_crossbow", durabilityBows, dragonRepair,
                    new RangedConfig(archers_pullTime_heavyCrossbow, heavy_crossbow_damage, bow_velocity));
            guardianCrossbow("elder_guardian_heavy_crossbow", durabilityBows, elderGuardianRepair,
                    new RangedConfig(archers_pullTime_heavyCrossbow, heavy_crossbow_damage, bow_velocity));
            crossbow("wither_heavy_crossbow", durabilityBows, witherRepair,
                    new RangedConfig(archers_pullTime_heavyCrossbow, heavy_crossbow_damage, bow_velocity));
            glacialCrossbow("glacial_heavy_crossbow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(archers_pullTime_heavyCrossbow, heavy_crossbow_damage, bow_velocity));
        }


        Weapon.register(meleeConfig, meleeEntries, Group.RPG_LOOT_KEY);
        for (var entry: rangedEntries) {
            var config = rangedConfig.get(entry.id.toString());
            if (config == null) {
                config = entry.defaults;
                rangedConfig.put(entry.id.toString(), config);
            }
            ((CustomRangedWeapon)entry.item).configure(config);
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register((content) -> {
            for (var entry: rangedEntries) {
                content.add(entry.item);
            }
        });
    }
}
