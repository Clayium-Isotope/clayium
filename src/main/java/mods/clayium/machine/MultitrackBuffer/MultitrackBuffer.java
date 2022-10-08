package mods.clayium.machine.MultitrackBuffer;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultitrackBuffer extends ClayiumMachine {
    public MultitrackBuffer(int tier) {
        super(EnumMachineKind.multitrackBuffer, "", tier, TileEntityMultitrackBuffer.class, GuiHandler.GuiIdMultitrackBuffer);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityMultitrackBuffer) {
            ((TileEntityMultitrackBuffer) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite(), 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
