package mods.clayium.block;

import mods.clayium.block.tile.TileClayContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockEnergyStorageUpgrade extends BlockDamaged implements IClayContainerModifier {
    public BlockEnergyStorageUpgrade(Material material) {
        super(material);
    }

    public BlockEnergyStorageUpgrade() {
        this(Material.rock);
    }

    public BlockDamaged addAdditionalEnergyStorage(int size) {
        return putInfo("AdditionalEnergyStorage", Integer.valueOf(size));
    }

    public int getAdditionalEnergyStorage(String blockname) {
        Object obj = getInfo(blockname, "AdditionalEnergyStorage");
        return (obj instanceof Integer) ? ((Integer) obj).intValue() : 0;
    }


    public void modifyClayContainer(IBlockAccess world, int x, int y, int z, TileClayContainer tile) {
        tile.clayEnergyStorageSize += getAdditionalEnergyStorage(getBlockName(world, x, y, z));
        if (tile.clayEnergyStorageSize > 64)
            tile.clayEnergyStorageSize = 64;
    }
}
