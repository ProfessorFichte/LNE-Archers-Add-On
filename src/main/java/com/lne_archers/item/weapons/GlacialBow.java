package com.lne_archers.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class GlacialBow extends CustomBow {
    public GlacialBow(Item.Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
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
