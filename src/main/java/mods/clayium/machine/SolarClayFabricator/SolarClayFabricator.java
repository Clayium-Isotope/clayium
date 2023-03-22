package mods.clayium.machine.SolarClayFabricator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.BlockStateClayDirectionalContainer;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SolarClayFabricator extends ClayDirectionalNoRecipeMachine {
    public SolarClayFabricator(int tier) {
        super(TileEntitySolarClayFabricator.class, EnumMachineKind.solarClayFabricator,
                tier == 5 ? "mk1" : tier == 6 ? "mk2" : tier == 7 ? "mk3" : "",
                GuiHandler.GuiIdClayMachines, tier);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySolarClayFabricator) {
            ((TileEntitySolarClayFabricator) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite(), 0);
            ((TileEntitySolarClayFabricator) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer), 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state.withProperty(BlockStateClayDirectionalContainer.FACING, EnumFacing.UP), 2);
        }
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(BlockStateClayDirectionalContainer.FACING, EnumFacing.UP);
    }
}
