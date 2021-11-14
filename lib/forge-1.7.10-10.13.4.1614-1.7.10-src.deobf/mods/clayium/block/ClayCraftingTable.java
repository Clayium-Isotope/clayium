package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileClayCraftingTable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ClayCraftingTable
        extends ClayContainerTiered {
    public ClayCraftingTable(int tier) {
        super(Material.clay, (Class) TileClayCraftingTable.class, "clayium:compressedclay-0", 30, 0, tier);
        setStepSound(Block.soundTypeGravel);
        setHardness(1.0F);
        setResistance(4.0F);
        setHarvestLevel("shovel", 0);
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.UpOverlayIcon = par1IconRegister.registerIcon("clayium:claycraftingtable");
        super.registerBlockIcons(par1IconRegister);
    }


    public boolean isOpaqueCube() {
        return false;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }

    public void setInitialBlockBounds() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    }
}
