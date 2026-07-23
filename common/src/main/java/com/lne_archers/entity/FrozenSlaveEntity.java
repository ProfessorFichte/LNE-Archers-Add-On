package com.lne_archers.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.internals.target.EntityRelation;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import java.util.EnumSet;
import java.util.UUID;

public class FrozenSlaveEntity extends PathAwareEntity implements SpellEntity.Spawned {
    public static EntityType<FrozenSlaveEntity> ENTITY_TYPE;

    private LivingEntity owner;
    private UUID ownerUuid;
    private int timeToLive = -1;

    public FrozenSlaveEntity(EntityType<? extends FrozenSlaveEntity> type, World world) {
        super(type, world);
    }

    public FrozenSlaveEntity(EntityType<? extends FrozenSlaveEntity> type, World world, LivingEntity owner) {
        this(type, world);
        this.owner = owner;
        this.ownerUuid = owner.getUuid();
        scaleHealthToOwner(owner);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2);
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        this.owner = args.owner();
        this.ownerUuid = owner.getUuid();
        if (args.spawnData().time_to_live_seconds > 0) {
            this.timeToLive = (int)(args.spawnData().time_to_live_seconds * 20);
        }
        scaleHealthToOwner(owner);
    }

    private void scaleHealthToOwner(LivingEntity owner) {
        // TODO: Replace SpellSchools.FROST with the correct FROST_RANGE school (e.g. MoreSpellSchools.FROST_RANGE)
        double frostPower = SpellPower.getSpellPower(SpellSchools.FROST, owner).randomValue();
        double health = Math.max(10.0, Math.min(120.0, 20.0 + frostPower * 2.0));
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(health);
        setHealth((float) health);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(0, new SwimGoal(this));
        goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.add(2, new FollowOwnerGoal(this));
        goalSelector.add(3, new WanderAroundFarGoal(this, 0.7));
        goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        targetSelector.add(0, new DefendOwnerGoal(this));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new ActiveTargetGoal<>(this, HostileEntity.class, true));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        this.timeToLive = nbt.getInt("TimeToLive");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("TimeToLive", timeToLive);
    }

    public LivingEntity getOwner() {
        if (owner == null && ownerUuid != null && getWorld() instanceof ServerWorld sw) {
            Entity e = sw.getEntity(ownerUuid);
            if (e instanceof LivingEntity living) owner = living;
        }
        return owner;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            LivingEntity owner = getOwner();
            if (owner == null || !owner.isAlive()) {
                discard();
                return;
            }
            if (timeToLive > 0 && --timeToLive <= 0) {
                discard();
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        // TODO: Replace / augment with Spell Engine AOE Impact on hit:
        // RegistryEntry<Spell> spellEntry = SpellRegistry.from(getWorld()).getEntry(YOUR_SPELL_ID).get();
        // SpellHelper.performImpacts(getWorld(), this, (LivingEntity) target, target, spellEntry,
        //     spellEntry.value().impacts,
        //     new SpellHelper.ImpactContext().power(SpellPower.getSpellPower(spellEntry.value().school, this)).position(getPos()));
        return super.tryAttack(target);
    }

    @Override
    public boolean isGlowing() { return true; }

    @Override
    public boolean isPushable() { return false; }

    private class FollowOwnerGoal extends Goal {
        private final FrozenSlaveEntity slave;
        private LivingEntity targetOwner;
        private int timer;

        FollowOwnerGoal(FrozenSlaveEntity slave) {
            this.slave = slave;
            setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity owner = slave.getOwner();
            if (owner == null || (slave.getTarget() != null && slave.getTarget().isAlive())) return false;
            if (slave.squaredDistanceTo(owner) < 4.0) return false;
            targetOwner = owner;
            return true;
        }

        @Override
        public boolean shouldContinue() {
            if (slave.getTarget() != null && slave.getTarget().isAlive()) return false;
            return targetOwner != null && targetOwner.isAlive() && slave.squaredDistanceTo(targetOwner) > 4.0;
        }

        @Override
        public void start() {
            timer = 0;
            slave.getNavigation().startMovingTo(targetOwner, 1.0);
        }

        @Override
        public void stop() { targetOwner = null; }

        @Override
        public void tick() {
            if (targetOwner == null) return;
            slave.getLookControl().lookAt(targetOwner, 10.0F, (float)slave.getMaxLookPitchChange());
            if (--timer <= 0) {
                timer = 10;
                slave.getNavigation().startMovingTo(targetOwner, 1.0);
            }
        }
    }

    private static class DefendOwnerGoal extends Goal {
        private final FrozenSlaveEntity slave;
        private LivingEntity attacker;

        DefendOwnerGoal(FrozenSlaveEntity slave) {
            this.slave = slave;
        }

        @Override
        public boolean canStart() {
            LivingEntity owner = slave.getOwner();
            if (owner == null) return false;
            LivingEntity ownerAttacker = owner.getAttacker();
            if (ownerAttacker == null || !ownerAttacker.isAlive() || ownerAttacker == slave) return false;
            var relation = EntityRelations.getRelation(owner, ownerAttacker);
            if (relation == EntityRelation.ALLY || relation == EntityRelation.FRIENDLY) return false;
            attacker = ownerAttacker;
            return true;
        }

        @Override
        public boolean shouldContinue() {
            return attacker != null && attacker.isAlive();
        }

        @Override
        public void start() { slave.setTarget(attacker); }

        @Override
        public void stop() { attacker = null; }
    }
}
