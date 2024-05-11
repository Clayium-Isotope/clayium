package mods.clayium.client.render;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Deprecated
public class TEISRLaserReflector extends TileEntityItemStackRenderer {

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        EnumFacing direction = EnumFacing.SOUTH;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        TileEntityRendererDispatcher.instance.renderEngine
                .bindTexture(new ResourceLocation(ClayiumCore.ModId, "laser_reflector"));
        float f14 = 0;
        float f15 = 16;
        float f4 = 0;
        float f5 = 16;
        float f = 0.125F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5D, -0.5D, -0.5D);
        GlStateManager.scale(1.2D, 1.2D, 1.2D);
        GlStateManager.translate(0.5D, 0.5D, 0.5D);
        switch (direction) {
            case UP:
                GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
                break;
            case DOWN:
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, -1.0F);
                break;
            case EAST:
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, -1.0F);
        }

        GlStateManager.translate(-0.5D, -0.5D, -0.5D);
        builder.pos(0.0D + (double) (f * 2.0F), 0.0D + (double) f, 0.0D + (double) (f * 2.0F)).tex(f14, f4)
                .normal(0.0F, -1.0F, 0.0F).color(1.0f, 1.0f, 1.0f, 1.0f);
        builder.pos(1.0D - (double) (f * 2.0F), 0.0D + (double) f, 0.0D + (double) (f * 2.0F)).tex(f15, f4)
                .normal(0.0F, -1.0F, 0.0F).color(1.0f, 1.0f, 1.0f, 1.0f);
        builder.pos(1.0D - (double) (f * 2.0F), 0.0D + (double) f, 1.0D - (double) (f * 2.0F)).tex(f15, f5)
                .normal(0.0F, -1.0F, 0.0F).color(1.0f, 1.0f, 1.0f, 1.0f);
        builder.pos(0.0D + (double) (f * 2.0F), 0.0D + (double) f, 1.0D - (double) (f * 2.0F)).tex(f14, f5)
                .normal(0.0F, -1.0F, 0.0F).color(1.0f, 1.0f, 1.0f, 1.0f);
        double x1 = 0.5D;
        double y1 = 1.0D - (double) f;
        double z1 = 0.5D;
        double x2 = 1.0D - (double) (f * 2.0F);
        double y2 = 0.0D + (double) f;
        double z2 = 0.0D + (double) (f * 2.0F);
        double x3 = 0.0D + (double) (f * 2.0F);
        double y3 = 0.0D + (double) f;
        double z3 = 0.0D + (double) (f * 2.0F);
        double xa = x2 - x1;
        double ya = y2 - y1;
        double za = z2 - z1;
        double xb = x3 - x1;
        double yb = y3 - y1;
        double zb = z3 - z1;
        double xc = ya * zb - yb * za;
        double yc = za * xb - zb * xa;
        double zc = xa * yb - xb * ya;
        double lc = Math.pow(xc * xc + yc * yc + zc * zc, 0.5D);

        for (int i = 0; i < 4; ++i) {
            builder.pos(x1, y1, z1).tex(f15, f4).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc))
                    .color(1.0f, 1.0f, 1.0f, 1.0f);
            builder.pos(x2, y2, z2).tex(f15, f5).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc))
                    .color(1.0f, 1.0f, 1.0f, 1.0f);
            builder.pos(x3, y3, z3).tex(f14, f5).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc))
                    .color(1.0f, 1.0f, 1.0f, 1.0f);
            builder.pos(x1, y1, z1).tex(f15, f4).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc))
                    .color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.translate(0.5D, 0.0D, 0.5D);
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5D, 0.0D, -0.5D);
        }

        tessellator.draw();
        GlStateManager.popMatrix();
    }
}
