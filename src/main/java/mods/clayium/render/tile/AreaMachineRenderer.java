package mods.clayium.render.tile;

import mods.clayium.block.tile.IAxisAlignedBBContainer;
import mods.clayium.util.UtilRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class AreaMachineRenderer
        extends TileEntitySpecialRenderer {
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_147500_8_) {
        if (tile instanceof IAxisAlignedBBContainer) {
            UtilRender.setLightValue(15, 15);
            IAxisAlignedBBContainer tile1 = (IAxisAlignedBBContainer) tile;
            UtilRender.renderAxisAlignedBB(tile1, 0.1F, 0.1F, 0.7F);
            Block block1 = tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord);
            UtilRender.renderBox(tile.xCoord + block1.getBlockBoundsMinX(), tile.yCoord + block1.getBlockBoundsMinY(), tile.zCoord + block1.getBlockBoundsMinZ(), tile.xCoord + block1
                    .getBlockBoundsMaxX(), tile.yCoord + block1.getBlockBoundsMaxY(), tile.zCoord + block1.getBlockBoundsMaxZ(), tile1
                    .getBoxAppearance(), 1.0F, 0.0F, 0.0F);
        }
    }
}
