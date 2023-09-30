package net.mehvahdjukaar.meteors.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mehvahdjukaar.meteors.MeteorEntity;
import net.mehvahdjukaar.meteors.Meteors;
import net.mehvahdjukaar.meteors.MeteorsClient;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MeteorRenderer extends EntityRenderer<MeteorEntity> {
    private static final ResourceLocation TEXTURE = Meteors.res("textures/entity/meteor.png");
    private static final ResourceLocation OVERLAY_TEXTURE = Meteors.res("textures/entity/overlay.png");
    private final ModelPart meteor;
    private final ModelPart meteorEmissive;
    private final ModelPart overlay;

    public MeteorRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart ball1 = context.bakeLayer(MeteorsClient.METEOR_MODEL);
        this.meteor = ball1.getChild("meteor");
        this.meteorEmissive = ball1.getChild("meteor_emissive");
        this.overlay = ball1.getChild("overlay");
    }

    @Override
    public void render(MeteorEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.pushPose();
        float si = (float) (Math.sin(System.currentTimeMillis() / 8000.0) * 400);
        float s2 = (float) (Math.sin(System.currentTimeMillis() / 5000.0) * 700);

        poseStack.mulPose(Axis.YP.rotationDegrees(si));
        poseStack.mulPose(Axis.XP.rotationDegrees(s2));
        var s = LightTexture.sky(packedLight);
        var b = Math.max(7,LightTexture.block(packedLight));

        VertexConsumer buffer1 = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        meteor.render(poseStack, buffer1,
                LightTexture.pack(b,s), OverlayTexture.NO_OVERLAY);
        meteorEmissive.render(poseStack, buffer1,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        float f = (float)entity.tickCount + partialTick;
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.energySwirl(OVERLAY_TEXTURE,
                this.xOffset(f) % 1.0F, f * 0.01F % 1.0F));

        overlay.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private float xOffset(float tickCount) {
        return tickCount * 0.01F;
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorEntity entity) {
        return TEXTURE;
    }

    public static LayerDefinition createMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("meteor", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, -4F, -4F, 8F, 8, 8),
                PartPose.offset(0, 0, 0));

        root.addOrReplaceChild("meteor_emissive", CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-4F, -4F, -4F, 8F, 8, 8),
                PartPose.offset(0, 0, 0));

        root.addOrReplaceChild("overlay", CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-5F, -5F, -5F, 10, 10, 10),
                PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

}
