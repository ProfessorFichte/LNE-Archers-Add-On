package com.lne_archers.item;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.item.Group;
import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.item.weapon.SpellWeaponItem;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;
import static com.lne_archers.LNE_ArchersMod.tweaksConfig;

public class WeaponsRegister {
    public static final ArrayList<RangedEntry> rangedEntries = new ArrayList<>();
    public static final ArrayList<Weapon.Entry> meleeEntries = new ArrayList<>();

    public interface RangedFactory {
        Item create(Item.Settings settings, RangedConfig config, Supplier<Ingredient> repairIngredientSupplier);
    }


    public static final class RangedEntry {
        private final Identifier id;
        private final RangedFactory factory;
        private final RangedConfig defaults;
        private final Supplier<Ingredient> repairIngredientSupplier;
        private final int durability;
        public List<Identifier> spells = null;

        public Item item;

        public Equipment.LootProperties lootProperties = Equipment.LootProperties.EMPTY;
        public Equipment.WeaponType weaponType = Equipment.WeaponType.SHORT_BOW;

        public RangedEntry(Identifier id, RangedFactory factory, RangedConfig defaults, Supplier<Ingredient> repairIngredientSupplier, int durability) {
            this.id = id;
            this.factory = factory;
            this.defaults = defaults;
            this.repairIngredientSupplier = repairIngredientSupplier;
            this.durability = durability;
        }

        public Identifier id() {
            return id;
        }

        public Item create(Item.Settings settings, RangedConfig config) {
            this.item = factory.create(
                    settings.maxDamage(durability),
                    config,
                    repairIngredientSupplier
            );
            return this.item;
        }

        public Item item() {
            return item;
        }

        public RangedEntry weaponType(Equipment.WeaponType weaponType) {
            this.weaponType = weaponType;
            return this;
        }

        public RangedEntry loot(Equipment.LootProperties lootProperties) {
            this.lootProperties = lootProperties;
            return this;
        }
        public RangedEntry castSpell() {
            spells = List.of();
            return this;
        }

        public RangedEntry spell(Identifier spellId) {
            spells = List.of(spellId);
            return this;
        }
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
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
    private static RangedEntry bow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var entry = new RangedEntry(Identifier.of(MOD_ID, name), CustomBow::new, defaults, repairIngredientSupplier, durability);
        rangedEntries.add(entry);
        return entry;
    }

