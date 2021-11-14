package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TilePANAdapter;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class PANAdapter
        extends ClayContainerTiered implements IPANConductor {
    public PANAdapter(int tier) {
        super(Material.iron, (Class) TilePANAdapter.class, 40, 2, tier);
        setHardness(2.0F).setResistance(2.0F);
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        setSameOverlayIcons(par1IconRegister.registerIcon("clayium:panadapter"));
        super.registerBlockIcons(par1IconRegister);
    }


    public int getTier(ItemStack itemstack) {
        return Math.max(super.getTier(itemstack), 11);
    }


    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        TileEntity te1 = UtilBuilder.safeGetTileEntity(world, tileX, tileY, tileZ);
        if (te instanceof TilePANAdapter && !(te1 instanceof TilePANAdapter))
            ((TilePANAdapter) te).onNeighborChange();
    }
}
