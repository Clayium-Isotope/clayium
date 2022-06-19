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
    @Override
    public void render(TileEntityClayContainer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        ModelClayContainer modelCC = new ModelClayContainer(buffer);

        // TODO render breaking progress

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

        if (!ClayContainer.ClayContainerState.renderAsPipe(getWorld(), te.getPos())) {
            modelCC.renderBlocked();

            PositionTextureVertex ptv0 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv1 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 1.0d), 0f, 0f);
            PositionTextureVertex ptv2 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv3 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 1.0d), 0f, 0f);
            PositionTextureVertex ptv4 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv5 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 1.0d), 0f, 0f);
            PositionTextureVertex ptv6 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 0.0d), 0f, 0f);
            PositionTextureVertex ptv7 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 1.0d), 0f, 0f);

            if (bindImportBlockTexAsCan(te.insertRoutes, EnumFacing.DOWN))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv2, ptv3, ptv7}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.extractRoutes, EnumFacing.DOWN))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv2, ptv3, ptv7}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.insertRoutes, EnumFacing.UP))
                new TexturedQuad(new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.extractRoutes, EnumFacing.UP))
                new TexturedQuad(new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.insertRoutes, EnumFacing.SOUTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv2, ptv6, ptv4, ptv0}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.extractRoutes, EnumFacing.SOUTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv2, ptv6, ptv4, ptv0}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.insertRoutes, EnumFacing.NORTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv7, ptv3, ptv1, ptv5}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.extractRoutes, EnumFacing.NORTH))
                new TexturedQuad(new PositionTextureVertex[] {ptv7, ptv3, ptv1, ptv5}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.insertRoutes, EnumFacing.WEST))
                new TexturedQuad(new PositionTextureVertex[] {ptv3, ptv2, ptv0, ptv1}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.extractRoutes, EnumFacing.WEST))
                new TexturedQuad(new PositionTextureVertex[] {ptv3, ptv2, ptv0, ptv1}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);

            if (bindImportBlockTexAsCan(te.insertRoutes, EnumFacing.EAST))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv7, ptv5, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
            if (bindExportBlockTexAsCan(te.extractRoutes, EnumFacing.EAST))
                new TexturedQuad(new PositionTextureVertex[] {ptv6, ptv7, ptv5, ptv4}, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);


        } else {
            modelCC.renderPiped(te.getBlockState(), buffer);

            if (rendererDispatcher.entity instanceof EntityPlayer
                    && ClayiumItems.hasPipingTools((EntityPlayer) rendererDispatcher.entity)) {
                if (te.getBlockState().isTheFacingActivated(EnumFacing.DOWN)) {
                    if (bindImportPipeTexAsCan(te.insertRoutes, EnumFacing.DOWN))
                        modelCC.renderPipeDown(buffer);
                    if (bindExportPipeTexAsCan(te.extractRoutes, EnumFacing.DOWN))
                        modelCC.renderPipeDown(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.UP)) {
                    if (bindImportPipeTexAsCan(te.insertRoutes, EnumFacing.UP))
                        modelCC.renderPipeUp(buffer);
                    if (bindExportPipeTexAsCan(te.extractRoutes, EnumFacing.UP))
                        modelCC.renderPipeUp(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.NORTH)) {
                    if (bindImportPipeTexAsCan(te.insertRoutes, EnumFacing.NORTH))
                        modelCC.renderPipeNorth(buffer);
                    if (bindExportPipeTexAsCan(te.extractRoutes, EnumFacing.NORTH))
                        modelCC.renderPipeNorth(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.SOUTH)) {
                    if (bindImportPipeTexAsCan(te.insertRoutes, EnumFacing.SOUTH))
                        modelCC.renderPipeSouth(buffer);
                    if (bindExportPipeTexAsCan(te.extractRoutes, EnumFacing.SOUTH))
                        modelCC.renderPipeSouth(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.WEST)) {
                    if (bindImportPipeTexAsCan(te.insertRoutes, EnumFacing.WEST))
                        modelCC.renderPipeWest(buffer);
                    if (bindExportPipeTexAsCan(te.extractRoutes, EnumFacing.WEST))
                        modelCC.renderPipeWest(buffer);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.EAST)) {
                    if (bindImportPipeTexAsCan(te.insertRoutes, EnumFacing.EAST))
                        modelCC.renderPipeEast(buffer);
                    if (bindExportPipeTexAsCan(te.extractRoutes, EnumFacing.EAST))
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
