package mods.clayium.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.ClayContainer;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class RenderClayContainer
        implements ISimpleBlockRenderingHandler {
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (modelId == getRenderId()) {


            ((ClayContainer) block).setInitialBlockBounds();

            int metadata1 = (((ClayContainer) block).metaMode != 0) ? (3 + metadata / 16 * 16) : metadata;
            Tessellator tessellator = Tessellator.instance;
            renderer.setRenderBoundsFromBlock(block);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderer.setRenderBoundsFromBlock(block);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata1));
            if (block instanceof ClayContainer && ((ClayContainer) block).getOverlayIcon(0, metadata1) != null)
                renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getOverlayIcon(0, metadata1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata1));
            if (block instanceof ClayContainer && ((ClayContainer) block).getOverlayIcon(1, metadata1) != null)
                renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getOverlayIcon(1, metadata1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata1));
            if (block instanceof ClayContainer && ((ClayContainer) block).getOverlayIcon(2, metadata1) != null)
                renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getOverlayIcon(2, metadata1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata1));
            if (block instanceof ClayContainer && ((ClayContainer) block).getOverlayIcon(3, metadata1) != null)
                renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getOverlayIcon(3, metadata1));
            if (block instanceof mods.clayium.block.ClayBuffer || block instanceof mods.clayium.block.MultitrackBuffer)
                renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getInsertIcons(null, 0, 0, 0)[0]);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata1));
            if (block instanceof ClayContainer && ((ClayContainer) block).getOverlayIcon(4, metadata1) != null)
                renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getOverlayIcon(4, metadata1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata1));
            if (block instanceof ClayContainer && ((ClayContainer) block).getOverlayIcon(5, metadata1) != null)
                renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, ((ClayContainer) block).getOverlayIcon(5, metadata1));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            renderer.setRenderBoundsFromBlock(block);
        }
    }


    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == getRenderId()) {

            if (block instanceof ClayContainer) {

                if (!((ClayContainer) block).renderAsPipe(world, x, y, z)) {
                    Tessellator tessellator = Tessellator.instance;

                    int renderMode = (((ClayContainer) block).isTransparent() || ((ClayContainer) block).isOverlayTransparent()) ? block.getRenderBlockPass() : 2;

                    boolean underlay = (ClayContainer.renderPass == 0) ? (!((ClayContainer) block).isTransparent()) : ((ClayContainer) block).isTransparent();
                    boolean overlay = (ClayContainer.renderPass == 0) ? (!((ClayContainer) block).isOverlayTransparent()) : ((ClayContainer) block).isOverlayTransparent();
                    boolean io = (ClayContainer.renderPass == 0);

                    if (underlay) {
                        renderer.renderStandardBlock(block, x, y, z);
                    }


                    double d = 0.0D;
                    if (renderMode == 1) {
                        d = 3.0E-4D;
                    }
                    if (renderMode == 0) {
                        tessellator.draw();

                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glAlphaFunc(516, 0.1F);

                        tessellator.startDrawingQuads();
                    }

                    boolean flag = false;
                    float f3 = 0.5F, f4 = 1.0F, f5 = 0.8F, f6 = 0.6F;
                    float f7 = f4, f8 = f4, f9 = f4;
                    float f10 = f3, f11 = f5, f12 = f6;
                    float f13 = f3, f14 = f5, f15 = f6;
                    float f16 = f3, f17 = f5, f18 = f6;


                    TileClayContainer te = (UtilBuilder.safeGetTileEntity(world, x, y, z) instanceof TileClayContainer) ? (TileClayContainer) UtilBuilder.safeGetTileEntity(world, x, y, z) : null;
                    Block block1 = block;


                    int l = block.getMixedBrightnessForBlock(world, x, y, z);

                    IIcon[] insertIcons = ((ClayContainer) block1).getInsertIcons(world, x, y, z), extractIcons = ((ClayContainer) block1).getExtractIcons(world, x, y, z);
                    IIcon filterIcon = ((ClayContainer) block1).FilterIcon;
                    tessellator.setBrightness((renderer.renderMinY > 0.0D) ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
                    tessellator.setColorOpaque_F(f10, f13, f16);
                    tessellator.setNormal(0.0F, -1.0F, 0.0F);
                    if (((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 0) != null && overlay)
                        renderer.renderFaceYNeg(block, x, y - d, z, ((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 0));
                    if (te != null && io) {
                        int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 0) - 6];
                        if (insertIcons != null && route >= 0 && route < insertIcons.length)
                            renderer.renderFaceYNeg(block, x, y - d * 2.0D, z, insertIcons[route]);
                        route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 0) - 6];
                        if (extractIcons != null && route >= 0 && route < extractIcons.length)
                            renderer.renderFaceYNeg(block, x, y - d * 3.0D, z, extractIcons[route]);
                        if (filterIcon != null && te.filters[UtilDirection.direction2Side(te.getFrontDirection(), 0) - 6] != null) {
                            renderer.renderFaceYNeg(block, x, y - d * 4.0D, z, filterIcon);
                        }
                    }
                    tessellator.setBrightness((renderer.renderMaxY < 1.0D) ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
                    tessellator.setColorOpaque_F(f7, f8, f9);
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
                    if (((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 1) != null && overlay)
                        renderer.renderFaceYPos(block, x, y + d, z, ((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 1));
                    if (te != null && io) {
                        int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 1) - 6];
                        if (insertIcons != null && route >= 0 && route < insertIcons.length) {
                            renderer.renderFaceYPos(block, x, y + d * 2.0D, z, insertIcons[route]);
                        }


                        route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 1) - 6];
                        if (extractIcons != null && route >= 0 && route < extractIcons.length)
                            renderer.renderFaceYPos(block, x, y + d * 3.0D, z, extractIcons[route]);
                        if (filterIcon != null && te.filters[UtilDirection.direction2Side(te.getFrontDirection(), 1) - 6] != null) {
                            renderer.renderFaceYPos(block, x, y + d * 4.0D, z, filterIcon);
                        }
                    }
                    tessellator.setBrightness((renderer.renderMinZ > 0.0D) ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
                    tessellator.setColorOpaque_F(f11, f14, f17);
                    tessellator.setNormal(0.0F, 0.0F, -1.0F);
                    if (((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 2) != null && overlay)
                        renderer.renderFaceZNeg(block, x, y, z - d, ((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 2));
                    if (te != null && io) {
                        int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 2) - 6];
                        if (insertIcons != null && route >= 0 && route < insertIcons.length)
                            renderer.renderFaceZNeg(block, x, y, z - d * 2.0D, insertIcons[route]);
                        route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 2) - 6];
                        if (extractIcons != null && route >= 0 && route < extractIcons.length)
                            renderer.renderFaceZNeg(block, x, y, z - d * 3.0D, extractIcons[route]);
                        if (filterIcon != null && te.filters[UtilDirection.direction2Side(te.getFrontDirection(), 2) - 6] != null) {
                            renderer.renderFaceZNeg(block, x, y, z - d * 4.0D, filterIcon);
                        }
                    }
                    tessellator.setBrightness((renderer.renderMaxZ < 1.0D) ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
                    tessellator.setColorOpaque_F(f11, f14, f17);
                    tessellator.setNormal(0.0F, 0.0F, 1.0F);
                    if (((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 3) != null && overlay)
                        renderer.renderFaceZPos(block, x, y, z + d, ((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 3));
                    if (te != null && io) {
                        int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 3) - 6];
                        if (insertIcons != null && route >= 0 && route < insertIcons.length)
                            renderer.renderFaceZPos(block, x, y, z + d * 2.0D, insertIcons[route]);
                        route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 3) - 6];
                        if (extractIcons != null && route >= 0 && route < extractIcons.length)
                            renderer.renderFaceZPos(block, x, y, z + d * 3.0D, extractIcons[route]);
                        if (filterIcon != null && te.filters[UtilDirection.direction2Side(te.getFrontDirection(), 3) - 6] != null) {
                            renderer.renderFaceZPos(block, x, y, z + d * 4.0D, filterIcon);
                        }
                    }
                    tessellator.setBrightness((renderer.renderMinX > 0.0D) ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
                    tessellator.setColorOpaque_F(f12, f15, f18);
                    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                    if (((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 4) != null && overlay)
                        renderer.renderFaceXNeg(block, x - d, y, z, ((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 4));
                    if (te != null && io) {
                        int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 4) - 6];
                        if (insertIcons != null && route >= 0 && route < insertIcons.length)
                            renderer.renderFaceXNeg(block, x - d * 2.0D, y, z, insertIcons[route]);
                        route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 4) - 6];
                        if (extractIcons != null && route >= 0 && route < extractIcons.length)
                            renderer.renderFaceXNeg(block, x - d * 3.0D, y, z, extractIcons[route]);
                        if (filterIcon != null && te.filters[UtilDirection.direction2Side(te.getFrontDirection(), 4) - 6] != null) {
                            renderer.renderFaceXNeg(block, x - d * 4.0D, y, z, filterIcon);
                        }
                    }
                    tessellator.setBrightness((renderer.renderMaxX < 1.0D) ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
                    tessellator.setColorOpaque_F(f12, f15, f18);
                    tessellator.setNormal(1.0F, 0.0F, 0.0F);
                    if (((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 5) != null && overlay)
                        renderer.renderFaceXPos(block, x + d, y, z, ((ClayContainer) block).getOverlayIcon(renderer.blockAccess, x, y, z, 5));
                    if (te != null && io) {
                        int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 5) - 6];
                        if (insertIcons != null && route >= 0 && route < insertIcons.length)
                            renderer.renderFaceXPos(block, x + d * 2.0D, y, z, insertIcons[route]);
                        route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), 5) - 6];
                        if (extractIcons != null && route >= 0 && route < extractIcons.length)
                            renderer.renderFaceXPos(block, x + d * 3.0D, y, z, extractIcons[route]);
                        if (filterIcon != null && te.filters[UtilDirection.direction2Side(te.getFrontDirection(), 5) - 6] != null) {
                            renderer.renderFaceXPos(block, x + d * 4.0D, y, z, filterIcon);
                        }
                    }


                    if (renderMode == 0) {
                        tessellator.draw();

                        GL11.glDisable(3042);
                        GL11.glAlphaFunc(516, 0.5F);

                        tessellator.startDrawingQuads();
                    }
                } else {
                    boolean pipingMode = ClayiumCore.proxy.renderAsPipingMode();
                    ClayContainer block1 = (ClayContainer) block;

                    TileClayContainer te = (UtilBuilder.safeGetTileEntity(world, x, y, z) instanceof TileClayContainer) ? (TileClayContainer) UtilBuilder.safeGetTileEntity(world, x, y, z) : null;

                    Tessellator tessellator = Tessellator.instance;

                    float o = ClayContainer.pipeWidth;
                    renderer.field_152631_f = true;
                    block.setBlockBounds(0.5F - o, 0.5F - o, 0.5F - o, 0.5F + o, 0.5F + o, 0.5F + o);


                    renderer.setRenderBoundsFromBlock(block);
                    renderer.renderStandardBlock(block, x, y, z);

                    IIcon[] insertIcons = block1.getInsertPipeIcons(world, x, y, z), extractIcons = block1.getExtractPipeIcons(world, x, y, z);

                    UtilDirection[] directions = {UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.EAST, UtilDirection.WEST, UtilDirection.UP, UtilDirection.DOWN};
                    for (UtilDirection direction : directions) {
                        if (block1.checkPipeConnection(world, x, y, z, direction)) {
                            block.setBlockBounds(0.5F - o + ((direction.offsetX == 1) ? (o * 2.0F) : 0.0F) - ((direction.offsetX == -1) ? (0.5F - o) : 0.0F), 0.5F - o + ((direction.offsetY == 1) ? (o * 2.0F) : 0.0F) - ((direction.offsetY == -1) ? (0.5F - o) : 0.0F), 0.5F - o + ((direction.offsetZ == 1) ? (o * 2.0F) : 0.0F) - ((direction.offsetZ == -1) ? (0.5F - o) : 0.0F), 0.5F + o - ((direction.offsetX == -1) ? (o * 2.0F) : 0.0F) + ((direction.offsetX == 1) ? (0.5F - o) : 0.0F), 0.5F + o - ((direction.offsetY == -1) ? (o * 2.0F) : 0.0F) + ((direction.offsetY == 1) ? (0.5F - o) : 0.0F), 0.5F + o - ((direction.offsetZ == -1) ? (o * 2.0F) : 0.0F) + ((direction.offsetZ == 1) ? (0.5F - o) : 0.0F));


                            renderer.setRenderBoundsFromBlock(block);
                            renderer.renderStandardBlock(block, x, y, z);

                            if (pipingMode) {
                                int route = te.insertRoutes[UtilDirection.direction2Side(te.getFrontDirection(), direction.ordinal()) - 6];
                                if (insertIcons != null && route >= 0 && route < insertIcons.length) {
                                    renderer.setOverrideBlockTexture(insertIcons[route]);
                                    renderer.renderStandardBlock(block, x, y, z);
                                }
                                route = te.extractRoutes[UtilDirection.direction2Side(te.getFrontDirection(), direction.ordinal()) - 6];
                                if (extractIcons != null && route >= 0 && route < extractIcons.length) {
                                    renderer.setOverrideBlockTexture(extractIcons[route]);
                                    renderer.renderStandardBlock(block, x, y, z);
                                }
                                if (renderer.hasOverrideBlockTexture()) {
                                    renderer.clearOverrideBlockTexture();
                                }
                            }
                        }
                        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    }
                    renderer.field_152631_f = false;
                }
            }
            return true;
        }
        return false;
    }


    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }


    public int getRenderId() {
        return ClayiumCore.clayContainerRenderId;
    }
}
