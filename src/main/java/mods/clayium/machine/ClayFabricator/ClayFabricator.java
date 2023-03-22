package mods.clayium.machine.ClayFabricator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayFabricator extends ClayHorizontalNoRecipeMachine {
    public ClayFabricator(int tier) {
        super(TileEntityClayFabricator.class, EnumMachineKind.clayFabricator,
                tier == 8 ? "mk1" : tier == 9 ? "mk2" : tier == 13 ? "mk3" : "",
                GuiHandler.GuiIdClayMachines, tier);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayFabricator) {
            ((TileEntityClayFabricator) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.UP, 0);
            ((TileEntityClayFabricator) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.DOWN, 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
