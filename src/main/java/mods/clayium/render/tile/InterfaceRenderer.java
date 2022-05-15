package mods.clayium.render.tile;

import mods.clayium.block.tile.ISynchronizedInterface;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilRender;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;


public class InterfaceRenderer
        extends TileEntitySpecialRenderer {
    private int count = 0;


    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicktime) {
        if (tile != null && tile instanceof ISynchronizedInterface && ((ISynchronizedInterface) tile).isSynced() && ((ISynchronizedInterface) tile).acceptCoordChanger()) {
            MovingObjectPosition p = (Minecraft.getMinecraft()).renderViewEntity.rayTrace(9999.0D, 0.0F);
            boolean b = (p.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && p.blockX == tile.xCoord && p.blockY == tile.yCoord && p.blockZ == tile.zCoord);
            if (b) {
                ISynchronizedInterface in = (ISynchronizedInterface) tile;
                World cworld = in.getCoreWorld();
                if (cworld != null) {
                    int cx = tile.xCoord + in.getCoreBlockXCoord();
                    int cy = tile.yCoord + in.getCoreBlockYCoord();
                    int cz = tile.zCoord + in.getCoreBlockZCoord();
                    Block cblock = cworld.getBlock(tile.xCoord + in.getCoreBlockXCoord(), tile.yCoord + in.getCoreBlockYCoord(), tile.zCoord + in.getCoreBlockZCoord());
                    if (cblock != null) {
                        ItemStack itemstack = UtilBuilder.getItemBlock(cworld, cx, cy, cz);

                        float ticktime = (float) tile.getWorldObj().getWorldTime() + partialTicktime;

                        GL11.glPushMatrix();
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
                        float f = 0.8F;
                        ForgeDirection direction = ForgeDirection.getOrientation(p.sideHit);
                        switch (direction) {
                            case UP:
                                GL11.glTranslatef(0.0F, f, 0.0F);
                                break;
                            case DOWN:
                                GL11.glTranslatef(0.0F, -f, 0.0F);
                                break;
                            case NORTH:
                                GL11.glTranslatef(0.0F, 0.0F, -f);
                                break;
                            case SOUTH:
                                GL11.glTranslatef(0.0F, 0.0F, f);
                                break;
                            case WEST:
                                GL11.glTranslatef(-f, 0.0F, 0.0F);
                                break;
                            case EAST:
                                GL11.glTranslatef(f, 0.0F, 0.0F);
                                break;
                        }


                        GL11.glPushMatrix();
                        GL11.glTranslatef(0.0F, 0.05F, 0.0F);

                        GL11.glRotatef(ticktime * 2.0F, 0.0F, 1.0F, 0.0F);


                        float f1 = 0.25F;
                        GL11.glScalef(f1, f1, f1);
                        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
                        GL11.glEnable(32826);
                        GL11.glAlphaFunc(516, 0.0F);
                        GL11.glEnable(3042);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
                        texturemanager.bindTexture(texturemanager.getResourceLocation(itemstack.getItemSpriteNumber()));
                        RenderBlocks render = RenderBlocks.getInstance();
                        render.useInventoryTint = false;
                        render.renderBlockAsItem(cblock, itemstack.getItemDamage(), 0.0F);
                        GL11.glPopMatrix();

                        GL11.glDisable(2896);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


                        GL11.glEnable(32826);
                        GL11.glAlphaFunc(516, 0.0F);
                        GL11.glEnable(3042);

                        GL11.glPushMatrix();
                        GL11.glTranslatef(0.0F, 0.4F, 0.0F);

                        GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);

                        GL11.glPushMatrix();
                        f1 = 0.01F;
                        GL11.glScalef(f1, f1, f1);

                        GL11.glDisable(2929);
                        drawString(itemstack.getDisplayName());
                        GL11.glEnable(2929);

                        GL11.glPopMatrix();

                        GL11.glTranslatef(0.0F, -0.1F, 0.0F);

                        GL11.glPushMatrix();
                        f1 = 0.005F;
                        GL11.glScalef(f1, f1, f1);

                        GL11.glDisable(2929);
                        drawString("" + cx + "," + cy + "," + cz + ";" + cworld.provider.getDimensionName());
                        GL11.glEnable(2929);

                        GL11.glPopMatrix();

                        GL11.glPopMatrix();
                        GL11.glEnable(2896);

                        GL11.glPopMatrix();


                        if (cworld.provider.dimensionId == (tile.getWorldObj()).provider.dimensionId) {
                            float cr = (float) (Math.sin((ticktime * 0.1F) + 0.0D) + 1.0D) * 0.5F;
                            float cg = (float) (Math.sin((ticktime * 0.1F) + 2.0943951023931953D) + 1.0D) * 0.5F;
                            float cb = (float) (Math.sin((ticktime * 0.1F) + 4.1887902047863905D) + 1.0D) * 0.5F;
                            float cf = (float) (Math.sin((ticktime * 0.12F) + 0.0D) + 1.0D) * 0.15F + 0.2F;
                            UtilRender.renderBox(cx + cblock.getBlockBoundsMinX(), cy + cblock.getBlockBoundsMinY(), cz + cblock.getBlockBoundsMinZ(), cx + cblock
                                    .getBlockBoundsMaxX(), cy + cblock.getBlockBoundsMaxY(), cz + cblock.getBlockBoundsMaxZ(), 2, cr, cg, cb, cf, false);


                            Tessellator tessellator = Tessellator.instance;
                            GL11.glDisable(3553);
                            GL11.glDisable(2929);
                            GL11.glAlphaFunc(516, 0.1F);
                            GL11.glEnable(3042);
                            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                            tessellator.startDrawing(1);
                            tessellator.setColorRGBA_F(cr, cg, cb, cf);
                            tessellator.addVertex(x + 0.5D, y + 0.5D, z + 0.5D);
                            tessellator.addVertex(x + in.getCoreBlockXCoord() + 0.5D, y + in.getCoreBlockYCoord() + 0.5D, z + in.getCoreBlockZCoord() + 0.5D);
                            tessellator.draw();
                            GL11.glEnable(2929);
                            GL11.glEnable(3553);
                        }

                        GL11.glAlphaFunc(516, 0.1F);
                        GL11.glEnable(3042);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    }
                }
            }
        }
    }

    public static void drawString(String s) {
        GL11.glPushMatrix();

        GL11.glScalef(1.0F, -1.0F, 1.0F);

        FontRenderer fontrenderer = RenderManager.instance.getFontRenderer();
        int i = fontrenderer.getStringWidth(s);


        GL11.glEnable(32823);
        GL11.glPolygonOffset(0.1F, 1.0F);


        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        i /= 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
        tessellator.addVertex((-i - 1), -1.0D, 0.0D);
        tessellator.addVertex((-i - 1), 8.0D, 0.0D);
        tessellator.addVertex((i + 1), 8.0D, 0.0D);
        tessellator.addVertex((i + 1), -1.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(32823);

        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, -1);
        GL11.glPopMatrix();
    }
}
