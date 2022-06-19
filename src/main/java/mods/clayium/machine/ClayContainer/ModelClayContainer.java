package mods.clayium.machine.ClayContainer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class ModelClayContainer extends ModelBase {
    private static final float pipeWidth = 0.1875F;

    // always should reset them
    private PositionTextureVertex ptv0;
    private PositionTextureVertex ptv1;
    private PositionTextureVertex ptv2;
    private PositionTextureVertex ptv3;
    private PositionTextureVertex ptv4;
    private PositionTextureVertex ptv5;
    private PositionTextureVertex ptv6;
    private PositionTextureVertex ptv7;

    private static final PositionTextureVertex[] fullBlockVtx = new PositionTextureVertex[] {
            new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 0.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 1.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 0.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 1.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 0.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 1.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 0.0d), 0f, 0f),
            new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 1.0d), 0f, 0f)
    };

    private final ModelRenderer hull = new ModelRenderer(this).setTextureSize(16, 16);

    public ModelClayContainer(BufferBuilder builder) {
        this.textureHeight = this.textureWidth = 16;

        this.hull.addBox(0.0f, 0.0f, 0.0f, 16, 16, 16);
    }

    public void renderBlocked() {
        this.hull.render(0.0625F);
    }

    public void renderPiped(ClayContainer.ClayContainerState state, BufferBuilder builder) {
        renderPipeOrigin(builder);
        if (state.isTheFacingActivated(EnumFacing.UP)) renderPipeUp(builder);
        if (state.isTheFacingActivated(EnumFacing.DOWN)) renderPipeDown(builder);
        if (state.isTheFacingActivated(EnumFacing.NORTH)) renderPipeNorth(builder);
        if (state.isTheFacingActivated(EnumFacing.SOUTH)) renderPipeSouth(builder);
        if (state.isTheFacingActivated(EnumFacing.WEST)) renderPipeWest(builder);
        if (state.isTheFacingActivated(EnumFacing.EAST)) renderPipeEast(builder);
    }

    private void renderPipeOrigin(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);

        new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv2, ptv3, ptv7}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[] {ptv2, ptv6, ptv4, ptv0}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[] {ptv7, ptv3, ptv1, ptv5}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[] {ptv3, ptv2, ptv0, ptv1}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv7, ptv5, ptv4}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }

    public void renderPipeDown(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 1.0d,             0.5d - pipeWidth), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 1.0d,             0.5d + pipeWidth), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 1.0d,             0.5d - pipeWidth), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 1.0d,             0.5d + pipeWidth), 0f, 0f);

        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv2, ptv3, ptv7}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
//            new TexturedQuad(new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv6, ptv4, ptv0}, 5, 0, 11, 5, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv7, ptv3, ptv1, ptv5}, 5, 0, 11, 5, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv3, ptv2, ptv0, ptv1}, 5, 0, 11, 5, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv7, ptv5, ptv4}, 5, 0, 11, 5, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }

    public void renderPipeUp(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.0d,             0.5d - pipeWidth), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.0d,             0.5d + pipeWidth), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.0d,             0.5d - pipeWidth), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.0d,             0.5d + pipeWidth), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);

//            new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv2, ptv3, ptv7}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv5, ptv1, ptv0, ptv4}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv6, ptv4, ptv0}, 5, 11, 11, 16, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv7, ptv3, ptv1, ptv5}, 5, 11, 11, 16, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv3, ptv2, ptv0, ptv1}, 5, 11, 11, 16, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv7, ptv5, ptv4}, 5, 11, 11, 16, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }

    public void renderPipeSouth(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.0d            ), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.0d            ), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.0d            ), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.0d            ), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);

        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv2, ptv3, ptv7}, 5, 0, 11, 5, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv5, ptv1, ptv0, ptv4}, 5, 11, 11, 16, 16, 16).draw(builder, 1f);
//            new TexturedQuad(new PositionTextureVertex[] {ptv2, ptv6, ptv4, ptv0}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv7, ptv3, ptv1, ptv5}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv3, ptv2, ptv0, ptv1}, 0, 5, 5, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv7, ptv5, ptv4}, 11, 5, 16, 11, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }

    public void renderPipeNorth(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 1.0d            ), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 1.0d            ), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 1.0d            ), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 1.0d            ), 0f, 0f);

        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv2, ptv3, ptv7}, 5, 11, 11, 16, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv5, ptv1, ptv0, ptv4}, 5, 0, 11, 5, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv6, ptv4, ptv0}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
//            new TexturedQuad(new PositionTextureVertex[] {ptv7, ptv3, ptv1, ptv5}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv3, ptv2, ptv0, ptv1}, 11, 5, 16, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv7, ptv5, ptv4}, 0, 5, 5, 11, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }

    public void renderPipeWest(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.0d,             0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.0d,             0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.0d,             0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.0d,             0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);

        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv2, ptv3, ptv7}, 0, 5, 5, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv5, ptv1, ptv0, ptv4}, 0, 5, 5, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv6, ptv4, ptv0}, 11, 5, 16, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv7, ptv3, ptv1, ptv5}, 0, 5, 5, 11, 16, 16).draw(builder, 1f);
//            new TexturedQuad(new PositionTextureVertex[] {ptv3, ptv2, ptv0, ptv1}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv7, ptv5, ptv4}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }

    public void renderPipeEast(BufferBuilder builder) {
        GlStateManager.pushMatrix();

        ptv0 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv1 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv2 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv3 = new PositionTextureVertex(new Vec3d(0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv4 = new PositionTextureVertex(new Vec3d(1.0d,             0.5d - pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv5 = new PositionTextureVertex(new Vec3d(1.0d,             0.5d - pipeWidth, 0.5d + pipeWidth), 0f, 0f);
        ptv6 = new PositionTextureVertex(new Vec3d(1.0d,             0.5d + pipeWidth, 0.5d - pipeWidth), 0f, 0f);
        ptv7 = new PositionTextureVertex(new Vec3d(1.0d,             0.5d + pipeWidth, 0.5d + pipeWidth), 0f, 0f);

        new TexturedQuad(new PositionTextureVertex[]{ptv6, ptv2, ptv3, ptv7}, 11, 5, 16, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv5, ptv1, ptv0, ptv4}, 11, 5, 16, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv2, ptv6, ptv4, ptv0}, 0, 5, 5, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv7, ptv3, ptv1, ptv5}, 11, 5, 16, 11, 16, 16).draw(builder, 1f);
        new TexturedQuad(new PositionTextureVertex[]{ptv3, ptv2, ptv0, ptv1}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);
//            new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv7, ptv5, ptv4}, 5, 5, 11, 11, 16, 16).draw(builder, 1f);

        GlStateManager.popMatrix();
    }
}
