package com.lne_archers.item.weapons;

import more_rpg_loot.item.weapons.ElderGuardianMeleeWeapon;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class ElderGuardianSpear extends ElderGuardianMeleeWeapon {
    public ElderGuardianSpear(ToolMaterial material, Settings settings) {
        this(material, 1,2.4F, settings);
    }

    public ElderGuardianSpear(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return 1.0f;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return false;
    }
}
