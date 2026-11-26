package com.lne_archers.datagen;

import com.mrpg_lib.compat.spell_engine.loot.SpecificSpellScrollPoolLootFunction;
import com.mrpg_lib.util.loot.ConditionalItemEntry;
import com.mrpg_lib.util.loot.ConditionalItemLootFunction;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public abstract class ModLootTableProvider extends SimpleFabricLootTableProvider {
    private BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter;

    protected ModLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, LootContextType type) {
        super(output, registryLookup, type);
    }

    protected abstract void generate();

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        this.exporter = exporter;
        generate();
    }

    protected void table(String path, LootPool.Builder... pools) {
        var table = LootTable.builder();
        for (var pool : pools) table.pool(pool);
        exporter.accept(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, path)), table);
    }

    protected static LootPool.Builder pool(LootNumberProvider rolls, LootPoolEntry.Builder<?>... entries) {
        var pool = LootPool.builder().rolls(rolls);
        for (var entry : entries) pool.with(entry);
        return pool;
    }

    protected static LootNumberProvider constant(float value) {
        return ConstantLootNumberProvider.create(value);
    }

    protected static LootNumberProvider uniform(float min, float max) {
        return UniformLootNumberProvider.create(min, max);
    }

    protected static LeafEntry.Builder<?> item(String id, int weight, LootFunction.Builder... functions) {
        Item item = Registries.ITEM.get(Identifier.of(id));
        if (item == Items.AIR) throw new IllegalStateException("Loot table item is not registered: " + id);
        var entry = ItemEntry.builder(item).weight(weight);
        for (var function : functions) entry.apply(function);
        return entry;
    }

    protected static LeafEntry.Builder<?> conditional(String id, int weight, LootFunction.Builder... functions) {
        var entry = ConditionalItemEntry.builder(Identifier.of(id), Optional.empty()).weight(weight);
        for (var function : functions) entry.apply(function);
        return entry;
    }

    protected static LeafEntry.Builder<?> empty(int weight) {
        return EmptyEntry.builder().weight(weight);
    }

    protected static LeafEntry.Builder<?> nested(String id, int weight) {
        return LootTableEntry.builder(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(id))).weight(weight);
    }

    protected static ConditionalLootFunction.Builder<?> count(LootNumberProvider count) {
        return SetCountLootFunction.builder(count);
    }

    protected static EnchantWithLevelsLootFunction.Builder enchant(float min, float max) {
        return new EnchantWithLevelsLootFunction.Builder(uniform(min, max));
    }

    protected static LootFunction.Builder scroll(int tierMin, int tierMax, int count, String... pools) {
        return SpecificSpellScrollPoolLootFunction.builder(List.of(pools), constant(tierMin), constant(tierMax), constant(count), List.of());
    }

    protected static LootFunction.Builder conditionalItem(String id) {
        return ConditionalItemLootFunction.builder(Identifier.of(id));
    }

    protected static LootCondition.Builder chance(float chance) {
        return RandomChanceLootCondition.builder(chance);
    }

    protected static LootCondition.Builder killedByPlayer() {
        return KilledByPlayerLootCondition.builder();
    }

    protected static LootCondition.Builder survivesExplosion() {
        return SurvivesExplosionLootCondition.builder();
    }

    public static class Chests extends ModLootTableProvider {
        public Chests(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.CHEST);
        }

        @Override
        protected void generate() {
            chestsArcherTier1();
            chestsArcherTier2();
            chestsArcherTier3();
            chestsArcherTier4();
            chestsArcherTier5();
            chestsDeadeyeTier1();
            chestsDeadeyeTier2();
            chestsDeadeyeTier3();
            chestsDeadeyeTier4();
            chestsDeadeyeTier5();
            chestsGeneral0();
            chestsGeneral1();
            chestsTundraHunterTier1();
            chestsTundraHunterTier2();
            chestsTundraHunterTier3();
            chestsTundraHunterTier4();
            chestsTundraHunterTier5();
            chestsWarArcherTier1();
            chestsWarArcherTier2();
            chestsWarArcherTier3();
            chestsWarArcherTier4();
            chestsWarArcherTier5();
        }

        private void chestsArcherTier1() {
            table("chests/archer/tier1",
                pool(uniform(1, 2),
                        item("minecraft:stick", 5, count(uniform(2, 4))),
                        item("minecraft:leather", 5, count(uniform(4, 8))),
                        item("minecraft:string", 5, count(uniform(2, 6))),
                        item("minecraft:flint", 4, count(uniform(1, 5))),
                        item("minecraft:redstone", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:iron_ingot", 2, count(uniform(1, 2))),
                        item("minecraft:rabbit_hide", 2, count(uniform(1, 2))),
                        item("archers:flint_spear", 3),
                        item("minecraft:crossbow", 3),
                        item("minecraft:bow", 3),
                        item("archers:composite_longbow", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers:spell_scroll/archer"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)));
        }

        private void chestsArcherTier2() {
            table("chests/archer/tier2",
                pool(uniform(1, 3),
                        item("minecraft:leather", 3, count(uniform(4, 8))),
                        item("minecraft:iron_ingot", 3, count(uniform(2, 5))),
                        item("minecraft:chain", 3, count(uniform(2, 5))),
                        item("minecraft:string", 3, count(uniform(1, 2))),
                        item("minecraft:rabbit_hide", 3, count(uniform(1, 3))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(4, 6))),
                        item("minecraft:arrow", 3, count(uniform(1, 10))),
                        item("archers:iron_spear", 2),
                        item("archers:composite_longbow", 2),
                        item("archers:mechanic_shortbow", 2),
                        item("archers:rapid_crossbow", 2),
                        item("archers:archer_armor_head", 1),
                        item("archers:archer_armor_chest", 1),
                        item("archers:archer_armor_legs", 1),
                        item("archers:archer_armor_feet", 1),
                        item("archers:small_quiver", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers:spell_scroll/archer"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)));
        }

        private void chestsArcherTier3() {
            table("chests/archer/tier3",
                pool(uniform(2, 3),
                        item("minecraft:string", 4, count(uniform(1, 4))),
                        item("minecraft:arrow", 4, count(uniform(6, 16))),
                        item("minecraft:turtle_scute", 4, count(uniform(1, 4))),
                        item("minecraft:rabbit_hide", 4, count(uniform(2, 5))),
                        item("archers:medium_quiver", 3),
                        item("archers:rapid_crossbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:mechanic_shortbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:iron_spear", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:diamond_spear", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:royal_longbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:heavy_crossbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_feet", 1, enchant(1, 13).conditionally(chance(0.35F)))).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#archers:spell_scroll/archer"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:lesser_melee_crit_chance", 1),
                        conditional("relics_rpgs:lesser_roll", 1),
                        conditional("relics_rpgs:lesser_use_dex", 1),
                        conditional("relics_rpgs:lesser_use_ranged", 1),
                        conditional("loot_n_explore:waldmeister", 1)));
        }

        private void chestsArcherTier4() {
            table("chests/archer/tier4",
                pool(uniform(2, 4),
                        item("minecraft:string", 3, count(uniform(3, 5))),
                        item("minecraft:turtle_scute", 3, count(uniform(2, 4))),
                        item("minecraft:rabbit_hide", 3, count(uniform(4, 6))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("archers:diamond_spear", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:royal_longbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:heavy_crossbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:medium_quiver", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#archers:spell_scroll/archer"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:medium_proc_ranged_damage", 1),
                        conditional("relics_rpgs:medium_proc_attack_speed", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)));
        }

        private void chestsArcherTier5() {
            table("chests/archer/tier5",
                pool(uniform(2, 4),
                        item("minecraft:turtle_scute", 3, count(uniform(1, 4))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:golden_apple", 1),
                        item("archers:diamond_spear", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:royal_longbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:heavy_crossbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:large_quiver", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#archers:spell_scroll/archer"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:greater_perk_roll_damage", 1),
                        conditional("relics_rpgs:greater_perk_ranged_levitate", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)));
        }

        private void chestsDeadeyeTier1() {
            table("chests/deadeye/tier1",
                pool(uniform(1, 2),
                        item("minecraft:stick", 5, count(uniform(2, 4))),
                        item("minecraft:leather", 5, count(uniform(4, 8))),
                        item("minecraft:string", 5, count(uniform(2, 6))),
                        item("minecraft:flint", 4, count(uniform(1, 5))),
                        item("minecraft:redstone", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:gold_ingot", 2, count(uniform(1, 2))),
                        item("minecraft:rabbit_hide", 2, count(uniform(1, 2))),
                        item("archers:flint_spear", 3, conditionalItem("rogues:flint_dagger")),
                        item("minecraft:crossbow", 3),
                        item("minecraft:bow", 3),
                        item("archers:composite_longbow", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers_expansion:spell_scroll/deadeye"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)),
                pool(uniform(1, 1),
                        empty(15),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#rogues:spell_scroll/rogue"))));
        }

        private void chestsDeadeyeTier2() {
            table("chests/deadeye/tier2",
                pool(uniform(1, 3),
                        item("minecraft:leather", 3, count(uniform(4, 8))),
                        item("minecraft:gold_ingot", 3, count(uniform(2, 5))),
                        item("minecraft:spider_eye", 3, count(uniform(2, 5))),
                        item("minecraft:string", 3, count(uniform(1, 2))),
                        item("minecraft:rabbit_hide", 3, count(uniform(1, 3))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(4, 6))),
                        item("minecraft:arrow", 3, count(uniform(1, 10))),
                        item("archers:iron_spear", 2, conditionalItem("rogues:iron_dagger")),
                        item("archers:composite_longbow", 2),
                        item("archers:mechanic_shortbow", 2),
                        item("archers:rapid_crossbow", 2),
                        item("archers:archer_armor_head", 1),
                        item("archers:archer_armor_chest", 1),
                        item("archers:archer_armor_legs", 1),
                        item("archers:archer_armor_feet", 1),
                        item("archers:small_quiver", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers_expansion:spell_scroll/deadeye"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)),
                pool(uniform(1, 1),
                        empty(10),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#rogues:spell_scroll/rogue"))));
        }

        private void chestsDeadeyeTier3() {
            table("chests/deadeye/tier3",
                pool(uniform(2, 3),
                        item("minecraft:string", 4, count(uniform(1, 4))),
                        item("minecraft:arrow", 4, count(uniform(6, 16))),
                        item("mrpg_lib:hardened_leather", 4, count(uniform(1, 4))),
                        item("minecraft:rabbit_hide", 4, count(uniform(2, 5))),
                        item("archers:medium_quiver", 3),
                        item("archers:rapid_crossbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:mechanic_shortbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:iron_spear", 3, conditionalItem("rogues:iron_dagger"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:diamond_spear", 1, conditionalItem("rogues:diamond_dagger"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:royal_longbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:heavy_crossbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:deadeye_head"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:deadeye_chest"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:deadeye_legs"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:deadeye_feet"), enchant(1, 13).conditionally(chance(0.35F)))).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#archers_expansion:spell_scroll/deadeye"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:lesser_melee_crit_chance", 1),
                        conditional("relics_rpgs:lesser_roll", 1),
                        conditional("relics_rpgs:lesser_use_dex", 1),
                        conditional("relics_rpgs:lesser_use_ranged", 1),
                        conditional("loot_n_explore:waldmeister", 1)),
                pool(uniform(1, 2),
                        empty(8),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#rogues:spell_scroll/rogue")),
                        conditional("rogues:diamond_sickle", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:assassin_armor_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:assassin_armor_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:assassin_armor_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:assassin_armor_feet", 1, enchant(1, 13).conditionally(chance(0.35F)))));
        }

        private void chestsDeadeyeTier4() {
            table("chests/deadeye/tier4",
                pool(uniform(2, 4),
                        item("minecraft:string", 3, count(uniform(3, 5))),
                        item("mrpg_lib:hardened_leather", 3, count(uniform(2, 4))),
                        item("minecraft:rabbit_hide", 3, count(uniform(4, 6))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("archers:diamond_spear", 1, conditionalItem("rogues:diamond_dagger"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:royal_longbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:heavy_crossbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:deadeye_head"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:deadeye_chest"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:deadeye_legs"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:deadeye_feet"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:medium_quiver", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#archers_expansion:spell_scroll/deadeye"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:medium_proc_ranged_damage", 1),
                        conditional("relics_rpgs:medium_proc_attack_speed", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)),
                pool(uniform(1, 2),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#rogues:spell_scroll/rogue")),
                        conditional("rogues:diamond_sickle", 2, enchant(1, 13).conditionally(chance(0.5F))),
                        conditional("rogues:assassin_armor_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("rogues:assassin_armor_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("rogues:assassin_armor_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("rogues:assassin_armor_feet", 1, enchant(6, 18).conditionally(chance(0.5F)))));
        }

        private void chestsDeadeyeTier5() {
            table("chests/deadeye/tier5",
                pool(uniform(2, 4),
                        item("minecraft:rabbit_hide", 3, count(uniform(1, 4))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:golden_apple", 1),
                        item("archers:diamond_spear", 1, conditionalItem("rogues:diamond_dagger"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:royal_longbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:heavy_crossbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:deadeye_head"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:deadeye_chest"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:deadeye_legs"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:deadeye_feet"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:large_quiver", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#archers_expansion:spell_scroll/deadeye"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:greater_perk_roll_damage", 1),
                        conditional("relics_rpgs:greater_perk_ranged_levitate", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)),
                pool(uniform(1, 2),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#rogues:spell_scroll/rogue")),
                        conditional("rogues:diamond_sickle", 2, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:assassin_armor_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:assassin_armor_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:assassin_armor_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:assassin_armor_feet", 1, enchant(13, 24).conditionally(chance(0.75F)))));
        }

        private void chestsGeneral0() {
            table("chests/general_0",
                pool(uniform(2, 4),
                        item("minecraft:flint", 1, count(uniform(1, 5))),
                        item("minecraft:bone", 1, count(uniform(1, 4))),
                        item("minecraft:stick", 1, count(uniform(1, 4))),
                        item("minecraft:leather", 1, count(uniform(1, 4))),
                        item("minecraft:string", 1, count(uniform(1, 4))),
                        item("minecraft:chain", 1, count(uniform(1, 3)))));
        }

        private void chestsGeneral1() {
            table("chests/general_1",
                pool(uniform(2, 4),
                        item("minecraft:bread", 1, count(uniform(1, 4))),
                        item("minecraft:wheat", 1, count(uniform(3, 7))),
                        item("minecraft:cooked_rabbit", 1, count(uniform(1, 2))),
                        item("minecraft:stick", 1, count(uniform(2, 5))),
                        item("minecraft:feather", 1, count(uniform(2, 5))),
                        item("minecraft:apple", 1, count(uniform(1, 3))),
                        item("minecraft:lapis_lazuli", 1, count(uniform(1, 2))),
                        item("minecraft:arrow", 1, count(uniform(1, 10)))));
        }

        private void chestsTundraHunterTier1() {
            table("chests/tundra_hunter/tier1",
                pool(uniform(1, 2),
                        item("minecraft:stick", 5, count(uniform(2, 4))),
                        item("minecraft:leather", 5, count(uniform(4, 8))),
                        item("minecraft:string", 5, count(uniform(2, 6))),
                        item("minecraft:flint", 4, count(uniform(1, 5))),
                        item("minecraft:redstone", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("mrpg_lib:polar_bear_fur", 2, count(uniform(1, 2))),
                        item("archers:flint_spear", 3, conditionalItem("berserker_rpg:flint_berserker_axe")),
                        item("minecraft:crossbow", 3),
                        item("minecraft:bow", 3),
                        item("archers:composite_longbow", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers_expansion:spell_scroll/tundra_hunter"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)),
                pool(uniform(1, 1),
                        empty(10),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#berserker_rpg:spell_scroll/berserker"))));
        }

        private void chestsTundraHunterTier2() {
            table("chests/tundra_hunter/tier2",
                pool(uniform(1, 3),
                        item("minecraft:leather", 3, count(uniform(4, 8))),
                        item("minecraft:prismarine_shard", 3, count(uniform(2, 5))),
                        item("minecraft:spider_eye", 3, count(uniform(2, 5))),
                        item("minecraft:string", 3, count(uniform(1, 2))),
                        item("mrpg_lib:polar_bear_fur", 3, count(uniform(1, 3))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(4, 6))),
                        item("minecraft:arrow", 3, count(uniform(1, 10))),
                        item("archers:iron_spear", 2, conditionalItem("berserker_rpg:iron_berserker_rpg")),
                        item("archers:composite_longbow", 2),
                        item("archers:mechanic_shortbow", 2),
                        item("archers:rapid_crossbow", 2),
                        item("archers:archer_armor_head", 1),
                        item("archers:archer_armor_chest", 1),
                        item("archers:archer_armor_legs", 1),
                        item("archers:archer_armor_feet", 1),
                        item("archers:small_quiver", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers_expansion:spell_scroll/tundra_hunter"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)),
                pool(uniform(1, 1),
                        empty(10),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#berserker_rpg:spell_scroll/berserker"))));
        }

        private void chestsTundraHunterTier3() {
            table("chests/tundra_hunter/tier3",
                pool(uniform(2, 3),
                        item("minecraft:string", 4, count(uniform(1, 4))),
                        item("minecraft:arrow", 4, count(uniform(6, 16))),
                        item("mrpg_lib:polar_bear_fur", 4, count(uniform(1, 4))),
                        item("minecraft:prismarine_shard", 4, count(uniform(2, 5))),
                        item("archers:medium_quiver", 3),
                        item("archers:rapid_crossbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:mechanic_shortbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:iron_spear", 3, conditionalItem("berserker_rpg:iron_berserker_axe"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:diamond_spear", 1, conditionalItem("berserker_rpg:diamond_berserker_axe"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:royal_longbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:heavy_crossbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:tundra_hunter_head"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:tundra_hunter_chest"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:tundra_hunter_legs"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:tundra_hunter_feet"), enchant(1, 13).conditionally(chance(0.35F)))).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#archers_expansion:spell_scroll/tundra_hunter"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:lesser_melee_crit_chance", 1),
                        conditional("relics_rpgs:lesser_roll", 1),
                        conditional("relics_rpgs:lesser_use_dex", 1),
                        conditional("relics_rpgs:lesser_use_ranged", 1),
                        conditional("loot_n_explore:waldmeister", 1)),
                pool(uniform(1, 2),
                        empty(8),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#berserker_rpg:spell_scroll/berserker")),
                        conditional("berserker_rpg:diamond_berserker_axe", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("berserker_rpg:northling_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("berserker_rpg:northling_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("berserker_rpg:northling_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("berserker_rpg:northling_feet", 1, enchant(1, 13).conditionally(chance(0.35F)))));
        }

        private void chestsTundraHunterTier4() {
            table("chests/tundra_hunter/tier4",
                pool(uniform(2, 4),
                        item("minecraft:string", 3, count(uniform(3, 5))),
                        item("mrpg_lib:polar_bear_fur", 3, count(uniform(2, 4))),
                        item("minecraft:prismarine_shard", 3, count(uniform(4, 6))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("archers:diamond_spear", 1, conditionalItem("berserker_rpg:diamond_berserker_axe"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:royal_longbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:heavy_crossbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:tundra_hunter_head"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:tundra_hunter_chest"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:tundra_hunter_legs"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:tundra_hunter_feet"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:medium_quiver", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#archers_expansion:spell_scroll/tundra_hunter"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:medium_proc_ranged_damage", 1),
                        conditional("relics_rpgs:medium_proc_attack_speed", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)),
                pool(uniform(1, 2),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#berserker_rpg:spell_scroll/berserker")),
                        conditional("berserker_rpg:diamond_berserker_axe", 2, enchant(1, 13).conditionally(chance(0.5F))),
                        conditional("berserker_rpg:northling_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("berserker_rpg:northling_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("berserker_rpg:northling_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("berserker_rpg:northling_feet", 1, enchant(6, 18).conditionally(chance(0.5F)))));
        }

        private void chestsTundraHunterTier5() {
            table("chests/tundra_hunter/tier5",
                pool(uniform(2, 4),
                        item("minecraft:prismarine_shard", 3, count(uniform(1, 4))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:golden_apple", 1),
                        item("archers:diamond_spear", 1, conditionalItem("berserker_rpg:diamond_berserker_axe"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:royal_longbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:heavy_crossbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:tundra_hunter_head"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:tundra_hunter_chest"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:tundra_hunter_legs"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:tundra_hunter_feet"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:large_quiver", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#archers_expansion:spell_scroll/tundra_hunter"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:greater_perk_roll_damage", 1),
                        conditional("relics_rpgs:greater_perk_ranged_levitate", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)),
                pool(uniform(1, 2),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#berserker_rpg:spell_scroll/berserker")),
                        conditional("berserker_rpg:diamond_berserker_axe", 2, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("berserker_rpg:northling_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("berserker_rpg:northling_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("berserker_rpg:northling_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("berserker_rpg:northling_feet", 1, enchant(13, 24).conditionally(chance(0.75F)))));
        }

        private void chestsWarArcherTier1() {
            table("chests/war_archer/tier1",
                pool(uniform(1, 2),
                        item("minecraft:stick", 5, count(uniform(2, 4))),
                        item("minecraft:leather", 5, count(uniform(4, 8))),
                        item("minecraft:string", 5, count(uniform(2, 6))),
                        item("minecraft:flint", 4, count(uniform(1, 5))),
                        item("minecraft:redstone", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:iron_ingot", 2, count(uniform(1, 2))),
                        item("minecraft:chain", 2, count(uniform(1, 2))),
                        item("archers:flint_spear", 3, conditionalItem("rogues:stone_double_axe")),
                        item("minecraft:crossbow", 3),
                        item("minecraft:bow", 3),
                        item("archers:composite_longbow", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers_expansion:spell_scroll/war_archer"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)),
                pool(uniform(1, 1),
                        empty(10),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#rogues:spell_scroll/warrior"))));
        }

        private void chestsWarArcherTier2() {
            table("chests/war_archer/tier2",
                pool(uniform(1, 3),
                        item("minecraft:leather", 3, count(uniform(4, 8))),
                        item("minecraft:iron_ingot", 3, count(uniform(2, 5))),
                        item("minecraft:chain", 3, count(uniform(2, 5))),
                        item("minecraft:string", 3, count(uniform(1, 2))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(4, 6))),
                        item("minecraft:arrow", 3, count(uniform(1, 10))),
                        item("archers:iron_spear", 2, conditionalItem("rogues:iron_double_axe")),
                        item("archers:composite_longbow", 2),
                        item("archers:mechanic_shortbow", 2),
                        item("archers:rapid_crossbow", 2),
                        item("archers:archer_armor_head", 1),
                        item("archers:archer_armor_chest", 1),
                        item("archers:archer_armor_legs", 1),
                        item("archers:archer_armor_feet", 1),
                        item("archers:small_quiver", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#archers_expansion:spell_scroll/war_archer"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_jade", 1),
                        conditional("loot_n_explore:apple_juice", 1)),
                pool(uniform(1, 1),
                        empty(10),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#rogues:spell_scroll/warrior"))));
        }

        private void chestsWarArcherTier3() {
            table("chests/war_archer/tier3",
                pool(uniform(2, 3),
                        item("minecraft:string", 4, count(uniform(1, 4))),
                        item("minecraft:arrow", 4, count(uniform(6, 16))),
                        item("minecraft:iron_ingot", 4, count(uniform(3, 7))),
                        item("minecraft:netherite_scrap", 4, count(uniform(1, 1))),
                        item("archers:medium_quiver", 3),
                        item("archers:rapid_crossbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:mechanic_shortbow", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:iron_spear", 3, conditionalItem("rogues:iron_double_axe"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:archer_armor_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:diamond_spear", 1, conditionalItem("rogues:diamond_double_axe"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:royal_longbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:heavy_crossbow", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:war_archer_head"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:war_archer_chest"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:war_archer_legs"), enchant(1, 13).conditionally(chance(0.35F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:war_archer_feet"), enchant(1, 13).conditionally(chance(0.35F)))).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#archers_expansion:spell_scroll/war_archer"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:diamond_ring", 1),
                        conditional("jewelry:diamond_necklace", 1),
                        conditional("jewelry:jade_ring", 1),
                        conditional("jewelry:jade_necklace", 1),
                        conditional("relics_rpgs:lesser_melee_crit_chance", 1),
                        conditional("relics_rpgs:lesser_roll", 1),
                        conditional("relics_rpgs:lesser_use_dex", 1),
                        conditional("relics_rpgs:lesser_use_ranged", 1),
                        conditional("loot_n_explore:waldmeister", 1)),
                pool(uniform(1, 2),
                        empty(8),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#rogues:spell_scroll/warrior")),
                        conditional("rogues:diamond_glaive", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:berserker_armor_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:berserker_armor_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:berserker_armor_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("rogues:berserker_armor_feet", 1, enchant(1, 13).conditionally(chance(0.35F)))));
        }

        private void chestsWarArcherTier4() {
            table("chests/war_archer/tier4",
                pool(uniform(2, 4),
                        item("minecraft:string", 3, count(uniform(3, 5))),
                        item("minecraft:iron_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:netherite_scrap", 3, count(uniform(1, 1))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("archers:diamond_spear", 1, conditionalItem("rogues:diamond_double_axe"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:royal_longbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:heavy_crossbow", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:war_archer_head"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:war_archer_chest"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:war_archer_legs"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:war_archer_feet"), enchant(6, 18).conditionally(chance(0.5F))),
                        item("archers:medium_quiver", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#archers_expansion:spell_scroll/war_archer"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:medium_proc_ranged_damage", 1),
                        conditional("relics_rpgs:medium_proc_attack_speed", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)),
                pool(uniform(1, 2),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#rogues:spell_scroll/warrior")),
                        conditional("rogues:diamond_glaive", 2, enchant(1, 13).conditionally(chance(0.5F))),
                        conditional("rogues:berserker_armor_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("rogues:berserker_armor_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("rogues:berserker_armor_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("rogues:berserker_armor_feet", 1, enchant(6, 18).conditionally(chance(0.5F)))));
        }

        private void chestsWarArcherTier5() {
            table("chests/war_archer/tier5",
                pool(uniform(2, 4),
                        item("minecraft:netherite_scrap", 3, count(uniform(1, 2))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:golden_apple", 1),
                        item("archers:diamond_spear", 1, conditionalItem("rogues:diamond_double_axe"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:royal_longbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:heavy_crossbow", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_head", 1, conditionalItem("archers_expansion:war_archer_head"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_chest", 1, conditionalItem("archers_expansion:war_archer_chest"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_legs", 1, conditionalItem("archers_expansion:war_archer_legs"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:ranger_armor_feet", 1, conditionalItem("archers_expansion:war_archer_feet"), enchant(13, 24).conditionally(chance(0.75F))),
                        item("archers:large_quiver", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#archers_expansion:spell_scroll/war_archer"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_jade_ring", 1),
                        conditional("jewelry:netherite_jade_necklace", 1),
                        conditional("relics_rpgs:greater_perk_roll_damage", 1),
                        conditional("relics_rpgs:greater_perk_ranged_levitate", 1),
                        conditional("loot_n_explore:forrest_spirit", 1)),
                pool(uniform(1, 2),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#rogues:spell_scroll/warrior")),
                        conditional("rogues:diamond_glaive", 2, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:berserker_armor_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:berserker_armor_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:berserker_armor_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("rogues:berserker_armor_feet", 1, enchant(13, 24).conditionally(chance(0.75F)))));
        }
    }
}
