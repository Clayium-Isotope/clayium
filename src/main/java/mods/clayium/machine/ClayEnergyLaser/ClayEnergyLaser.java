package mods.clayium.machine.ClayEnergyLaser;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@HasOriginalState
public class ClayEnergyLaser extends ClayDirectionalNoRecipeMachine {
    public ClayEnergyLaser(TierPrefix tier) {
        super(TileEntityClayEnergyLaser::new, EnumMachineKind.clayEnergyLaser, GuiHandler.GuiIdClayEnergyLaser, tier);
    }

    public void updatePower(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityClayEnergyLaser) {
                ((TileEntityClayEnergyLaser) tile).setPowered(ClayiumConfiguration.cfgInverseClayLaserRSCondition == (worldIn.getRedstonePowerFromNeighbors(pos) != 0));
            }
        }
    }

    @Override // TODO Move to BlockState class
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.updatePower(worldIn, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.updatePower(worldIn, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayEnergyLaser) {
            ((TileEntityClayEnergyLaser) worldIn.getTileEntity(pos)).setManager(worldIn, pos, UtilDirection.getDirectionFromEntity(pos, placer));
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.energy_laser");
        super.addInformation(stack, player, tooltip, advanced);

        if (UtilLocale.canLocalize("tooltip.energy_laser.energyConsumption")) {
            int e = 0;
            switch (this.tier) {
                case claySteel:
                    e = TileEntityClayEnergyLaser.consumingEnergyBlue;
                    break;
                case clayium:
                    e = TileEntityClayEnergyLaser.consumingEnergyGreen;
                    break;
                case ultimate:
                    e = TileEntityClayEnergyLaser.consumingEnergyRed;
                    break;
                case antimatter:
                    e = TileEntityClayEnergyLaser.consumingEnergyWhite;
            }

            tooltip.add(UtilLocale.localizeAndFormat("tooltip.energy_laser.energyConsumption", UtilLocale.ClayEnergyNumeral(e)));
        }
    }
}
