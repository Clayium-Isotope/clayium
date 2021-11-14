package mods.clayium.render.tile;

import mods.clayium.block.tile.IAxisAlignedBBContainer;
import mods.clayium.block.tile.TileClayMarker;
import mods.clayium.util.UtilRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ClayMarkerRenderer
        extends TileEntitySpecialRenderer {
    static double[] ds1 = new double[] {0.1875D, -0.1875D};
    static double[] ds2 = new double[] {0.125D, -0.125D};


    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_147500_8_) {
        if (tile instanceof TileClayMarker) {
            Tessellator tessellator;
            double d;
            Block block, block1;
            UtilRender.setLightValue(15, 15);

            TileClayMarker tile1 = (TileClayMarker) tile;
            switch (tile1.state) {
                case 1:
                    GL11.glDepthMask(false);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 1);

                    GL11.glDisable(2912);
                    GL11.glDisable(2929);
                    GL11.glDisable(2896);
                    GL11.glDisable(2884);

                    tessellator = Tessellator.instance;
                    GL11.glDisable(3553);

                    GL11.glLineWidth(3.0F);
                    tessellator.startDrawing(1);
                    tessellator.setColorRGBA_F(0.1F, 0.5F, 0.2F, 0.3F);
                    for (double d1 : ds1) {
                        for (double d2 : ds1) {
                            tessellator.addVertex(x + 0.0D + TileClayMarker.maxRange, y + 0.5D + d1, z + 0.5D + d2);
                            tessellator.addVertex(x + 1.0D - TileClayMarker.maxRange, y + 0.5D + d1, z + 0.5D + d2);
                            tessellator.addVertex(x + 0.5D + d1, y + 0.0D + TileClayMarker.maxRange, z + 0.5D + d2);
                            tessellator.addVertex(x + 0.5D + d1, y + 1.0D - TileClayMarker.maxRange, z + 0.5D + d2);
                            tessellator.addVertex(x + 0.5D + d1, y + 0.5D + d2, z + 0.0D + TileClayMarker.maxRange);
                            tessellator.addVertex(x + 0.5D + d1, y + 0.5D + d2, z + 1.0D - TileClayMarker.maxRange);
                        }
                    }


                    tessellator.draw();


                    GL11.glDisable(3042);
                    GL11.glEnable(2929);
                    GL11.glEnable(2912);

                    GL11.glLineWidth(2.0F);
                    tessellator.startDrawing(1);
                    tessellator.setColorRGBA_F(0.2F, 0.7F, 0.3F, 1.0F);
                    d = 0.02D;
                    for (double d1 : ds1) {
                        for (double d2 : ds1) {
                            tessellator.addVertex(x + 0.0D + TileClayMarker.maxRange, y + 0.5D + d1, z + 0.5D + d2);
                            tessellator.addVertex(x + 1.0D - TileClayMarker.maxRange, y + 0.5D + d1, z + 0.5D + d2);
                            tessellator.addVertex(x + 0.5D + d1, y + 0.0D + TileClayMarker.maxRange, z + 0.5D + d2);
                            tessellator.addVertex(x + 0.5D + d1, y + 1.0D - TileClayMarker.maxRange, z + 0.5D + d2);
                            tessellator.addVertex(x + 0.5D + d1, y + 0.5D + d2, z + 0.0D + TileClayMarker.maxRange);
                            tessellator.addVertex(x + 0.5D + d1, y + 0.5D + d2, z + 1.0D - TileClayMarker.maxRange);
                        }
                    }
                    tessellator.draw();

                    GL11.glEnable(2896);
                    GL11.glEnable(3553);
                    GL11.glEnable(2884);
                    GL11.glBlendFunc(770, 771);

                    block = tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord);
                    UtilRender.renderBox(tile.xCoord + block.getBlockBoundsMinX(), tile.yCoord + block.getBlockBoundsMinY(), tile.zCoord + block.getBlockBoundsMinZ(), tile.xCoord + block
                            .getBlockBoundsMaxX(), tile.yCoord + block.getBlockBoundsMaxY(), tile.zCoord + block.getBlockBoundsMaxZ(), 1, 1.0F, 0.0F, 0.0F);
                    break;
                case 2:
                case 3:
                case 4:
                    UtilRender.renderAxisAlignedBB((IAxisAlignedBBContainer) tile1, 0.1F, 0.1F, 0.7F);
                    block1 = tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord);
                    UtilRender.renderBox(tile.xCoord + block1.getBlockBoundsMinX(), tile.yCoord + block1.getBlockBoundsMinY(), tile.zCoord + block1.getBlockBoundsMinZ(), tile.xCoord + block1
                                    .getBlockBoundsMaxX(), tile.yCoord + block1.getBlockBoundsMaxY(), tile.zCoord + block1.getBlockBoundsMaxZ(),
                            Math.max(1, tile1.getBoxAppearance()), 1.0F, 0.0F, 0.0F);
                    break;
            }
        }
    }
}
