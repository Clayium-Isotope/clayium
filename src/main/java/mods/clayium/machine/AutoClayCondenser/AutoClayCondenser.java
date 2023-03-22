package mods.clayium.machine.AutoClayCondenser;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AutoClayCondenser extends ClayiumMachine {
    public AutoClayCondenser(int tier) {
        super(EnumMachineKind.autoClayCondenser, tier == 5 ? "mk1" : tier == 7 ? "mk2" : "", tier, TileEntityAutoClayCondenser.class, GuiHandler.GuiIdAutoClayCondenser);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayContainer) {
            ((TileEntityClayContainer) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.UP, 0);
            ((TileEntityClayContainer) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.DOWN, 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
