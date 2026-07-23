package com.lne_archers.client.entity;

import com.lne_archers.entity.FrozenSlaveEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrozenSlaveRenderer extends MobEntityRenderer<FrozenSlaveEntity, PlayerEntityModel<FrozenSlaveEntity>> {

    // Renders with the Minecraft ice block texture for the frozen look.
    // The entity is always glowing (set in FrozenSlaveEntity.isGlowing()).
    // MARK: For full transparency, override render() and swap the RenderLayer to
    //       RenderLayer.getEntityTranslucent(TEXTURE) and call model.render() manually with a lowered alpha color.
    private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/block/ice.png");

    public FrozenSlaveRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public Identifier getTexture(FrozenSlaveEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(FrozenSlaveEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // MARK: Swap to translucent render here for transparency:
        // VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        // model.render(matrices, consumer, light, getOverlay(entity, 0.0F), ColorHelper.Argb.getArgb(180, 200, 230, 255));
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
