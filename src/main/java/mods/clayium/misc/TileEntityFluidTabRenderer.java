package mods.clayium.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class TileEntityFluidTabRenderer
        extends TileEntitySpecialRenderer {
    public void renderTileEntityCupAt(TileFluidTab par1Tile, double par2, double par4, double par6, float par8) {
        setRotation(par1Tile, (float) par2, (float) par4, (float) par6);
    }


    public void setTileEntityRenderer(TileEntityRendererDispatcher par1TileEntityRenderer) {
        func_147497_a(par1TileEntityRenderer);
    }


    public void setRotation(TileFluidTab par0Tile, float par1, float par2, float par3) {
        Tessellator tessellator = Tessellator.instance;

        if (par0Tile.getFluidIcon() != null) {


            GL11.glPushMatrix();
            GL11.glEnable(32826);


            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
            GL11.glTranslatef(par1, par2 + 0.5F, par3);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

            IIcon iicon = par0Tile.getFluidIcon();
            float f14 = iicon.getMinU();
            float f15 = iicon.getMaxU();
            float f4 = iicon.getMinV();
            float f5 = iicon.getMaxV();

            bindTexture(TextureMap.locationBlocksTexture);

            float f = 0.0625F;
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            tessellator.addVertexWithUV(0.0D + f, -0.4D, -1.0D + f, f15, f4);
            tessellator.addVertexWithUV(1.0D - f, -0.4D, -1.0D + f, f14, f4);
            tessellator.addVertexWithUV(1.0D - f, -0.4D, 0.0D - f, f14, f5);
            tessellator.addVertexWithUV(0.0D + f, -0.4D, 0.0D - f, f15, f5);
            tessellator.draw();

            GL11.glDisable(32826);

            GL11.glPopMatrix();
        }
    }


    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
        renderTileEntityCupAt((TileFluidTab) par1TileEntity, par2, par4, par6, par8);
    }
}
