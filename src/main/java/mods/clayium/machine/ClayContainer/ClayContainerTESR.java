package mods.clayium.machine.ClayContainer;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClayContainerTESR extends TileEntitySpecialRenderer<TileEntityClayContainer> {
    private final BufferBuilder buffer = Tessellator.getInstance().getBuffer();

    @Override
    public void render(TileEntityClayContainer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.isInvalid()) return;

        ModelClayContainer modelCC = new ModelClayContainer(buffer);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        } else {
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/machinehull-3.png"));
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y + 1.0d, z + 1.0d);
        GlStateManager.scale(1.0d, -1.0d, -1.0d);

        if (!ClayContainer.ClayContainerState.renderAsPipe(te)) {
            modelCC.renderBlocked();

            PositionTextureVertex ptv0 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv1 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 1.0d), 0f, 0f);
            PositionTextureVertex ptv2 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv3 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 1.0d), 0f, 0f);
            PositionTextureVertex ptv4 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv5 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 1.0d), 0f, 0f);
            PositionTextureVertex ptv6 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv7 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 1.0d), 0f, 0f);

            if (bindImportBlockTexAsCan(te.importRoutes, EnumFacing.DOWN))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv2, ptv3, ptv7}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.DOWN))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv2, ptv3, ptv7}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.importRoutes, EnumFacing.UP))
                new TexturedQuad(new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.UP))
                new TexturedQuad(new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.importRoutes, EnumFacing.SOUTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv2, ptv6, ptv4, ptv0}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.SOUTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv2, ptv6, ptv4, ptv0}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.importRoutes, EnumFacing.NORTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv7, ptv3, ptv1, ptv5}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.NORTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv7, ptv3, ptv1, ptv5}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.importRoutes, EnumFacing.WEST))
                new TexturedQuad(new PositionTextureVertex[] {ptv3, ptv2, ptv0, ptv1}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.WEST))
                new TexturedQuad(new PositionTextureVertex[] {ptv3, ptv2, ptv0, ptv1}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.importRoutes, EnumFacing.EAST))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv7, ptv5, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.EAST))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv7, ptv5, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);


        } else {
            modelCC.renderPiped(te.getBlockState(), buffer);

            if (rendererDispatcher.entity instanceof EntityPlayer
                    && ClayiumItems.hasPipingTools((EntityPlayer) rendererDispatcher.entity)) {
                if (te.getBlockState().isTheFacingActivated(EnumFacing.DOWN)) {
                    if (bindImportPipeTexAsCan(te.importRoutes, EnumFacing.DOWN))
                        modelCC.renderPipeDown(buffer);
                    if (bindExportPipeTexAsCan(te.exportRoutes, EnumFacing.DOWN))
                        modelCC.renderPipeDown(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.UP)) {
                    if (bindImportPipeTexAsCan(te.importRoutes, EnumFacing.UP))
                        modelCC.renderPipeUp(buffer);
                    if (bindExportPipeTexAsCan(te.exportRoutes, EnumFacing.UP))
                        modelCC.renderPipeUp(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.NORTH)) {
                    if (bindImportPipeTexAsCan(te.importRoutes, EnumFacing.NORTH))
                        modelCC.renderPipeNorth(buffer);
                    if (bindExportPipeTexAsCan(te.exportRoutes, EnumFacing.NORTH))
                        modelCC.renderPipeNorth(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.SOUTH)) {
                    if (bindImportPipeTexAsCan(te.importRoutes, EnumFacing.SOUTH))
                        modelCC.renderPipeSouth(buffer);
                    if (bindExportPipeTexAsCan(te.exportRoutes, EnumFacing.SOUTH))
                        modelCC.renderPipeSouth(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.WEST)) {
                    if (bindImportPipeTexAsCan(te.importRoutes, EnumFacing.WEST))
                        modelCC.renderPipeWest(buffer);
                    if (bindExportPipeTexAsCan(te.exportRoutes, EnumFacing.WEST))
                        modelCC.renderPipeWest(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.EAST)) {
                    if (bindImportPipeTexAsCan(te.importRoutes, EnumFacing.EAST))
                        modelCC.renderPipeEast(buffer);
                    if (bindExportPipeTexAsCan(te.exportRoutes, EnumFacing.EAST))
                        modelCC.renderPipeEast(buffer);
                }
            }
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        }
    }

    private boolean bindImportBlockTexAsCan(Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return false;

        if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_" + routes.get(facing) + ".png"));

        return true;
    }

    private boolean bindExportBlockTexAsCan(Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return false;

        if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export_" + routes.get(facing) + ".png"));

        return true;
    }

    private boolean bindImportPipeTexAsCan(Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return false;

        if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_p.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_" + routes.get(facing) + "_p.png"));

        return true;
    }

    private boolean bindExportPipeTexAsCan(Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return false;

        if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export_p.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export_" + routes.get(facing) + "_p.png"));

        return true;
    }
}
