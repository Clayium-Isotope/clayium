package mods.clayium.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.clayium.block.CBlocks;
import mods.clayium.block.ClayContainer;
import mods.clayium.block.LaserReflector;
import mods.clayium.block.laser.IClayLaserManager;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TessellatorHelper;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;


public class RenderLaserReflector
        implements ISimpleBlockRenderingHandler {
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        UtilDirection direction = UtilDirection.SOUTH;
        if (modelId == getRenderId()) {

            GL11.glPushMatrix();
            TessellatorHelper.startDrawingQuads();

            IIcon iicon = renderer.getBlockIcon(CBlocks.blockLaserReflector);

            float f14 = iicon.getMinU();
            float f15 = iicon.getMaxU();
            float f4 = iicon.getMinV();
            float f5 = iicon.getMaxV();
            float f = 0.125F;

            TessellatorHelper.pushMatrix();
            TessellatorHelper.translate(-0.5D, -0.5D, -0.5D);
            TessellatorHelper.scale(1.2D, 1.2D, 1.2D);

            TessellatorHelper.translate(0.5D, 0.5D, 0.5D);
            switch (direction) {
                case UP:
                    TessellatorHelper.rotate(0.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case DOWN:
                    TessellatorHelper.rotate(180.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    TessellatorHelper.rotate(-90.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    TessellatorHelper.rotate(90.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case WEST:
                    TessellatorHelper.rotate(-90.0D, 0.0D, 0.0D, -1.0D);
                    break;
                case EAST:
                    TessellatorHelper.rotate(90.0D, 0.0D, 0.0D, -1.0D);
                    break;
            }


            TessellatorHelper.translate(-0.5D, -0.5D, -0.5D);

            TessellatorHelper.setColorRGBA(255, 255, 255, 225);
            TessellatorHelper.setNormal(0.0F, -1.0F, 0.0F);
            TessellatorHelper.addVertexWithUV(0.0D + (f * 2.0F), 0.0D + f, 0.0D + (f * 2.0F), f14, f4);
            TessellatorHelper.addVertexWithUV(1.0D - (f * 2.0F), 0.0D + f, 0.0D + (f * 2.0F), f15, f4);
            TessellatorHelper.addVertexWithUV(1.0D - (f * 2.0F), 0.0D + f, 1.0D - (f * 2.0F), f15, f5);
            TessellatorHelper.addVertexWithUV(0.0D + (f * 2.0F), 0.0D + f, 1.0D - (f * 2.0F), f14, f5);


            double x1 = 0.5D, y1 = 1.0D - f, z1 = 0.5D;
            double x2 = 1.0D - (f * 2.0F), y2 = 0.0D + f, z2 = 0.0D + (f * 2.0F);
            double x3 = 0.0D + (f * 2.0F), y3 = 0.0D + f, z3 = 0.0D + (f * 2.0F);

            double xa = x2 - x1, ya = y2 - y1, za = z2 - z1;
            double xb = x3 - x1, yb = y3 - y1, zb = z3 - z1;
            double xc = ya * zb - yb * za, yc = za * xb - zb * xa, zc = xa * yb - xb * ya, lc = Math.pow(xc * xc + yc * yc + zc * zc, 0.5D);

            for (int i = 0; i < 4; i++) {
                TessellatorHelper.setNormal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc));
                TessellatorHelper.addVertexWithUV(x1, y1, z1, f15, f4);
                TessellatorHelper.addVertexWithUV(x2, y2, z2, f15, f5);
                TessellatorHelper.addVertexWithUV(x3, y3, z3, f14, f5);
                TessellatorHelper.addVertexWithUV(x1, y1, z1, f15, f4);


                TessellatorHelper.translate(0.5D, 0.0D, 0.5D);
                TessellatorHelper.rotate(90.0D, 0.0D, 1.0D, 0.0D);
                TessellatorHelper.translate(-0.5D, 0.0D, -0.5D);
            }


            TessellatorHelper.popMatrix();

            TessellatorHelper.setColorOpaque_F(1.0F, 1.0F, 1.0F);

            TessellatorHelper.draw();
            GL11.glPopMatrix();
        }
    }


    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        UtilDirection direction = UtilDirection.getOrientation(((LaserReflector) world.getBlock(x, y, z)).getFront(world, x, y, z));
        if (te != null && te instanceof IClayLaserManager && ((IClayLaserManager) UtilBuilder.safeGetTileEntity(world, x, y, z)).getDirection() != null) {
            direction = ((IClayLaserManager) UtilBuilder.safeGetTileEntity(world, x, y, z)).getDirection();
        }
        if (modelId == getRenderId() && direction != null) {

            if (ClayContainer.renderPass == 0) {
                Tessellator tessellator = Tessellator.instance;


                tessellator.draw();

                IIcon iIcon = renderer.getBlockIcon(CBlocks.blockLaserReflector);

                float f1 = iIcon.getMinU();
                float f2 = iIcon.getMaxU();
                float f3 = iIcon.getMinV();
                float f6 = iIcon.getMaxV();
                float f7 = 0.125F;

                GL11.glPushMatrix();


                GL11.glEnable(32826);

                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(2884);


                GL11.glTranslatef(x + 0.5F, y + 0.5F, z + 0.5F);

                GL11.glTranslatef((-(x >> 4) * 16), (-(y >> 4) * 16), (-(z >> 4) * 16));
                switch (direction) {
                    case UP:
                        GL11.glRotatef(0.0F, 1.0F, 0.0F, 0.0F);
                        break;
                    case DOWN:
                        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                        break;
                    case NORTH:
                        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                        break;
                    case SOUTH:
                        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                        break;
                    case WEST:
                        GL11.glRotatef(-90.0F, 0.0F, 0.0F, -1.0F);
                        break;
                    case EAST:
                        GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
                        break;
                }

                GL11.glTranslatef(((x >> 4) * 16), ((y >> 4) * 16), ((z >> 4) * 16));
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

                tessellator.startDrawingQuads();
                tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
                tessellator.setColorRGBA(255, 255, 255, 255);
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                tessellator.addVertexWithUV(0.0D + (f7 * 2.0F), 0.0D + f7, 0.0D + (f7 * 2.0F), f1, f3);
                tessellator.addVertexWithUV(1.0D - (f7 * 2.0F), 0.0D + f7, 0.0D + (f7 * 2.0F), f2, f3);
                tessellator.addVertexWithUV(1.0D - (f7 * 2.0F), 0.0D + f7, 1.0D - (f7 * 2.0F), f2, f6);
                tessellator.addVertexWithUV(0.0D + (f7 * 2.0F), 0.0D + f7, 1.0D - (f7 * 2.0F), f1, f6);
                tessellator.draw();


                double d1 = 0.5D, d2 = 1.0D - f7, d3 = 0.5D;
                double d4 = 1.0D - (f7 * 2.0F), d5 = 0.0D + f7, d6 = 0.0D + (f7 * 2.0F);
                double d7 = 0.0D + (f7 * 2.0F), d8 = 0.0D + f7, d9 = 0.0D + (f7 * 2.0F);

                double d10 = d4 - d1, d11 = d5 - d2, d12 = d6 - d3;
                double d13 = d7 - d1, d14 = d8 - d2, d15 = d9 - d3;
                double d16 = d11 * d15 - d14 * d12, d17 = d12 * d13 - d15 * d10, d18 = d10 * d14 - d13 * d11, d19 = Math.pow(d16 * d16 + d17 * d17 + d18 * d18, 0.5D);

                for (int j = 0; j < 4; j++) {
                    tessellator.startDrawingQuads();
                    tessellator.setNormal((float) (d16 / d19), (float) (d17 / d19), (float) (d18 / d19));
                    tessellator.addVertexWithUV(d1, d2, d3, f2, f3);
                    tessellator.addVertexWithUV(d4, d5, d6, f2, f6);
                    tessellator.addVertexWithUV(d7, d8, d9, f1, f6);
                    tessellator.addVertexWithUV(d1, d2, d3, f2, f3);
                    tessellator.draw();
                    GL11.glTranslatef(0.5F, 0.0F, 0.5F);
                    GL11.glTranslatef((-(x >> 4) * 16), (-(y >> 4) * 16), (-(z >> 4) * 16));
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(((x >> 4) * 16), ((y >> 4) * 16), ((z >> 4) * 16));
                    GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
                }


                GL11.glPopMatrix();
                GL11.glEnable(2884);
                GL11.glDisable(32826);
                GL11.glDisable(3042);

                tessellator.startDrawingQuads();

                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                return true;
            }
            IIcon iicon = renderer.getBlockIcon(CBlocks.blockLaserReflector);

            float f14 = iicon.getMinU();
            float f15 = iicon.getMaxU();
            float f4 = iicon.getMinV();
            float f5 = iicon.getMaxV();
            float f = 0.125F;

            TessellatorHelper.pushMatrix();

            TessellatorHelper.translate(x + 0.5D, y + 0.5D, z + 0.5D);
            switch (direction) {
                case UP:
                    TessellatorHelper.rotate(0.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case DOWN:
                    TessellatorHelper.rotate(180.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    TessellatorHelper.rotate(-90.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    TessellatorHelper.rotate(90.0D, 1.0D, 0.0D, 0.0D);
                    break;
                case WEST:
                    TessellatorHelper.rotate(-90.0D, 0.0D, 0.0D, -1.0D);
                    break;
                case EAST:
                    TessellatorHelper.rotate(90.0D, 0.0D, 0.0D, -1.0D);
                    break;
            }


            TessellatorHelper.translate(-0.5D, -0.5D, -0.5D);

            TessellatorHelper.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            TessellatorHelper.setColorRGBA(255, 255, 255, 225);
            TessellatorHelper.setNormal(0.0F, -1.0F, 0.0F);
            TessellatorHelper.addVertexWithUV(0.0D + (f * 2.0F), 0.0D + f, 0.0D + (f * 2.0F), f14, f4);
            TessellatorHelper.addVertexWithUV(1.0D - (f * 2.0F), 0.0D + f, 0.0D + (f * 2.0F), f15, f4);
            TessellatorHelper.addVertexWithUV(1.0D - (f * 2.0F), 0.0D + f, 1.0D - (f * 2.0F), f15, f5);
            TessellatorHelper.addVertexWithUV(0.0D + (f * 2.0F), 0.0D + f, 1.0D - (f * 2.0F), f14, f5);


            double x1 = 0.5D, y1 = 1.0D - f, z1 = 0.5D;
            double x2 = 1.0D - (f * 2.0F), y2 = 0.0D + f, z2 = 0.0D + (f * 2.0F);
            double x3 = 0.0D + (f * 2.0F), y3 = 0.0D + f, z3 = 0.0D + (f * 2.0F);

            double xa = x2 - x1, ya = y2 - y1, za = z2 - z1;
            double xb = x3 - x1, yb = y3 - y1, zb = z3 - z1;
            double xc = ya * zb - yb * za, yc = za * xb - zb * xa, zc = xa * yb - xb * ya, lc = Math.pow(xc * xc + yc * yc + zc * zc, 0.5D);

            for (int i = 0; i < 4; i++) {
                TessellatorHelper.setNormal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc));
                TessellatorHelper.addVertexWithUV(x1, y1, z1, f15, f4);
                TessellatorHelper.addVertexWithUV(x2, y2, z2, f15, f5);
                TessellatorHelper.addVertexWithUV(x3, y3, z3, f14, f5);
                TessellatorHelper.addVertexWithUV(x1, y1, z1, f15, f4);


                TessellatorHelper.translate(0.5D, 0.0D, 0.5D);
                TessellatorHelper.rotate(90.0D, 0.0D, 1.0D, 0.0D);
                TessellatorHelper.translate(-0.5D, 0.0D, -0.5D);
            }


            TessellatorHelper.popMatrix();

            TessellatorHelper.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            return true;
        }

        return false;
    }


    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }


    public int getRenderId() {
        return ClayiumCore.laserReflectorRenderId;
    }
}
