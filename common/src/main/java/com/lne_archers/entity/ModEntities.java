package com.lne_archers.entity;

import com.lne_archers.LNE_ArchersMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static EntityType<WintersGripEntity> WINTERS_GRIP_TYPE;
    public static EntityType<FrozenSlaveEntity> FROZEN_SLAVE_TYPE;
    public static EntityType<InfiltratorArrowEntity> INFILTRATOR_ARROW_TYPE;

    public static void register() {
        WINTERS_GRIP_TYPE = WintersGripEntity.ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(LNE_ArchersMod.MOD_ID, "winters_grip"),
            FabricEntityTypeBuilder.<WintersGripEntity>create(SpawnGroup.MISC, WintersGripEntity::new)
                .dimensions(EntityDimensions.fixed(5.0F, 3.0F))
                .fireImmune()
                .trackRangeBlocks(64)
                .trackedUpdateRate(20)
                .build()
        );

        FROZEN_SLAVE_TYPE = FrozenSlaveEntity.ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(LNE_ArchersMod.MOD_ID, "frozen_slave"),
            FabricEntityTypeBuilder.<FrozenSlaveEntity>create(SpawnGroup.CREATURE, FrozenSlaveEntity::new)
                .dimensions(EntityDimensions.fixed(0.6F, 1.95F))
                .trackRangeBlocks(64)
                .trackedUpdateRate(2)
                .build()
        );

        INFILTRATOR_ARROW_TYPE = InfiltratorArrowEntity.ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(LNE_ArchersMod.MOD_ID, "infiltrator_arrow"),
            FabricEntityTypeBuilder.<InfiltratorArrowEntity>create(SpawnGroup.MISC, InfiltratorArrowEntity::new)
                .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                .trackRangeBlocks(64)
                .trackedUpdateRate(1)
                .build()
        );
    }

    public static void registerAttributes() {
        net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry.register(
            FrozenSlaveEntity.ENTITY_TYPE,
            FrozenSlaveEntity.createAttributes()
        );
    }
}
