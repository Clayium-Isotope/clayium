package mods.clayium.client.render;

import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.LaserReflector.LaserReflector;
import mods.clayium.machine.LaserReflector.TileEntityLaserReflector;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class TESRLaserReflector extends TileEntitySpecialRenderer<TileEntityLaserReflector> {
    @Override
    public void render(TileEntityLaserReflector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing direction = te.getWorld().getBlockState(te.getPos()).getValue(LaserReflector.FACING);
        if (te.getDirection() != null) {
            direction = te.getDirection();
        }

        if (direction == null) return;

        this.bindTexture(EnumMachineKind.laserReflector.getFaceResource());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        switch (direction) {
            case UP:
                GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
                break;
            case DOWN:
                GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
                break;
            case NORTH:
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                break;
            case SOUTH:
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                break;
            case WEST:
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                break;
            case EAST:
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.translate(-0.5D, -0.5D, -0.5D);

        float f5 = 0.125f;
        float a = 1.0f; // 225.0f / 255.0f;

        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        builder.pos(0.0d + (double) (f5 * 2.0F), 0.0D + (double) f5, 0.0D + (double) (f5 * 2.0F)).tex(0.0d, 0.0d).color(1.0f, 1.0f, 1.0f, a).normal(0.0f, -1.0f, 0.0f).endVertex();
        builder.pos(1.0d - (double) (f5 * 2.0F), 0.0D + (double) f5, 0.0D + (double) (f5 * 2.0F)).tex(1.0d, 0.0d).color(1.0f, 1.0f, 1.0f, a).normal(0.0f, -1.0f, 0.0f).endVertex();
        builder.pos(1.0d - (double) (f5 * 2.0F), 0.0D + (double) f5, 1.0d - (double) (f5 * 2.0F)).tex(1.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal(0.0f, -1.0f, 0.0f).endVertex();
        builder.pos(0.0d + (double) (f5 * 2.0F), 0.0D + (double) f5, 1.0d - (double) (f5 * 2.0F)).tex(0.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal(0.0f, -1.0f, 0.0f).endVertex();

        tessellator.draw();

        double x1 = 0.5D;
        double y1 = 1.0D - (double) f5;
        double z1 = 0.5D;
        double x2 = 1.0D - (double) (f5 * 2.0F);
        double y2 = 0.0D + (double) f5;
        double z2 = 0.0D + (double) (f5 * 2.0F);
        double x3 = 0.0D + (double) (f5 * 2.0F);
        double y3 = 0.0D + (double) f5;
        double z3 = 0.0D + (double) (f5 * 2.0F);
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

        builder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        builder.pos(x1, y1, z1).tex(1.0d, 0.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D + (double) (f5 * 2.0F), y2, 0.5D + (double) (f5 * 2.0F)).tex(1.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D + (double) (f5 * 2.0F), y3, 0.5D - (double) (f5 * 2.0F)).tex(0.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();

        builder.pos(x1, y1, z1).tex(1.0d, 0.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D + (double) (f5 * 2.0F), y2, 0.5D - (double) (f5 * 2.0F)).tex(1.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D - (double) (f5 * 2.0F), y3, 0.5D - (double) (f5 * 2.0F)).tex(0.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();

        builder.pos(x1, y1, z1).tex(1.0d, 0.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D - (double) (f5 * 2.0F), y2, 0.5D - (double) (f5 * 2.0F)).tex(1.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D - (double) (f5 * 2.0F), y3, 0.5D + (double) (f5 * 2.0F)).tex(0.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();

        builder.pos(x1, y1, z1).tex(1.0d, 0.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D - (double) (f5 * 2.0F), y2, 0.5D + (double) (f5 * 2.0F)).tex(1.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();
        builder.pos(0.5D + (double) (f5 * 2.0F), y3, 0.5D + (double) (f5 * 2.0F)).tex(0.0d, 1.0d).color(1.0f, 1.0f, 1.0f, a).normal((float) (xc / lc), (float) (yc / lc), (float) (zc / lc)).endVertex();

        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        RenderClayLaser.render(te, x, y, z);
    }

    @Override
    public boolean isGlobalRenderer(TileEntityLaserReflector te) {
        return true;
    }
}
