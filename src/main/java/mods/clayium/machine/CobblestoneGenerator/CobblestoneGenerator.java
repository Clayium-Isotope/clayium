package mods.clayium.machine.CobblestoneGenerator;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CobblestoneGenerator extends ClayDirectionalNoRecipeMachine {
    public CobblestoneGenerator(int tier) {
        super(TileEntityCobblestoneGenerator.class, EnumMachineKind.cobblestoneGenerator, tier);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityCobblestoneGenerator) {
            ((TileEntityCobblestoneGenerator) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer), 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
