package mods.clayium.machine.ClayContainer;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.filter.IFilter;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TESRClayContainer extends TileEntitySpecialRenderer<TileEntityClayContainer> {
    private final BufferBuilder buffer = Tessellator.getInstance().getBuffer();

    private final PositionTextureVertex ptv0 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv1 = new PositionTextureVertex(new Vec3d(0.0d, 0.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex ptv2 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv3 = new PositionTextureVertex(new Vec3d(0.0d, 1.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex ptv4 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv5 = new PositionTextureVertex(new Vec3d(1.0d, 0.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex ptv6 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 0.0d), 0f, 0f);
    private final PositionTextureVertex ptv7 = new PositionTextureVertex(new Vec3d(1.0d, 1.0d, 1.0d), 0f, 0f);
    private final PositionTextureVertex[] downPTVs  = new PositionTextureVertex[] { ptv3, ptv7, ptv6, ptv2 };
    private final PositionTextureVertex[] upPTVs    = new PositionTextureVertex[] { ptv5, ptv1, ptv0, ptv4 };
    private final PositionTextureVertex[] southPTVs = new PositionTextureVertex[] { ptv4, ptv0, ptv2, ptv6 };
    private final PositionTextureVertex[] northPTVs = new PositionTextureVertex[] { ptv1, ptv5, ptv7, ptv3 };
    private final PositionTextureVertex[] westPTVs  = new PositionTextureVertex[] { ptv0, ptv1, ptv3, ptv2 };
    private final PositionTextureVertex[] eastPTVs  = new PositionTextureVertex[] { ptv5, ptv4, ptv6, ptv7 };

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

        if (!BlockStateClayContainer.renderAsPipe(te)) {
            modelCC.renderBlocked();

            ResourceLocation face = te.getFaceResource();
            if (face != null) {
                this.bindTexture(face);
                if (te.getBlockState() instanceof BlockStateClaySidedContainer)
                    pasteBoundTexToBlockFace(te.getBlockState().getValue(BlockStateClaySidedContainer.FACING));
                if (te.getBlockState() instanceof BlockStateClayDirectionalContainer)
                    pasteBoundTexToBlockFace(te.getBlockState().getValue(BlockStateClayDirectionalContainer.FACING));
            }

            for (EnumFacing facing : EnumFacing.VALUES) {
                bindImportBlockTexAsCan(te.importRoutes, te.InsertIcons, facing);
                bindExportBlockTexAsCan(te.exportRoutes, te.ExtractIcons, facing);

                bindFilterBlockTexAsCan(te.filters.get(facing), facing);
            }

        } else {
            modelCC.renderPiped(te.getBlockState(), buffer);

            if (rendererDispatcher.entity instanceof EntityPlayer
                    && ClayiumItems.hasPipingTools((EntityPlayer) rendererDispatcher.entity)) {
                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (te.getBlockState().isTheFacingActivated(facing)) {
                        bindImportPipeTexAsCan(modelCC, te.importRoutes, te.InsertPipeIcons, facing);
                        bindExportPipeTexAsCan(modelCC, te.exportRoutes, te.ExtractPipeIcons, facing);
                    }
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

    private void bindImportBlockTexAsCan(Map<EnumFacing, Integer> routes, List<ResourceLocation> otherwise, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) == -2)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_energy.png"));
        else if (routes.get(facing) < otherwise.size())
            this.bindTexture(otherwise.get(routes.get(facing)));

        pasteBoundTexToBlockFace(facing);
    }

    private void bindExportBlockTexAsCan(Map<EnumFacing, Integer> routes, List<ResourceLocation> otherwise, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) < otherwise.size())
            this.bindTexture(otherwise.get(routes.get(facing)));

        pasteBoundTexToBlockFace(facing);
    }

    private void bindImportPipeTexAsCan(ModelClayContainer modelCC, Map<EnumFacing, Integer> routes, List<ResourceLocation> otherwise, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) == -2)
            this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_energy_p.png"));
        else if (routes.get(facing) < otherwise.size())
            this.bindTexture(otherwise.get(routes.get(facing)));

        pasteBoundTexToPipeFace(modelCC, facing);
    }

    private void bindExportPipeTexAsCan(ModelClayContainer modelCC, Map<EnumFacing, Integer> routes, List<ResourceLocation> otherwise, EnumFacing facing) {
        if (routes.get(facing) == -1) return;

        if (routes.get(facing) < otherwise.size())
            this.bindTexture(otherwise.get(routes.get(facing)));

        pasteBoundTexToPipeFace(modelCC, facing);
    }

    private void bindFilterBlockTexAsCan(ItemStack filter, EnumFacing facing) {
        if (!(filter.getItem() instanceof IFilter)) return;

        this.bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/filter.png"));
        pasteBoundTexToBlockFace(facing);
    }

    protected void pasteBoundTexToBlockFace(EnumFacing facing) {
        PositionTextureVertex[] ptvs = new PositionTextureVertex[4];
        switch (facing) {
            case DOWN:
                ptvs = downPTVs;
                break;
            case UP:
                ptvs = upPTVs;
                break;
            case SOUTH:
                ptvs = southPTVs;
                break;
            case NORTH:
                ptvs = northPTVs;
                break;
            case WEST:
                ptvs = westPTVs;
                break;
            case EAST:
                ptvs = eastPTVs;
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
