package mods.clayium.util;

import mods.clayium.block.tile.IAxisAlignedBBContainer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class UtilRender {
    public static UtilRender INSTANCE = new UtilRender();

    public static void renderInvCuboid(RenderBlocks renderer, Block block, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, IIcon icon) {
        Tessellator tessellator = Tessellator.instance;
        float minX_ = (float) block.getBlockBoundsMinX();
        float maxX_ = (float) block.getBlockBoundsMaxX();
        float minY_ = (float) block.getBlockBoundsMinY();
        float maxY_ = (float) block.getBlockBoundsMaxY();
        float minZ_ = (float) block.getBlockBoundsMinZ();
        float maxZ_ = (float) block.getBlockBoundsMaxZ();
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        block.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        renderer.setRenderBoundsFromBlock(block);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        block.setBlockBounds(minX_, minY_, minZ_, maxX_, maxY_, maxZ_);
        renderer.setRenderBoundsFromBlock(block);
    }

    public static void renderAxisAlignedBB(IAxisAlignedBBContainer tile, float r, float g, float b, float alpha, boolean border) {
        int state = tile.getBoxAppearance();
        int pass = MinecraftForgeClient.getRenderPass();
        if (state != 0 && pass == 1 && tile.hasAxisAlignedBB()) {
            double minX = (tile.getAxisAlignedBB()).minX - TileEntityRendererDispatcher.staticPlayerX;
            double minY = (tile.getAxisAlignedBB()).minY - TileEntityRendererDispatcher.staticPlayerY;
            double minZ = (tile.getAxisAlignedBB()).minZ - TileEntityRendererDispatcher.staticPlayerZ;
            double maxX = (tile.getAxisAlignedBB()).maxX - TileEntityRendererDispatcher.staticPlayerX;
            double maxY = (tile.getAxisAlignedBB()).maxY - TileEntityRendererDispatcher.staticPlayerY;
            double maxZ = (tile.getAxisAlignedBB()).maxZ - TileEntityRendererDispatcher.staticPlayerZ;

            GL11.glDepthMask(false);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);

            GL11.glEnable(32823);
            GL11.glPolygonOffset(-1.0F, -1.0F);


            GL11.glDisable(2912);
            if (state == 2) GL11.glDisable(2929);

            GL11.glDisable(2896);
            GL11.glDisable(2884);

            Tessellator tessellator = Tessellator.instance;
            GL11.glDisable(3553);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(r, g, b, alpha);
            tessellator.addVertex(minX, minY, minZ);
            tessellator.addVertex(minX, minY, maxZ);
            tessellator.addVertex(minX, maxY, maxZ);
            tessellator.addVertex(minX, maxY, minZ);

            tessellator.addVertex(maxX, minY, minZ);
            tessellator.addVertex(maxX, minY, maxZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(maxX, maxY, minZ);

            tessellator.addVertex(minX, minY, minZ);
            tessellator.addVertex(minX, minY, maxZ);
            tessellator.addVertex(maxX, minY, maxZ);
            tessellator.addVertex(maxX, minY, minZ);

            tessellator.addVertex(minX, maxY, minZ);
            tessellator.addVertex(minX, maxY, maxZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(maxX, maxY, minZ);

            tessellator.addVertex(minX, minY, minZ);
            tessellator.addVertex(maxX, minY, minZ);
            tessellator.addVertex(maxX, maxY, minZ);
            tessellator.addVertex(minX, maxY, minZ);

            tessellator.addVertex(minX, minY, maxZ);
            tessellator.addVertex(maxX, minY, maxZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(minX, maxY, maxZ);
            tessellator.draw();

            if (border) {
                GL11.glLineWidth(2.0F);
                tessellator.startDrawing(2);
                tessellator.setColorRGBA_F(r, g, b, 1.0F);
                tessellator.addVertex(minX, minY, minZ);
                tessellator.addVertex(minX, minY, maxZ);
                tessellator.addVertex(maxX, minY, maxZ);
                tessellator.addVertex(maxX, minY, minZ);
                tessellator.draw();

                tessellator.startDrawing(2);
                tessellator.setColorRGBA_F(r, g, b, 1.0F);
                tessellator.addVertex(minX, maxY, minZ);
                tessellator.addVertex(minX, maxY, maxZ);
                tessellator.addVertex(maxX, maxY, maxZ);
                tessellator.addVertex(maxX, maxY, minZ);
                tessellator.draw();

                tessellator.startDrawing(1);
                tessellator.setColorRGBA_F(r, g, b, 1.0F);
                tessellator.addVertex(minX, minY, minZ);
                tessellator.addVertex(minX, maxY, minZ);
                tessellator.addVertex(minX, minY, maxZ);
                tessellator.addVertex(minX, maxY, maxZ);
                tessellator.addVertex(maxX, minY, minZ);
                tessellator.addVertex(maxX, maxY, minZ);
                tessellator.addVertex(maxX, minY, maxZ);
                tessellator.addVertex(maxX, maxY, maxZ);
                tessellator.draw();
            }

            GL11.glEnable(3553);

            GL11.glDepthMask(true);
            GL11.glDisable(3042);

            GL11.glEnable(2912);
            if (state == 2) GL11.glEnable(2929);

            GL11.glDisable(32823);

            GL11.glEnable(2896);
            GL11.glEnable(2884);
        }
    }

    public static void renderAxisAlignedBB(IAxisAlignedBBContainer tile, float r, float g, float b) {
        renderAxisAlignedBB(tile, r, g, b, 0.3F, true);
    }

    public static void renderBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int state, float r, float g, float b, float alpha, boolean border) {
        INSTANCE.getClass();
        renderAxisAlignedBB(new AABB4Render(minX, minY, minZ, maxX, maxY, maxZ, state), r, g, b, alpha, border);
    }

    public static void renderBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int state, float r, float g, float b, boolean border) {
        renderBox(minX, minY, minZ, maxX, maxY, maxZ, state, r, g, b, 0.3F, true);
    }

    public static void renderBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int state, float r, float g, float b) {
        renderBox(minX, minY, minZ, maxX, maxY, maxZ, state, r, g, b, true);
    }

    public static class AABB4Render implements IAxisAlignedBBContainer {
        private AxisAlignedBB aabb;
        private int state;

        public AABB4Render(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int state) {
            this.aabb = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            this.state = state;
        }

        public AxisAlignedBB getAxisAlignedBB() {
            return this.aabb;
        }

        public boolean hasAxisAlignedBB() {
            return true;
        }

        public int getBoxAppearance() {
            return this.state;
        }

        public void setAxisAlignedBB(AxisAlignedBB aabb) {
            this.aabb = aabb;
        }
    }

    public static void setLightValue(int sky, int block) {
        int i = sky << 20 | block << 4;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
    }
}
