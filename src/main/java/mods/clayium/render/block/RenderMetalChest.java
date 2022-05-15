package mods.clayium.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CMaterials;
import mods.clayium.render.tile.MetalChestRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;


public class RenderMetalChest
        implements ISimpleBlockRenderingHandler {
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (modelId == getRenderId()) {

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            MetalChestRenderer.INSTANCE.renderChest(5, 0.0F, 0.0F, CMaterials.getMaterialFromId(metadata), 0.0D, 0.0D, 0.0D, 0.0F);
            GL11.glEnable(32826);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }


    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }


    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }


    public int getRenderId() {
        return ClayiumCore.metalChestRenderId;
    }
}
