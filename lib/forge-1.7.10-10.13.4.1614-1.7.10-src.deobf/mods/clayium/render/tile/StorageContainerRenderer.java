package mods.clayium.render.tile;

import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class StorageContainerRenderer
        extends TileEntitySpecialRenderer {
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicktime) {
        if (tile != null && tile instanceof TileStorageContainer) {
            ItemStack itemstack = ((TileStorageContainer) tile).containerItemStacks[0];
            int stackSize = 0;
            if (itemstack == null) {
                itemstack = ((TileStorageContainer) tile).containerItemStacks[1];
            } else {
                stackSize = itemstack.stackSize;
            }

            if (itemstack != null) {

                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

                UtilDirection direction = UtilDirection.getOrientation(((TileStorageContainer) tile).getFrontDirection());
                switch (direction) {
                    case NORTH:
                        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case SOUTH:
                        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case WEST:
                        GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case EAST:
                        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                }


                if (MinecraftForgeClient.getRenderPass() == 0) {
                    EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0D, 0.0D, 0.0D, itemstack.copy());
                    (entityitem.getEntityItem()).stackSize = 1;
                    entityitem.hoverStart = 0.0F;


                    GL11.glPushMatrix();
                    GL11.glTranslatef(0.0F, -0.03F, 0.51F);
                    RenderItem.renderInFrame = true;
                    RenderManager.instance.renderEntityWithPosYaw((Entity) entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                    RenderItem.renderInFrame = false;
                    GL11.glPopMatrix();
                }


                if (MinecraftForgeClient.getRenderPass() == 1) {
                    String s = String.valueOf(UtilLocale.StackSizeNumeral(stackSize));

                    FontRenderer fontrenderer = RenderManager.instance.getFontRenderer();
                    GL11.glPushMatrix();

                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, -0.15F, -0.55F);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);


                    float f = 1.6F;
                    float f1 = 0.016666668F * f;
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(2896);


                    GL11.glTranslatef(0.0F, 0.0F, -0.01F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, -603979776);

                    MovingObjectPosition p = (Minecraft.getMinecraft()).renderViewEntity.rayTrace(9999.0D, 0.0F);
                    boolean b = (p.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && p.blockX == tile.xCoord && p.blockY == tile.yCoord && p.blockZ == tile.zCoord);
                    if (ClayiumCore.proxy.renderAsPipingMode() || b) {
                        GL11.glTranslatef(0.0F, -20.0F, -5.0F);
                        f1 = 0.5F;
                        GL11.glScalef(f1, f1, f1);
                        s = itemstack.getDisplayName();

                        int i = fontrenderer.getStringWidth(s);
                        float f2 = (i > 64) ? (64.0F / i) : 1.0F;
                        GL11.glScalef(f2, 1.0F, 1.0F);


                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glEnable(32823);
                        GL11.glPolygonOffset(0.1F, 1.0F);

                        Tessellator tessellator = Tessellator.instance;
                        GL11.glDisable(3553);
                        tessellator.startDrawingQuads();
                        i /= 2;
                        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
                        tessellator.addVertex((-i - 1), -1.0D, 0.0D);
                        tessellator.addVertex((-i - 1), 8.0D, 0.0D);
                        tessellator.addVertex((i + 1), 8.0D, 0.0D);
                        tessellator.addVertex((i + 1), -1.0D, 0.0D);
                        tessellator.draw();

                        GL11.glDisable(32823);
                        GL11.glEnable(3553);

                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, -1);
                    }


                    GL11.glEnable(2896);

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
            }
        }
    }
}
