package mods.clayium.machine.SaltExtractor;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaltExtractor extends ClayDirectionalNoRecipeMachine {
    public SaltExtractor(int tier) {
        super(TileEntitySaltExtractor.class, EnumMachineKind.saltExtractor, tier);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySaltExtractor) {
            ((TileEntitySaltExtractor) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite(), 0);
            ((TileEntitySaltExtractor) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer), 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
