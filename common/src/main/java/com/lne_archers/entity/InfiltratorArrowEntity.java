package com.lne_archers.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class InfiltratorArrowEntity extends ArrowEntity {
    public static EntityType<InfiltratorArrowEntity> ENTITY_TYPE;

    // MARK: Configure max range here (in blocks)
    public static float MAX_RANGE = 32.0F;

    private Vec3d spawnPos;

    public InfiltratorArrowEntity(EntityType<? extends InfiltratorArrowEntity> type, World world) {
        super((EntityType<? extends ArrowEntity>) type, world);
    }

    public static InfiltratorArrowEntity create(World world, LivingEntity shooter) {
        var arrow = new InfiltratorArrowEntity(ENTITY_TYPE, world);
        arrow.setOwner(shooter);
        arrow.spawnPos = shooter.getPos();
        arrow.setDamage(0.0);
        arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        return arrow;
    }

    @Override
    public void tick() {
        super.tick();

        if (spawnPos == null) spawnPos = getPos();

        if (!getWorld().isClient && getPos().distanceTo(spawnPos) >= MAX_RANGE) {
            discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        teleportOwner(hitResult.getEntity().getX(), hitResult.getEntity().getY(), hitResult.getEntity().getZ());
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        Vec3d pos = hitResult.getPos();
        teleportOwner(pos.x, pos.y, pos.z);
    }

    private void teleportOwner(double x, double y, double z) {
        if (getWorld().isClient) return;
        LivingEntity owner = getOwner() instanceof LivingEntity le ? le : null;
        if (owner == null) return;
        if (owner instanceof ServerPlayerEntity player) {
            player.networkHandler.requestTeleport(x, y, z, player.getYaw(), player.getPitch());
        } else {
            owner.setPosition(x, y, z);
            owner.setVelocity(Vec3d.ZERO);
            owner.fallDistance = 0.0F;
        }
        discard();
    }
}
