package mods.clayium.client.render;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayMarker.BlockStateClayMarker;
import mods.clayium.machine.ClayMarker.TileEntityClayMarker;
import mods.clayium.util.UtilRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class TESRClayMarker extends TileEntitySpecialRenderer<TileEntityClayMarker> {
    public static final TESRClayMarker instance = new TESRClayMarker();
    private static final double[] ds1 = new double[]{0.1875, -0.1875};
    private static final double[] ds2 = new double[]{0.125, -0.125};

    @Override
    public void render(TileEntityClayMarker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//        UtilRender.setLightValue(15, 15);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        IBlockState state = te.getWorld().getBlockState(te.getPos());

        GlStateManager.pushMatrix();
//                GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
//                GlStateManager.disableCull();
        GlStateManager.disableTexture2D();

        switch (state.getValue(BlockStateClayMarker.APPEARANCE)) {
            case _1:
                GlStateManager.glLineWidth(3.0f);

                builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
//                for (double d1 : ds1) {
//                    for (double d2 : ds1) {
//                        builder.pos(x + 0.0 + (double) TileEntityClayMarker.maxRange, y + 0.5 + d1, z + 0.5 + d2).color(0.1f, 0.5f, 0.2f, 0.3f).endVertex();
//                        builder.pos(x + 1.0 - (double) TileEntityClayMarker.maxRange, y + 0.5 + d1, z + 0.5 + d2).color(0.1f, 0.5f, 0.2f, 0.3f).endVertex();
//                        builder.pos(x + 0.5 + d1, y + 0.0 + (double) TileEntityClayMarker.maxRange, z + 0.5 + d2).color(0.1f, 0.5f, 0.2f, 0.3f).endVertex();
//                        builder.pos(x + 0.5 + d1, y + 1.0 - (double) TileEntityClayMarker.maxRange, z + 0.5 + d2).color(0.1f, 0.5f, 0.2f, 0.3f).endVertex();
//                        builder.pos(x + 0.5 + d1, y + 0.5 + d2, z + 0.0 + (double) TileEntityClayMarker.maxRange).color(0.1f, 0.5f, 0.2f, 0.3f).endVertex();
//                        builder.pos(x + 0.5 + d1, y + 0.5 + d2, z + 1.0 - (double) TileEntityClayMarker.maxRange).color(0.1f, 0.5f, 0.2f, 0.3f).endVertex();
//                    }
//                }
////                builder.finishDrawing();
//                tessellator.draw();
//
//                builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                for (double d1 : ds1) {
                    for (double d2 : ds1) {
                        builder.pos(x + 0.0 + (double) TileEntityClayMarker.maxRange, y + 0.5 + d1, z + 0.5 + d2).color(0.2F, 0.7F, 0.3F, 1.0F).endVertex();
                        builder.pos(x + 1.0 - (double) TileEntityClayMarker.maxRange, y + 0.5 + d1, z + 0.5 + d2).color(0.2F, 0.7F, 0.3F, 1.0F).endVertex();
                        builder.pos(x + 0.5 + d1, y + 0.0 + (double) TileEntityClayMarker.maxRange, z + 0.5 + d2).color(0.2F, 0.7F, 0.3F, 1.0F).endVertex();
                        builder.pos(x + 0.5 + d1, y + 1.0 - (double) TileEntityClayMarker.maxRange, z + 0.5 + d2).color(0.2F, 0.7F, 0.3F, 1.0F).endVertex();
                        builder.pos(x + 0.5 + d1, y + 0.5 + d2, z + 0.0 + (double) TileEntityClayMarker.maxRange).color(0.2F, 0.7F, 0.3F, 1.0F).endVertex();
                        builder.pos(x + 0.5 + d1, y + 0.5 + d2, z + 1.0 - (double) TileEntityClayMarker.maxRange).color(0.2F, 0.7F, 0.3F, 1.0F).endVertex();
                    }
                }
//                builder.finishDrawing();
                tessellator.draw();
//                UtilRender.renderBox(state.getBoundingBox(te.getWorld(), te.getPos()).offset(x, y, z), AABBHolder.Appearance._1, new Color(1.0f, 0.0f, 0.0f, 0.3f), true, rendererDispatcher);
                UtilRender.renderBoldAABB(state.getBoundingBox(te.getWorld(), te.getPos()).offset(x, y, z), new Color(1.0f, 0.0f, 0.0f, 1.0f), true);

                break;
            case _2:
            case _3:
            case _4:
                if (te.hasAxisAlignedBB()) {
                    ClayiumCore.logger.info(te.getAxisAlignedBB());
                    UtilRender.renderBoldAABB(te.getAxisAlignedBB().offset(BlockPos.ORIGIN.subtract(te.getPos())).offset(x, y, z), new Color(0.1f, 0.1f, 0.7f, 1.0f), true);
                }
                UtilRender.renderBoldAABB(state.getBoundingBox(te.getWorld(), te.getPos()).offset(x, y, z), new Color(1.0f, 0.0f, 0.0f, 1.0f), true);


//                UtilRender.renderAxisAlignedBB(te, new Color(0.1f, 0.1f, 0.7f, 0.3f), true, this.rendererDispatcher);
//                UtilRender.renderBox(state.getBoundingBox(te.getWorld(), te.getPos()).offset(x, y, z), te.getBoxAppearance(), new Color(1.0f, 0.0f, 0.0f, 0.3f), true, rendererDispatcher);
        }

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
//                GlStateManager.enableCull();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.glLineWidth(1.0f);
        GlStateManager.enableDepth();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
    }
}
