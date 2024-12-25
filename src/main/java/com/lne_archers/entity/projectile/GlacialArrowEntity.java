package com.lne_archers.entity.projectile;

import more_rpg_loot.effects.Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

public class GlacialArrowEntity extends ArrowEntity {
    public GlacialArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        applyStatusEffect(target,0,4, Effects.FREEZING,1,
                true,true,false,0);
    }
}
