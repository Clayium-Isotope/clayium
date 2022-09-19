package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayChemicalReactor extends ClayiumMachine {
    public ClayChemicalReactor(int tier) {
        super(EnumMachineKind.chemicalReactor, "", tier, TileEntityClayChemicalReactor.class, GuiHandler.GuiIdClayChemicalReactor);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayChemicalReactor) {
            TileEntityClayChemicalReactor teccr = (TileEntityClayChemicalReactor) worldIn.getTileEntity(pos);
            teccr.importRoutes.replace(EnumFacing.UP, 0);
            teccr.importRoutes.replace(placer.getHorizontalFacing(), -2);
            teccr.exportRoutes.replace(EnumFacing.DOWN, 0);
        }
    }
}
