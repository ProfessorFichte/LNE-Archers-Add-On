package com.lne_archers.client.render;

import com.lne_archers.effects.FrozenSlaveVisualAccess;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class FrozenSlaveOverlayFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier ICE_TEXTURE = Identifier.ofVanilla("textures/block/ice.png");
    private static final int OVERLAY_COLOR = (ColorHelper.channelFromFloat(0.8F) << 24) | 0xFFFFFF;

    public FrozenSlaveOverlayFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
                        float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                        float headYaw, float headPitch) {
        if (!(entity instanceof FrozenSlaveVisualAccess access) || !access.lneArchers$isFrozenSlaveVisual()) return;

        var vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(ICE_TEXTURE));
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, OVERLAY_COLOR);
    }
}
