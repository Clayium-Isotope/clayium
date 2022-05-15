package mods.clayium.block;

import mods.clayium.block.tile.TilePANCore;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PANCore
        extends ClayContainer implements IPANConductor, ITieredBlock {
    public PANCore() {
        super(Material.iron, (Class) TilePANCore.class, "clayium:pancore", 41, 1);
        setHardness(4.0F).setResistance(4.0F);
    }

    protected boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof TilePANCore) {
            ((TilePANCore) te).debug(player);
        }
        return super.onBlockRightClicked(world, x, y, z, player, side, hitX, hitY, hitZ);
    }


    public int getTier(ItemStack itemstack) {
        return 11;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 11;
    }
}
