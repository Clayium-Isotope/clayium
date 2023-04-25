package mods.clayium.machine.ClayContainer;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.filter.IFilter;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UtilDirection;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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

@SideOnly(Side.CLIENT)
public class TESRClayContainer extends TileEntitySpecialRenderer<TileEntityClayContainer> {
    private static final double offset = 0.02d;
    private static final PositionTextureVertex ptv0 = new PositionTextureVertex(new Vec3d(0.0d - offset, 0.0d - offset, 0.0d - offset), 0f, 0f);
    private static final PositionTextureVertex ptv1 = new PositionTextureVertex(new Vec3d(0.0d - offset, 0.0d - offset, 1.0d + offset), 0f, 0f);
    private static final PositionTextureVertex ptv2 = new PositionTextureVertex(new Vec3d(0.0d - offset, 1.0d + offset, 0.0d - offset), 0f, 0f);
    private static final PositionTextureVertex ptv3 = new PositionTextureVertex(new Vec3d(0.0d - offset, 1.0d + offset, 1.0d + offset), 0f, 0f);
    private static final PositionTextureVertex ptv4 = new PositionTextureVertex(new Vec3d(1.0d + offset, 0.0d - offset, 0.0d - offset), 0f, 0f);
    private static final PositionTextureVertex ptv5 = new PositionTextureVertex(new Vec3d(1.0d + offset, 0.0d - offset, 1.0d + offset), 0f, 0f);
    private static final PositionTextureVertex ptv6 = new PositionTextureVertex(new Vec3d(1.0d + offset, 1.0d + offset, 0.0d - offset), 0f, 0f);
    private static final PositionTextureVertex ptv7 = new PositionTextureVertex(new Vec3d(1.0d + offset, 1.0d + offset, 1.0d + offset), 0f, 0f);
    private static final PositionTextureVertex[] downPTVs  = new PositionTextureVertex[] { ptv3, ptv7, ptv6, ptv2 };
    private static final PositionTextureVertex[] upPTVs    = new PositionTextureVertex[] { ptv5, ptv1, ptv0, ptv4 };
    private static final PositionTextureVertex[] southPTVs = new PositionTextureVertex[] { ptv4, ptv0, ptv2, ptv6 };
    private static final PositionTextureVertex[] northPTVs = new PositionTextureVertex[] { ptv1, ptv5, ptv7, ptv3 };
    private static final PositionTextureVertex[] westPTVs  = new PositionTextureVertex[] { ptv0, ptv1, ptv3, ptv2 };
    private static final PositionTextureVertex[] eastPTVs  = new PositionTextureVertex[] { ptv5, ptv4, ptv6, ptv7 };

    @Override
    public void render(TileEntityClayContainer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        TESRClayContainer.render(te, x, y, z,  partialTicks, destroyStage, alpha, this.rendererDispatcher);
    }

