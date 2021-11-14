package mods.clayium.misc;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.core.ClayiumCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class RenderFluidTab
        implements ISimpleBlockRenderingHandler {
    private IIcon boxIIcon;

    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        this.boxIIcon = Blocks.planks.getBlockTextureFromSide(1);

        if (modelID == getRenderId()) {


            renderInvCuboid(renderer, block, 0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F, this.boxIIcon);


            renderInvCuboid(renderer, block, 0.0F, 0.125F, 0.0F, 1.0F, 1.0F, 0.0625F, this.boxIIcon);
            renderInvCuboid(renderer, block, 0.0F, 0.125F, 0.9375F, 1.0F, 1.0F, 1.0F, this.boxIIcon);
            renderInvCuboid(renderer, block, 0.0F, 0.125F, 0.0625F, 0.0625F, 1.0F, 0.9375F, this.boxIIcon);
            renderInvCuboid(renderer, block, 0.9375F, 0.125F, 0.0625F, 1.0F, 1.0F, 0.9375F, this.boxIIcon);
        }
    }


    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        this.boxIIcon = Blocks.planks.getBlockTextureFromSide(1);

        if (modelId == getRenderId()) {


            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.125F, 0.0F, 1.0F, 1.0F, 0.0625F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.125F, 0.9375F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.125F, 0.0625F, 0.0625F, 1.0F, 0.9375F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.9375F, 0.125F, 0.0625F, 1.0F, 1.0F, 0.9375F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);


            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            return true;
        }
        return false;
    }


    public boolean shouldRender3DInInventory(int a) {
        return true;
    }


    public int getRenderId() {
        return ClayiumCore.fluidTabRenderId;
    }


    private void renderInvCuboid(RenderBlocks renderer, Block block, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, IIcon icon) {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        block.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        renderer.setRenderBoundsFromBlock(block);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
    }
}
