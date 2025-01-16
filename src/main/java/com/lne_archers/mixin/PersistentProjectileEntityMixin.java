package com.lne_archers.mixin;

import com.lne_archers.item.weapons.*;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.util.HelperMethods;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {

    @Shadow private double damage;
    private static final ParticleBatch particlesDragon = new ParticleBatch(
            "spell_engine:arcane_hit",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            25,
            0.3F,
            0.7F,
            0);

    private static final ParticleBatch particlesGlacial = new ParticleBatch(
            "spell_engine:frost_hit",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            20,
            0.3F,
            0.5F,
            0);

    private static final ParticleBatch particlesWither = new ParticleBatch(
            "smoke",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            20,
            0.1F,
            0.3F,
            0);
    private static final ParticleBatch particlesElderG = new ParticleBatch(
            "more_rpg_classes:big_splash",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            25,
            0.3F,
            0.5F,
            0);


    @Inject(method = "onEntityHit", at = @At(value = "TAIL", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
    private void onEntityHit$LNEArchers(EntityHitResult entityHitResult, CallbackInfo ci) {
        ProjectileEntity projectileEntity = (ProjectileEntity)(Object)this;
        var entity = entityHitResult.getEntity();
        Entity entity2 = projectileEntity.getOwner();


        if(entity2  instanceof PlayerEntity player && entity instanceof LivingEntity livingEntity){
            World world = livingEntity.getWorld();
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            float ranged_damage = (float) Objects.requireNonNull(player).getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.attribute);
            float projectileDamage = (float) this.damage + ranged_damage;

            if(item instanceof DragonBow || item instanceof DragonCrossbow){
                com.lne_archers.util.HelperMethods.spawnCloudEntity(ParticleTypes.DRAGON_BREATH,player,livingEntity,5,
                        1.0F,3,2.0F,StatusEffects.INSTANT_DAMAGE,1,1);

                if(!world.isClient()){
                    ParticleHelper.sendBatches(livingEntity, new ParticleBatch[]{particlesDragon});
                }
            }

            if(item instanceof GlacialBow || item instanceof GlacialCrossbow){
                applyStatusEffect(livingEntity,0,6, Effects.FREEZING,1,
                        true,true,false,0);
                HelperMethods.stackFreezeStacks(livingEntity,20);
                if(!world.isClient()) {
                    ParticleHelper.sendBatches(livingEntity, new ParticleBatch[]{particlesGlacial});
                }
            }

            if( item instanceof ElderGuardianBow || item instanceof ElderGuardianCrossbow){
                if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
                    if(!world.isClient()) {
                        ParticleHelper.sendBatches(livingEntity, new ParticleBatch[]{particlesElderG});
                    }
                    float amplifier_multiplier = 0.15F;
                    int effect_duration = 120;
                    int amplifier = (int) (ranged_damage * amplifier_multiplier);
                    livingEntity.addStatusEffect(new StatusEffectInstance(MRPGCEffects.BLEEDING, effect_duration, amplifier,
                            false, false, true));
                }else{
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 6, 0,
                            false, false, true));
                }
            }

            if(item instanceof WitherBow || item instanceof WitherCrossbow){
                List<StatusEffectInstance> list = livingEntity.getStatusEffects().stream().toList();
                int negativeEffectsAmount = 0;
                if (!list.isEmpty()) {
                    for (StatusEffectInstance statusEffectInstance : list) {
                        StatusEffect statusEffect = statusEffectInstance.getEffectType();
                        if (!statusEffect.isBeneficial()) {
                            negativeEffectsAmount++;
                        }
                    }
                }
                applyStatusEffect(livingEntity,negativeEffectsAmount,5, StatusEffects.WITHER,0,
                        false,true,false,0);


                if(!world.isClient()) {
                    ParticleHelper.sendBatches(livingEntity, new ParticleBatch[]{particlesWither});
                }

            }


        }
    }

}
