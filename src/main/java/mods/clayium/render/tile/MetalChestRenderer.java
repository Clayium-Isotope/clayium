package mods.clayium.render.tile;

import mods.clayium.block.tile.TileMetalChest;
import mods.clayium.item.CMaterial;
import mods.clayium.item.CMaterials;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class MetalChestRenderer
        extends TileEntitySpecialRenderer {
    public static MetalChestRenderer INSTANCE = new MetalChestRenderer();

    private static final ResourceLocation base = new ResourceLocation("clayium", "textures/entity/metalchest/base.png");
    private static final ResourceLocation dark = new ResourceLocation("clayium", "textures/entity/metalchest/dark.png");
    private static final ResourceLocation light = new ResourceLocation("clayium", "textures/entity/metalchest/light.png");
    private ModelChest field_147510_h = new ModelChest();


    public void renderChest(int metadata, float prevLidAngle, float lidAngle, CMaterial material, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
        if (material == null) material = CMaterials.ALUMINIUM;


        ModelChest modelchest = this.field_147510_h;


        GL11.glPushMatrix();
        GL11.glEnable(32826);

        GL11.glTranslatef((float) p_147500_2_, (float) p_147500_4_ + 1.0F, (float) p_147500_6_ + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        short short1 = 0;

        if (metadata == 2) {
            short1 = 180;
        }

        if (metadata == 3) {
            short1 = 0;
        }

        if (metadata == 4) {
            short1 = 90;
        }

        if (metadata == 5) {
            short1 = -90;
        }

        GL11.glRotatef(short1, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float f1 = prevLidAngle + (lidAngle - prevLidAngle) * p_147500_8_;


        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        modelchest.chestLid.rotateAngleX = -(f1 * 3.1415927F / 2.0F);

        GL11.glDisable(3042);
        bindTexture(base);
        GL11.glColor4f(material.colors[0][0] / 255.0F, material.colors[0][1] / 255.0F, material.colors[0][2] / 255.0F, 1.0F);
        modelchest.renderAll();

        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(false);

        GL11.glEnable(32823);
        GL11.glPolygonOffset(-0.1F, -1.0F);

        bindTexture(dark);
        GL11.glColor4f(material.colors[1][0] / 255.0F, material.colors[1][1] / 255.0F, material.colors[1][2] / 255.0F, 1.0F);
        modelchest.renderAll();

        GL11.glPolygonOffset(-0.2F, -2.0F);
        bindTexture(light);
        GL11.glColor4f(material.colors[2][0] / 255.0F, material.colors[2][1] / 255.0F, material.colors[2][2] / 255.0F, 1.0F);
        modelchest.renderAll();

        GL11.glDepthMask(true);
        GL11.glDisable(32823);
        GL11.glEnable(3008);
        GL11.glDisable(32826);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }


    public void renderTileEntityAt(TileEntity tile, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
        if (tile instanceof TileMetalChest)
            renderChest(
                    tile.hasWorldObj() ? tile
                            .getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord) : 0, ((TileMetalChest) tile).prevLidAngle, ((TileMetalChest) tile).lidAngle, ((TileMetalChest) tile).material, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
    }
}
