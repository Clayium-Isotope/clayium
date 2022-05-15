package mods.clayium.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;


public class RenderQuartzCrucible
        implements ISimpleBlockRenderingHandler {
    private IIcon boxIIcon;

    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        this.boxIIcon = Blocks.quartz_block.getBlockTextureFromSide(1);

        if (modelID == getRenderId()) {


            UtilRender.renderInvCuboid(renderer, block, 0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F, this.boxIIcon);


            UtilRender.renderInvCuboid(renderer, block, 0.0F, 0.0625F, 0.0F, 1.0F, 0.75F, 0.0625F, this.boxIIcon);
            UtilRender.renderInvCuboid(renderer, block, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.75F, 1.0F, this.boxIIcon);
            UtilRender.renderInvCuboid(renderer, block, 0.0F, 0.0625F, 0.0625F, 0.0625F, 0.75F, 0.9375F, this.boxIIcon);
            UtilRender.renderInvCuboid(renderer, block, 0.9375F, 0.0625F, 0.0625F, 1.0F, 0.75F, 0.9375F, this.boxIIcon);
        }
    }


    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        this.boxIIcon = Blocks.quartz_block.getBlockTextureFromSide(1);

        if (modelId == getRenderId()) {

            Tessellator tessellator = Tessellator.instance;

            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.75F, 0.0625F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.0625F, 0.9375F, 1.0F, 0.75F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.0F, 0.0625F, 0.0625F, 0.0625F, 0.75F, 0.9375F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setOverrideBlockTexture(this.boxIIcon);
            block.setBlockBounds(0.9375F, 0.0625F, 0.0625F, 1.0F, 0.75F, 0.9375F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

            renderer.clearOverrideBlockTexture();

            int meta = world.getBlockMetadata(x, y, z);
            if (meta > 0) {

                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (meta + 1) / 16.0F, 1.0F);
                renderer.setRenderBoundsFromBlock(block);
                tessellator.setBrightness(240);
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                renderer.renderFaceYPos(block, x, y, z, renderer.getBlockIcon((Block) Blocks.portal));
            }

            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            return true;
        }
        return false;
    }


    public boolean shouldRender3DInInventory(int a) {
        return true;
    }


    public int getRenderId() {
        return ClayiumCore.quartzCrucibleRenderId;
    }
}