    private static RangedEntry crossbow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var entry = new RangedEntry(Identifier.of(MOD_ID, name), CustomCrossbow::new, defaults, repairIngredientSupplier, durability);
        rangedEntries.add(entry);
        return entry;
    }


    private static final float pullTime_shortBow = 0.8F - 1F;
    private static final float pullTime_longBow = 1.5F - 1F;
    private static final float pullTime_rapidCrossbow = 0;
    private static final float pullTime_heavyCrossbow = 1.75F - 1F;
    private static final float velocity_shortBow = 0F;
    private static final float velocity_longBow = 0.75F;
    private static final float velocity_rapidCrossbow = 0F;
    private static final float velocity_heavyCrossbow = 0.5F;

    public static float short_bow_damage = 10F;
    public static float long_bow_damage = 13.5F;
    public static float rapid_crossbow_damage = 10.5F;
    public static float heavy_crossbow_damage = 17.0F;
    private static final int durabilityBows = ToolMaterials.NETHERITE.getDurability();

    //SPEARS
    private static Weapon.Entry addMelee(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType type) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, type);
        meleeEntries.add(entry);
        return entry;
    }

    private static Weapon.Entry spear(String name, Weapon.CustomMaterial material, float damage) {
        return addMelee(name, material, SpellWeaponItem::new, new WeaponConfig(damage, archers_spearAttackSpeed), Equipment.WeaponType.SPEAR);
    }
    private static final float archers_spearAttackSpeed = -2.6F;
    private static final float spearAttackDamage = 8.0F;
    private static final float weaponSpellPower = 4.0F;
    ///MELEE PASSIVES
    public static Identifier dragonclaw = Identifier.of(RPGLoot.MOD_ID, "dragonclaw");
    public static Identifier avalanche = Identifier.of(RPGLoot.MOD_ID, "avalanche");
    public static Identifier waterbomb = Identifier.of(RPGLoot.MOD_ID, "waterbomb");
    public static Identifier wither_pulse = Identifier.of(RPGLoot.MOD_ID, "wither_pulse");
    ///RANGED PASSIVES
    public static Identifier dragon_breath = Identifier.of(MOD_ID, "dragon_breath");
    public static Identifier reef_arrows = Identifier.of(MOD_ID, "reef_arrows");
    public static Identifier glacial_splitter = Identifier.of(MOD_ID, "glacial_splitter");
    public static Identifier cursed_wither_bolt = Identifier.of(MOD_ID, "cursed_wither_bolt");

    //Registration
    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, WeaponConfig> meleeConfig) {
        if (!tweaksConfig.value.disable_special_lne_weapons) {
            var dragonRepair = ingredient("minecraft:amethyst_shard",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var elderGuardianRepair = ingredient("minecraft:prismarine_shard",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var frostMonarchRepair = ingredient("minecraft:ice",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var witherRepair = ingredient("minecraft:bone",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            //SPEARS
            spear("ender_dragon_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, dragonRepair),spearAttackDamage)
                    .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, weaponSpellPower))
                    .spell(dragonclaw);
            spear("elder_guardian_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, elderGuardianRepair),spearAttackDamage)
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.WATER.id, weaponSpellPower))
                    .spell(waterbomb);
            spear("wither_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, witherRepair),spearAttackDamage)
                    .attribute(AttributeModifier.bonus(SpellSchools.SOUL.id, weaponSpellPower))
                    .spell(wither_pulse);
            spear("glacial_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, frostMonarchRepair),spearAttackDamage)
                    .attribute(AttributeModifier.bonus(SpellSchools.FROST.id, weaponSpellPower))
                    .spell(avalanche);
            //SHORT BOWS
            bow("ender_dragon_bow", durabilityBows, dragonRepair,
                    new RangedConfig(short_bow_damage, pullTime_shortBow,velocity_shortBow)
                    .withAttribute(SpellSchools.ARCANE.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.SHORT_BOW)
                    .spell(dragon_breath);
            bow("elder_guardian_bow", durabilityBows, elderGuardianRepair,
                    new RangedConfig(short_bow_damage, pullTime_shortBow,velocity_shortBow)
                            .withAttribute(MoreSpellSchools.WATER.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.SHORT_BOW)
                    .spell(reef_arrows);
            bow("wither_bow", durabilityBows, witherRepair,
                    new RangedConfig(short_bow_damage, pullTime_shortBow,velocity_shortBow)
                            .withAttribute(SpellSchools.SOUL.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.SHORT_BOW)
                    .spell(cursed_wither_bolt);
            bow("glacial_bow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(short_bow_damage, pullTime_shortBow,velocity_shortBow)
                            .withAttribute(SpellSchools.FROST.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.SHORT_BOW)
                    .spell(glacial_splitter);
            //LONG BOWS
            bow("ender_dragon_long_bow", durabilityBows, dragonRepair,
                    new RangedConfig(long_bow_damage, pullTime_longBow, velocity_longBow)
                            .withAttribute(SpellSchools.ARCANE.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.LONG_BOW)
                    .spell(dragon_breath);
            bow("elder_guardian_long_bow", durabilityBows, elderGuardianRepair,
                    new RangedConfig(long_bow_damage, pullTime_longBow, velocity_longBow)
                            .withAttribute(MoreSpellSchools.WATER.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.LONG_BOW)
                    .spell(reef_arrows);
            bow("wither_long_bow", durabilityBows, witherRepair,
                    new RangedConfig(long_bow_damage, pullTime_longBow, velocity_longBow)
                            .withAttribute(SpellSchools.SOUL.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.LONG_BOW)
                    .spell(cursed_wither_bolt);
            bow("glacial_long_bow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(long_bow_damage, pullTime_longBow, velocity_longBow)
                            .withAttribute(SpellSchools.FROST.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.LONG_BOW)
                    .spell(glacial_splitter);
            //RAPID CROSSBOWS
            crossbow("ender_dragon_rapid_crossbow", durabilityBows, dragonRepair,
                    new RangedConfig(rapid_crossbow_damage, pullTime_rapidCrossbow, velocity_rapidCrossbow)
                            .withAttribute(SpellSchools.ARCANE.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.RAPID_CROSSBOW)
                    .spell(dragon_breath);
            crossbow("elder_guardian_rapid_crossbow", durabilityBows, elderGuardianRepair,
                    new RangedConfig( rapid_crossbow_damage, pullTime_rapidCrossbow,velocity_rapidCrossbow)
                            .withAttribute(MoreSpellSchools.WATER.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.RAPID_CROSSBOW)
                    .spell(reef_arrows);
            crossbow("wither_rapid_crossbow", durabilityBows, witherRepair,
                    new RangedConfig(rapid_crossbow_damage, pullTime_rapidCrossbow, velocity_rapidCrossbow)
                            .withAttribute(SpellSchools.SOUL.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.RAPID_CROSSBOW)
                    .spell(cursed_wither_bolt);
            crossbow("glacial_rapid_crossbow", durabilityBows, frostMonarchRepair,
                    new RangedConfig(rapid_crossbow_damage, pullTime_rapidCrossbow, velocity_rapidCrossbow)
                            .withAttribute(SpellSchools.FROST.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.RAPID_CROSSBOW)
                    .spell(glacial_splitter);
            //HEAVY CROSSBOWS
            crossbow("ender_dragon_heavy_crossbow", durabilityBows, dragonRepair,
                    new RangedConfig( heavy_crossbow_damage, pullTime_heavyCrossbow,velocity_heavyCrossbow)
                            .withAttribute(SpellSchools.ARCANE.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.HEAVY_CROSSBOW)
                    .spell(dragon_breath);
            crossbow("elder_guardian_heavy_crossbow", durabilityBows, elderGuardianRepair,
                    new RangedConfig( heavy_crossbow_damage, pullTime_heavyCrossbow,velocity_heavyCrossbow)
                            .withAttribute(MoreSpellSchools.WATER.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.HEAVY_CROSSBOW)
                    .spell(reef_arrows);
            crossbow("wither_heavy_crossbow", durabilityBows, witherRepair,
                    new RangedConfig( heavy_crossbow_damage, pullTime_heavyCrossbow,velocity_heavyCrossbow)
                            .withAttribute(SpellSchools.SOUL.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.HEAVY_CROSSBOW)
                    .spell(cursed_wither_bolt);
            crossbow("glacial_heavy_crossbow", durabilityBows, frostMonarchRepair,
                    new RangedConfig( heavy_crossbow_damage, pullTime_heavyCrossbow,velocity_heavyCrossbow)
                            .withAttribute(SpellSchools.FROST.id, EntityAttributeModifier.Operation.ADD_VALUE, weaponSpellPower))
                    .weaponType(Equipment.WeaponType.HEAVY_CROSSBOW)
                    .spell(glacial_splitter);
        }

        Weapon.register(meleeConfig, meleeEntries, Group.RPG_LOOT_KEY);
        for (var entry: rangedEntries) {
            var config = rangedConfig.get(entry.id.toString());
            if (config == null) {
                config = entry.defaults;
                rangedConfig.put(entry.id.toString(), config);
            }
            var settings = new Item.Settings();
            var tier = entry.lootProperties.tier();
            settings.rarity(Rarity.RARE);
            settings.fireproof();
            if (entry.spells != null) {
                if (entry.spells.isEmpty()) {
                    settings.component(SpellDataComponents.SPELL_CONTAINER, SpellContainerHelper.createForRangedWeapon());
                } else {
                    settings.component(SpellDataComponents.SPELL_CONTAINER, SpellContainerHelper.createForRangedWeapon(entry.spells));
                }
            }
            var item = entry.create(settings, config);
            Registry.register(Registries.ITEM, entry.id, item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register((content) -> {
            for (var entry: rangedEntries) {
                content.add(entry.item);
            }
        });
    }
}
