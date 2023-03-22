package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChemicalMetalSeparator extends ClayiumMachine {
    public ChemicalMetalSeparator() {
        super(EnumMachineKind.chemicalMetalSeparator, "", 6, TileEntityChemicalMetalSeparator.class, GuiHandler.GuiIdChemicalMetalSeparator);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityChemicalMetalSeparator) {
            ((TileEntityChemicalMetalSeparator) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.UP, 0);
            ((TileEntityChemicalMetalSeparator) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.DOWN, 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
