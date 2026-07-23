package com.lne_archers.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.internals.target.EntityRelation;
import net.spell_engine.internals.target.EntityRelations;

import java.util.*;

public class WintersGripEntity extends Entity implements SpellEntity.Spawned {
    public static EntityType<WintersGripEntity> ENTITY_TYPE;

    private LivingEntity owner;
    private UUID ownerUuid;
    private int timeToLive;
    private final Map<UUID, Vec3d> trackedEnemies = new HashMap<>();

    public WintersGripEntity(EntityType<? extends WintersGripEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        this.owner = args.owner();
        this.ownerUuid = owner.getUuid();
        this.timeToLive = (int)(args.spawnData().time_to_live_seconds * 20);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        this.timeToLive = nbt.getInt("TimeToLive");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
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
        if (getWorld().isClient) return;

        LivingEntity owner = getOwner();
        if (owner == null || !owner.isAlive()) {
            discard();
            return;
        }
        if (--timeToLive <= 0) {
            discard();
            return;
        }

        List<LivingEntity> current = getWorld().getNonSpectatingEntities(LivingEntity.class, getBoundingBox());
        Set<UUID> currentUuids = new HashSet<>();
        for (LivingEntity entity : current) {
            if (!isAlly(owner, entity)) {
                currentUuids.add(entity.getUuid());
                trackedEnemies.put(entity.getUuid(), entity.getPos());
            }
        }

        if (getWorld() instanceof ServerWorld sw) {
            Iterator<Map.Entry<UUID, Vec3d>> it = trackedEnemies.entrySet().iterator();
            while (it.hasNext()) {
                var entry = it.next();
                if (!currentUuids.contains(entry.getKey())) {
                    Entity tracked = sw.getEntity(entry.getKey());
                    if (tracked == null || (tracked instanceof LivingEntity living && !living.isAlive())) {
                        spawnFrozenSlave(sw, entry.getValue());
                    }
                    it.remove();
                }
            }
        }
    }

    private void spawnFrozenSlave(ServerWorld world, Vec3d pos) {
        LivingEntity owner = getOwner();
        if (owner == null) return;
        /// ENTITY SPELL SPAWNING IMPACT HERE
        FrozenSlaveEntity slave = new FrozenSlaveEntity(FrozenSlaveEntity.ENTITY_TYPE, world, owner);
        slave.setPosition(pos);
        world.spawnEntity(slave);
    }

    private boolean isAlly(LivingEntity owner, Entity entity) {
        if (entity == owner) return true;
        var relation = EntityRelations.getRelation(owner, entity);
        return relation == EntityRelation.ALLY || relation == EntityRelation.FRIENDLY;
    }

    @Override
    public boolean isCollidable() { return false; }

    @Override
    public boolean isPushable() { return false; }
}
