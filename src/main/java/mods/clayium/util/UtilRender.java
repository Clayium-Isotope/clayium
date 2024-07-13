package mods.clayium.util;

import mods.clayium.machine.ClayMarker.AABBHolder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class UtilRender {
    /**
     * From {@link net.minecraft.client.renderer.entity.Render#renderOffsetAABB(AxisAlignedBB, double, double, double)}
     */
    public static void renderOffsetAABB(AxisAlignedBB boundingBox, Vec3d pos, Color color, BufferBuilder bufferbuilder) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        bufferbuilder.setTranslation(pos.x, pos.y, pos.z);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.finishDrawing();
        bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void renderLine(Vec3d p1, Vec3d p2, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(p1.x + 0.5, p1.y + 0.5, p1.z + 0.5).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(p2.x + 0.5, p2.y + 0.5, p2.z + 0.5).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void renderAxisAlignedBB(AABBHolder tile, Color rgba, boolean border, TileEntityRendererDispatcher dispatcher) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        AABBHolder.Appearance state = tile.getBoxAppearance();
        int pass = MinecraftForgeClient.getRenderPass();

        if (AABBHolder.Appearance.NoRender.equals(state) || /*pass == 1 ||*/ !tile.hasAxisAlignedBB()) {
            return;
        }

        final double minX = tile.getAxisAlignedBB().minX - dispatcher.entityX;
        final double minY = tile.getAxisAlignedBB().minY - dispatcher.entityY;
        final double minZ = tile.getAxisAlignedBB().minZ - dispatcher.entityZ;
        final double maxX = tile.getAxisAlignedBB().maxX - dispatcher.entityX;
        final double maxY = tile.getAxisAlignedBB().maxY - dispatcher.entityY;
        final double maxZ = tile.getAxisAlignedBB().maxZ - dispatcher.entityZ;
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(32823);
        GL11.glPolygonOffset(-1.0F, -1.0F);
        GL11.glDisable(2912);
        if (AABBHolder.Appearance._2.equals(state)) {
            GL11.glDisable(2929);
        }

        GL11.glDisable(2896);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        builder.pos(minX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(maxX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        builder.pos(minX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
        tessellator.draw();

        if (border) {
            GL11.glLineWidth(2.0F);
            builder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
            builder.pos(minX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(minX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);

            builder.pos(minX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(minX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            tessellator.draw();

            builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            builder.pos(minX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(minX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(minX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(minX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, minY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, maxY, minZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, minY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            builder.pos(maxX, maxY, maxZ).color(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), 0xff);
            tessellator.draw();
        }

        GL11.glEnable(3553);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2912);
        if (AABBHolder.Appearance._2.equals(state)) {
            GL11.glEnable(2929);
        }

        GL11.glDisable(32823);
        GL11.glEnable(2896);
        GL11.glEnable(2884);

    }

    public static void renderBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, AABBHolder.Appearance state, float r, float g, float b, float alpha, boolean border, TileEntityRendererDispatcher dispatcher) {
        renderAxisAlignedBB(new AABBforRender(minX, minY, minZ, maxX, maxY, maxZ, state), new Color(r, g, b, alpha), border, dispatcher);
    }

    public static void renderBox(AxisAlignedBB aabb, AABBHolder.Appearance state, Color color, boolean border, TileEntityRendererDispatcher dispatcher) {
        renderAxisAlignedBB(new AABBforRender(aabb, state), color, border, dispatcher);
    }

    public static void renderBoldAABB(AxisAlignedBB aabb, Color rgba, boolean border) {
        RenderGlobal.renderFilledBox(aabb, rgba.getRed() / 255f, rgba.getGreen() / 255f, rgba.getBlue() / 255f, rgba.getAlpha() / 255f);

        if (border) {
            GL11.glLineWidth(2.0F);
            RenderGlobal.drawSelectionBoundingBox(aabb, rgba.getRed() / 255f, rgba.getGreen() / 255f, rgba.getBlue() / 255f, 1.0f);
        }
    }

    private static class AABBforRender implements AABBHolder {
        private final AxisAlignedBB aabb;
        private final Appearance state;

        public AABBforRender(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Appearance state) {
            this(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), state);
        }

        public AABBforRender(AxisAlignedBB aabb, Appearance state) {
            this.aabb = aabb;
            this.state = state;
        }

        public AxisAlignedBB getAxisAlignedBB() {
            return this.aabb;
        }

        public boolean hasAxisAlignedBB() {
            return true;
        }

        public Appearance getBoxAppearance() {
            return this.state;
        }

        public void setAxisAlignedBB(AxisAlignedBB aabb) {}
    }

    public static void setLightValue(int sky, int block) {
        int i = sky << 20 | block << 4;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
    }
}
