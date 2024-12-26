package com.lne_archers.item.weapons;

import com.lne_archers.entity.projectile.EnderArrowEntity;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCasterEntity;

import java.util.List;
import java.util.function.Supplier;

import static net.spell_engine.internals.SpellRegistry.getSpell;

public class DragonBow extends CustomBow {
    public DragonBow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean bl = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            if (world.isClient) return;
            var ticks = stack.getMaxUseTime() - remainingUseTicks;
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (!itemStack.isEmpty() || bl ) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }

                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                float f = getPullProgress(i);
                if (!((double)f < 0.1)) {
                    boolean bl2 = bl && itemStack.isOf(Items.ARROW);
                    if (ticks < 80 || ticks > 100) {
                        ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                        EnderArrowEntity persistentProjectileEntity = new EnderArrowEntity(world, playerEntity);
                        persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            persistentProjectileEntity.setCritical(true);
                        }

                        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)j * 0.5 + 0.5);
                        }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            persistentProjectileEntity.setPunch(k);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            persistentProjectileEntity.setOnFireFor(100);
                        }

                        stack.damage(1, playerEntity, (p) -> {
                            p.sendToolBreakStatus(playerEntity.getActiveHand());
                        });
                        if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }

                        world.spawnEntity(persistentProjectileEntity);
                    } else if(ticks > 80 && ticks < 100) {
                        SpellCasterEntity caster = (SpellCasterEntity) user;
                        Identifier spellIdentifier = new Identifier("lne_archers","passive_dragon_bow");
                        var isCoolingDown = caster.getCooldownManager().isCoolingDown(spellIdentifier);
                        var spell = getSpell((spellIdentifier));
                        SpellInfo spellInfo = new SpellInfo(spell,spellIdentifier);
                        SpellHelper.ImpactContext context = null;
                        SpellHelper.shootProjectile(world,user,null,spellInfo,context);
                    }


                    world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            playerEntity.getInventory().removeOne(itemStack);
                        }
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        if (world.isClient) return;
        var ticks = stack.getMaxUseTime() - remainingUseTicks;
        if (ticks == 79) {
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.5f, 1.2f);
        }
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("lore.loot_n_explore.ender_dragon_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.lne_archers.ender_dragon_ranged").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.lne_archers.ender_dragon_ranged_1").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("passive.lne_archers.ender_dragon_ranged_2").formatted(Formatting.AQUA));

    }
}
