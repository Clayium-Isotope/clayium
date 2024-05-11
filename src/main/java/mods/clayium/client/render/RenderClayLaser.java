package mods.clayium.client.render;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderClayLaser {

    public static void render(IClayLaserManager manager, double x, double y, double z) {
        if (!manager.isIrradiating()) return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.disableFog();

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);

        TileEntityRendererDispatcher.instance.renderEngine
                .bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/laser.png"));

        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        switch (manager.getDirection()) {
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
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                break;
            case EAST:
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
        }

        ClayLaser laser = manager.getClayLaser();
        int max = Math.max(Math.max(laser.numbers.x, laser.numbers.y), laser.numbers.z);
        float scale = 1.0F - 14.0625F / (float) (laser.numbers.x + laser.numbers.y + laser.numbers.z + 14);
        GlStateManager.translate(0.0F, -scale / 6.0F, 0.0F);
        GlStateManager.scale(scale, (float) manager.getLaserLength() + scale / 3.0F, scale);
        int k = ClayiumConfiguration.cfgLaserQuality;

        int r = (int) (0xff * ((float) laser.numbers.z / max));
        int g = (int) (0xff * ((float) laser.numbers.y / max));
        int b = (int) (0xff * ((float) laser.numbers.x / max));
        int a = (int) (26.0F + scale * 26.0F) * 8 / k;

        double factor = 1.0D;
        for (int idx = 0; idx < k; ++idx) {
            double sin = Math.sin(180.0F / k * idx);
            double cos = Math.cos(180.0F / k * idx);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(factor * sin, 1.0D, factor * cos).tex(0.0F, 0.0F).color(r, g, b, a).endVertex();
            buffer.pos(-factor * sin, 1.0D, -factor * cos).tex(1.0F, 0.0F).color(r, g, b, a).endVertex();
            buffer.pos(-factor * sin, 0.0D, -factor * cos).tex(1.0F, 1.0F).color(r, g, b, a).endVertex();
            buffer.pos(factor * sin, 0.0D, factor * cos).tex(0.0F, 1.0F).color(r, g, b, a).endVertex();
            tessellator.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);

        GlStateManager.enableFog();
        GlStateManager.popMatrix();
    }
}
