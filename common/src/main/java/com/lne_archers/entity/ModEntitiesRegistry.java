package com.lne_archers.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class ModEntitiesRegistry {

    public static void registerEntities() {
        WintersGripEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "winters_grip"),
                EntityType.Builder.<WintersGripEntity>create(WintersGripEntity::new, SpawnGroup.MISC)
                        .dimensions(6F, 0.5F)
                        .makeFireImmune()
                        .maxTrackingRange(128)
                        .trackingTickInterval(20)
                        .build("winters_grip")
        );

        InfiltratorsArrowProjectile.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "infiltrators_arrow"),
                EntityType.Builder.<InfiltratorsArrowProjectile>create(InfiltratorsArrowProjectile::new, SpawnGroup.MISC)
                        .dimensions(0.5F, 0.5F)
                        .maxTrackingRange(64)
                        .trackingTickInterval(20)
                        .build("infiltrators_arrow")
        );
    }
}