    public static void render(TileEntityClayContainer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, TileEntityRendererDispatcher rendererDispatcher) {
        if (te == null || te.isInvalid()) return;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        ModelClayContainer modelCC = new ModelClayContainer(buffer);

        if (destroyStage >= 0) {
            TESRClayContainer.bindTexture(rendererDispatcher, DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        } else {
            TESRClayContainer.bindTexture(rendererDispatcher, new ResourceLocation(ClayiumCore.ModId, "textures/blocks/machinehull-" + (te.getHullTier() - 1) + ".png"));
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y + 1.0d, z + 1.0d);
        GlStateManager.scale(1.0d, -1.0d, -1.0d);

        if (!BlockStateClayContainer.renderAsPipe(te)) {
            modelCC.renderBlocked();

            ResourceLocation face = te.getFaceResource();
            if (face != null) {
                TESRClayContainer.bindTexture(rendererDispatcher, face);
                pasteBoundTexToBlockFace(te.getFront());
            }

            for (EnumFacing facing : EnumFacing.VALUES) {
                EnumSide side = UtilDirection.getSideOfDirection(te.getFront(), facing);
                bindImportBlockTexAsCan(te.getImportRoute(side), te.getInsertIcons(), facing, rendererDispatcher);
                bindExportBlockTexAsCan(te.getExportRoute(side), te.getExtractIcons(), facing, rendererDispatcher);

                bindFilterBlockTexAsCan(te.getFilters().get(facing), facing, rendererDispatcher);
            }

        } else {
            modelCC.renderPiped(te.getBlockState(), buffer);

            if (rendererDispatcher.entity instanceof EntityPlayer
                    && ClayiumItems.hasPipingTools((EntityPlayer) rendererDispatcher.entity)) {
                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (te.getBlockState().isTheFacingActivated(facing)) {
                        EnumSide side = UtilDirection.getSideOfDirection(te.getFront(), facing);
                        bindImportPipeTexAsCan(modelCC, te.getImportRoute(side), te.getInsertPipeIcons(), facing, buffer, rendererDispatcher);
                        bindExportPipeTexAsCan(modelCC, te.getExportRoute(side), te.getExtractPipeIcons(), facing, buffer, rendererDispatcher);
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

    private static void bindImportBlockTexAsCan(int route, List<ResourceLocation> otherwise, EnumFacing facing, TileEntityRendererDispatcher rendererDispatcher) {
        if (route == -1) return;

        if (route == -2)
            TESRClayContainer.bindTexture(rendererDispatcher, new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_energy.png"));
        else if (route < otherwise.size())
            TESRClayContainer.bindTexture(rendererDispatcher, otherwise.get(route));

        pasteBoundTexToBlockFace(facing);
    }

    private static void bindExportBlockTexAsCan(int route, List<ResourceLocation> otherwise, EnumFacing facing, TileEntityRendererDispatcher rendererDispatcher) {
        if (route == -1) return;

        if (route < otherwise.size())
            TESRClayContainer.bindTexture(rendererDispatcher, otherwise.get(route));

        pasteBoundTexToBlockFace(facing);
    }

    private static void bindImportPipeTexAsCan(ModelClayContainer modelCC, int route, List<ResourceLocation> otherwise, EnumFacing facing, BufferBuilder buffer, TileEntityRendererDispatcher rendererDispatcher) {
        if (route == -1) return;

        if (route == -2)
            TESRClayContainer.bindTexture(rendererDispatcher, new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/import_energy_p.png"));
        else if (route < otherwise.size())
            TESRClayContainer.bindTexture(rendererDispatcher, otherwise.get(route));

        pasteBoundTexToPipeFace(modelCC, facing, buffer);
    }

    private static void bindExportPipeTexAsCan(ModelClayContainer modelCC, int route, List<ResourceLocation> otherwise, EnumFacing facing, BufferBuilder buffer, TileEntityRendererDispatcher rendererDispatcher) {
        if (route == -1) return;

        if (route < otherwise.size())
            TESRClayContainer.bindTexture(rendererDispatcher, otherwise.get(route));

        pasteBoundTexToPipeFace(modelCC, facing, buffer);
    }

    private static void bindFilterBlockTexAsCan(ItemStack filter, EnumFacing facing, TileEntityRendererDispatcher rendererDispatcher) {
        if (!(filter.getItem() instanceof IFilter)) return;

        TESRClayContainer.bindTexture(rendererDispatcher, new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/filter.png"));
        pasteBoundTexToBlockFace(facing);
    }

    protected static void pasteBoundTexToBlockFace(EnumFacing facing) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

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

    protected static void pasteBoundTexToPipeFace(ModelClayContainer modelCC, EnumFacing facing, BufferBuilder buffer) {
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

    protected static void bindTexture(TileEntityRendererDispatcher rendererDispatcher, ResourceLocation location)
    {
        TextureManager texturemanager = rendererDispatcher.renderEngine;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(location);
        }
    }
}
