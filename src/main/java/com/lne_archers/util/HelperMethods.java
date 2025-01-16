package com.lne_archers.util;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import java.util.Iterator;
import java.util.List;

public class HelperMethods {
    public static void spawnCloudEntity(
            ParticleEffect particleType, Entity owner, Entity target, int waitTime,float radiusCloud, int durationSecondsCloud, float radiusGrowthCloud
            , StatusEffect statusEffect, int durationSecondsStatusEffect, int amplifierStatusEffect) {
        if (!target.getWorld().isClient) {
            List<LivingEntity> list = target.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(4.0, 2.0, 4.0));
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(target.getWorld(), target.getX(), target.getY(), target.getZ());
            if (owner instanceof LivingEntity) {
                areaEffectCloudEntity.setOwner((LivingEntity) owner);
            } else if (owner instanceof ProjectileEntity projectile) {
                Entity projectileOwner = projectile.getOwner();
                areaEffectCloudEntity.setOwner((LivingEntity) projectileOwner);
            }
            areaEffectCloudEntity.setParticleType(particleType);
            areaEffectCloudEntity.setRadius(radiusCloud);
            areaEffectCloudEntity.setDuration(durationSecondsCloud * 20);
            areaEffectCloudEntity.setWaitTime(waitTime);
            areaEffectCloudEntity.setRadiusGrowth((radiusGrowthCloud - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
            if(areaEffectCloudEntity != owner){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffect,
                        durationSecondsStatusEffect * 20, amplifierStatusEffect, false, false, true));
            }
            if (!list.isEmpty()) {
                Iterator var5 = list.iterator();
                while (var5.hasNext()) {
                    LivingEntity livingEntity2 = (LivingEntity) var5.next();
                    double x = owner.squaredDistanceTo(livingEntity2);
                    if (x < 16.0) {
                        areaEffectCloudEntity.setPosition(livingEntity2.getX(), livingEntity2.getY(), livingEntity2.getZ());
                        break;
                    }
                }
            }
            owner.getWorld().spawnEntity(areaEffectCloudEntity);
        }
    }
}
