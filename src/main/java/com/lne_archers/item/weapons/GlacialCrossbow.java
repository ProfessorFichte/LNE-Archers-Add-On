package com.lne_archers.item.weapons;

import com.google.common.collect.Lists;
import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class GlacialCrossbow extends CustomCrossbow {
    public GlacialCrossbow(Item.Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
    }

    private static List<ItemStack> getProjectiles(ItemStack crossbow) {
        List<ItemStack> list = Lists.newArrayList();
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null && nbtCompound.contains("ChargedProjectiles", 9)) {
            NbtList nbtList = nbtCompound.getList("ChargedProjectiles", 10);
            if (nbtList != null) {
                for(int i = 0; i < nbtList.size(); ++i) {
                    NbtCompound nbtCompound2 = nbtList.getCompound(i);
                    list.add(ItemStack.fromNbt(nbtCompound2));
                }
            }
        }

        return list;
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        List<ItemStack> list = getProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemStack = (ItemStack)list.get(0);
            tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(ScreenTexts.SPACE).append(itemStack.toHoverableText()));
            if (context.isAdvanced() && itemStack.isOf(Items.FIREWORK_ROCKET)) {
                List<Text> list2 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendTooltip(itemStack, world, list2, context);
                if (!list2.isEmpty()) {
                    for(int i = 0; i < list2.size(); ++i) {
                        list2.set(i, Text.literal("  ").append((Text)list2.get(i)).formatted(Formatting.GRAY));
                    }

                    tooltip.addAll(list2);
                }
            }

        }
        tooltip.add(Text.translatable("lore.loot_n_explore.glacial_weapon").formatted(Formatting.GOLD));
        if(Screen.hasShiftDown()) {
        tooltip.add(Text.translatable("passive.lne_archers.glacial_ranged").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.lne_archers.glacial_ranged_1").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("passive.lne_archers.glacial_ranged_2").formatted(Formatting.WHITE));
        }else{
            tooltip.add(Text.translatable("tooltip.loot_n_explore.shift_down"));
        }
    }


}