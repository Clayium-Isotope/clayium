package mods.clayium.render.tile;

import mods.clayium.block.tile.TileCAReactor;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilRender;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class CAReactorRenderer
        extends TileEntitySpecialRenderer {
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_147500_8_) {
        if (tile instanceof TileCAReactor && ClayiumCore.cfgCAReactorGlittering) {
            TileCAReactor tile1 = (TileCAReactor) tile;
            if (tile1.isDoingWork() && tile1.hullCoords != null) {
                UtilRender.setLightValue(15, 15);
                for (int j = 1; j < tile1.reactorRank + 1.01D; j++) {
                    for (int[] coord : tile1.hullCoords) {
                        int[] rel = tile1.getRelativeCoord(coord[0], coord[1], coord[2]);
                        float s = 1.0F;

                        float i = (float) tile1.reactorRank + 1.0F - j;
                        float r = j / ((float) tile1.reactorRank + 1.0F);
                        float f = 0.01F * ((float) Math.pow(i, 1.6D) + 1.0F);
                        UtilRender.renderBox((rel[0] - f), (rel[1] - f), (rel[2] - f), (rel[0] + s + f), (rel[1] + s + f), (rel[2] + s + f), 1, 1.0F, 1.0F, 0.3F + 0.05F * (2.0F * r - r * r) * j, 0.11F, false);
                    }
                }
            }
        }
    }
}
