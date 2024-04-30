package mods.clayium.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.itemblock.ItemBlockTierNamed;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;

public class EnergyStorageUpgrade extends BlockTiered implements IClayContainerModifier {
    private final int additionalEnergyStorage;

    public EnergyStorageUpgrade(int meta, TierPrefix tier, int additionalEnergyStorage) {
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

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTierNamed(this, "util.block.energy_storage_upgrade", UtilLocale.localizeAndFormat(TierPrefix.getLocalizeKey(this.tier)));
    }
}
