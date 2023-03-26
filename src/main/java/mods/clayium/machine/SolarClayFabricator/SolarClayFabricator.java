package mods.clayium.machine.SolarClayFabricator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.BlockStateClayContainer;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(BlockStateClayContainer.FACING, EnumFacing.UP);
    }
}
