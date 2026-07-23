package com.lne_archers.client.entity;

import com.lne_archers.entity.WintersGripEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WintersGripRenderer extends EntityRenderer<WintersGripEntity> {

    // MARK: Set the texture path for the winters_grip model here
    private static final Identifier TEXTURE = Identifier.of("lne_archers", "textures/entity/winters_grip.png");

    public WintersGripRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(WintersGripEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // MARK: Implement model rendering here using the model "lne_archers:spell_effect/winters_grip"
        // Reference your existing renderers (e.g. EarthGolemSpikeEntityRenderer) for the pattern.
        // Example using ItemRenderer or SinglePartEntityModel with a registered layer.
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(WintersGripEntity entity) {
        return TEXTURE;
    }
}
