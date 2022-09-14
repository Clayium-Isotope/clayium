package mods.clayium.machine.ClayAssembler;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.UtilTier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayAssembler extends ClayiumMachine {
    public ClayAssembler(EnumMachineKind kind, int tier) {
        super(kind, "", tier, TileEntityClayAssembler.class, GuiHandler.GuiIdClayAssembler);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayAssembler) {
            TileEntityClayAssembler teca = (TileEntityClayAssembler) worldIn.getTileEntity(pos);
            teca.importRoutes.replace(EnumFacing.UP, 2);
            if (!UtilTier.canManufactureCraft(this.tier))
                teca.importRoutes.replace(placer.getHorizontalFacing(), -2);
            teca.exportRoutes.replace(EnumFacing.DOWN, 0);
        }
    }
}
