package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class EnergyStorageUpgrade extends BlockTiered implements IClayContainerModifier {
    private final int additionalEnergyStorage;

    public EnergyStorageUpgrade(int meta, int tier, int additionalEnergyStorage) {
        super(Material.IRON, "energy_storage_upgrade_", meta, tier);
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.additionalEnergyStorage = additionalEnergyStorage;
    }

    @Override
    public void modifyClayContainer(IBlockAccess world, BlockPos pos, TileEntityClayContainer tile) {
        if (tile instanceof IClayEnergyConsumer) {
            IClayEnergyConsumer.growCEStorageSize((IClayEnergyConsumer) tile, this.getAdditionalEnergyStorage());
        }
    }

    public int getAdditionalEnergyStorage() {
        return additionalEnergyStorage;
    }
}
