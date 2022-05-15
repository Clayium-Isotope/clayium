package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileClayChunkLoader;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClayChunkLoader extends ClayContainerTiered {
    public ClayChunkLoader(int tier) {
        super(Material.iron, (Class) TileClayChunkLoader.class, 0, tier);
        setStepSound(Block.soundTypeMetal);
        setHardness(6.0F);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 0);
    }


    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te != null && te instanceof IClayChunkLoader) {
            ((IClayChunkLoader) te).releaseTicket();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        setSameIcons(par1IconRegister.registerIcon("clayium:zk60ahull"));
        setSameOverlayIcons(par1IconRegister.registerIcon("clayium:chunkloader"));
    }
}
