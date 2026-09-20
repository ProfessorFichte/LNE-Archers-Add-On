package com.lne_archers.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class ModEntitiesRegistry {

    public static Map<Identifier, EntityType<?>> entityTypesToRegister() {
        var types = new LinkedHashMap<Identifier, EntityType<?>>();

        WintersGripEntity.ENTITY_TYPE =
                EntityType.Builder.<WintersGripEntity>create(WintersGripEntity::new, SpawnGroup.MISC)
                        .setDimensions(6F, 0.5F)
                        .makeFireImmune()
                        .maxTrackingRange(128)
                        .trackingTickInterval(20)
                        .build("winters_grip");
        types.put(new Identifier(MOD_ID, "winters_grip"), WintersGripEntity.ENTITY_TYPE);

        InfiltratorsArrowProjectile.ENTITY_TYPE =
                EntityType.Builder.<InfiltratorsArrowProjectile>create(InfiltratorsArrowProjectile::new, SpawnGroup.MISC)
                        .setDimensions(0.5F, 0.5F)
                        .maxTrackingRange(64)
                        .trackingTickInterval(20)
                        .build("infiltrators_arrow");
        types.put(new Identifier(MOD_ID, "infiltrators_arrow"), InfiltratorsArrowProjectile.ENTITY_TYPE);

        return types;
    }

    public static void registerEntities() {
        entityTypesToRegister().forEach((id, entityType) -> Registry.register(Registries.ENTITY_TYPE, id, entityType));
    }
}
