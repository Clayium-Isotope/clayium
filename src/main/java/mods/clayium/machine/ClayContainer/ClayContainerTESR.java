package mods.clayium.machine.ClayContainer;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
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

    private final PositionTextureVertex ptv0 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv1 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex ptv2 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv3 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex ptv4 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv5 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex ptv6 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv7 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 1.0d), 0f, 0f);

    @Override
    public void render(TileEntityClayContainer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null || te.isInvalid()) return;

        ModelClayContainer modelCC = new ModelClayContainer(buffer);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        } else {
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/machinehull-" + (te.getTier() - 1) + ".png"));
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y + 1.0d, z + 1.0d);
        GlStateManager.scale(1.0d, -1.0d, -1.0d);

        if (!ClayContainer.ClayContainerState.renderAsPipe(te)) {
            modelCC.renderBlocked();

            if (te.getBlockType() instanceof ClayiumMachine) {
                ResourceLocation face = ((ClayiumMachine) te.getBlockType()).getMachineKind().getFaceResource();
                if (face != null) {
                    this.bindTexture(face);
                    pasteBoundTexToBlockFace(te.getBlockState().getValue(ClaySidedContainer.ClaySidedContainerState.FACING));
                }
            }

            bindImportBlockTexAsCan(te.importRoutes, EnumFacing.DOWN);
            bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.DOWN);

            bindImportBlockTexAsCan(te.importRoutes, EnumFacing.UP);
            bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.UP);

            bindImportBlockTexAsCan(te.importRoutes, EnumFacing.SOUTH);
            bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.SOUTH);

            bindImportBlockTexAsCan(te.importRoutes, EnumFacing.NORTH);
            bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.NORTH);

            bindImportBlockTexAsCan(te.importRoutes, EnumFacing.WEST);
            bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.WEST);

            bindImportBlockTexAsCan(te.importRoutes, EnumFacing.EAST);
            bindExportBlockTexAsCan(te.exportRoutes, EnumFacing.EAST);

        } else {
            modelCC.renderPiped(te.getBlockState(), buffer);

            if (rendererDispatcher.entity instanceof EntityPlayer
                    && ClayiumItems.hasPipingTools((EntityPlayer) rendererDispatcher.entity)) {
                if (te.getBlockState().isTheFacingActivated(EnumFacing.DOWN)) {
                    bindImportPipeTexAsCan(modelCC, te.importRoutes, EnumFacing.DOWN);
                    bindExportPipeTexAsCan(modelCC, te.exportRoutes, EnumFacing.DOWN);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.UP)) {
                    bindImportPipeTexAsCan(modelCC, te.importRoutes, EnumFacing.UP);
                    bindExportPipeTexAsCan(modelCC, te.exportRoutes, EnumFacing.UP);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.NORTH)) {
                    bindImportPipeTexAsCan(modelCC, te.importRoutes, EnumFacing.NORTH);
                    bindExportPipeTexAsCan(modelCC, te.exportRoutes, EnumFacing.NORTH);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.SOUTH)) {
                    bindImportPipeTexAsCan(modelCC, te.importRoutes, EnumFacing.SOUTH);
                    bindExportPipeTexAsCan(modelCC, te.exportRoutes, EnumFacing.SOUTH);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.WEST)) {
                    bindImportPipeTexAsCan(modelCC, te.importRoutes, EnumFacing.WEST);
                    bindExportPipeTexAsCan(modelCC, te.exportRoutes, EnumFacing.WEST);
                }

                if (te.getBlockState().isTheFacingActivated(EnumFacing.EAST)) {
                    bindImportPipeTexAsCan(modelCC, te.importRoutes, EnumFacing.EAST);
                    bindExportPipeTexAsCan(modelCC, te.exportRoutes, EnumFacing.EAST);
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

    private void bindImportBlockTexAsCan(Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) == -2)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_energy.png"));
        else if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_" + routes.get(facing) + ".png"));

        pasteBoundTexToBlockFace(facing);
    }

    private void bindExportBlockTexAsCan(Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export_" + routes.get(facing) + ".png"));

        pasteBoundTexToBlockFace(facing);
    }

    private void bindImportPipeTexAsCan(ModelClayContainer modelCC, Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) == -2)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_energy_p.png"));
        else if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_p.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_" + routes.get(facing) + "_p.png"));

        pasteBoundTexToPipeFace(modelCC, facing);
    }

    private void bindExportPipeTexAsCan(ModelClayContainer modelCC, Map<EnumFacing, Integer> routes, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) == 0)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export_p.png"));
        else
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/export_" + routes.get(facing) + "_p.png"));

        pasteBoundTexToPipeFace(modelCC, facing);
    }

    protected void pasteBoundTexToBlockFace(EnumFacing facing) {
        PositionTextureVertex[] ptvs = new PositionTextureVertex[4];
        switch (facing) {
            case DOWN:
                ptvs = new PositionTextureVertex[] {ptv3, ptv7, ptv6, ptv2};
                break;
            case UP:
                ptvs = new PositionTextureVertex[] {ptv5, ptv1, ptv0, ptv4};
                break;
            case SOUTH:
                ptvs = new PositionTextureVertex[] {ptv4, ptv0, ptv2, ptv6};
                break;
            case NORTH:
                ptvs = new PositionTextureVertex[] {ptv1, ptv5, ptv7, ptv3};
                break;
            case WEST:
                ptvs = new PositionTextureVertex[] {ptv0, ptv1, ptv3, ptv2};
                break;
            case EAST:
                ptvs = new PositionTextureVertex[] {ptv5, ptv4, ptv6, ptv7};
                break;
        }
        new TexturedQuad(ptvs, 0, 0, 16, 16, 16, 16).draw(buffer, 1f);
    }

    protected void pasteBoundTexToPipeFace(ModelClayContainer modelCC, EnumFacing facing) {
        switch (facing) {
            case DOWN:
                modelCC.renderPipeDown(buffer);
                break;
            case UP:
                modelCC.renderPipeUp(buffer);
                break;
            case SOUTH:
                modelCC.renderPipeSouth(buffer);
                break;
            case NORTH:
                modelCC.renderPipeNorth(buffer);
                break;
            case WEST:
                modelCC.renderPipeWest(buffer);
                break;
            case EAST:
                modelCC.renderPipeEast(buffer);
                break;
        }
    }
}
