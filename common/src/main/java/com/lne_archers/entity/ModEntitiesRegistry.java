package com.lne_archers.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
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
                FabricEntityTypeBuilder.<WintersGripEntity>create(SpawnGroup.MISC, WintersGripEntity::new)
                        .dimensions(EntityDimensions.changing(6F, 0.5F))
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );

        InfiltratorsArrowProjectile.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "infiltrators_arrow"),
                FabricEntityTypeBuilder.<InfiltratorsArrowProjectile>create(SpawnGroup.MISC, InfiltratorsArrowProjectile::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                        .trackRangeBlocks(64)
                        .trackedUpdateRate(20)
                        .build()
        );
    }
}
