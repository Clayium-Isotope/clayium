package mods.clayium.block;

import mods.clayium.block.tile.TileClayContainerTiered;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClayContainerTiered
        extends ClayContainer implements ITieredBlock {
    protected int tier;

    public ClayContainerTiered(Material material, Class<? extends TileEntity> tileEntityClass, int metaMode, int tier) {
        this(material, tileEntityClass, "clayium:machinehull-" + String.valueOf(tier - 1), metaMode, tier);
    }

    public ClayContainerTiered(Material material, Class<? extends TileEntity> tileEntityClass, String machineIconStr, int metaMode, int tier) {
        super(material, tileEntityClass, machineIconStr, metaMode);
        this.tier = tier;
    }

    public ClayContainerTiered(Material material, Class<? extends TileEntity> tileEntityClass, int guiId, int metaMode, int tier) {
        this(material, tileEntityClass, "clayium:machinehull-" + String.valueOf(tier - 1), guiId, metaMode, tier);
    }

    public ClayContainerTiered(Material material, Class<? extends TileEntity> tileEntityClass, String machineIconStr, int guiId, int metaMode, int tier) {
        super(material, tileEntityClass, machineIconStr, guiId, metaMode);
        this.tier = tier;
    }

    public int getTier(ItemStack itemstack) {
        return this.tier;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return this.tier;
    }


    public TileEntity createNewTileEntity(World world, int par2) {
        TileEntity tile = super.createNewTileEntity(world, par2);
        if (tile instanceof TileClayContainerTiered) {
            ((TileClayContainerTiered) tile).setBaseTier(this.tier);
        }
        return tile;
    }
}
