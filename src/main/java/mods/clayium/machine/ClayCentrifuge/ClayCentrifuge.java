package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayCentrifuge extends ClayiumMachine {
    public ClayCentrifuge(int tier) {
        super(EnumMachineKind.centrifuge, "", tier, TileEntityClayCentrifuge.class, GuiHandler.GuiIdClayCentrifuge);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayCentrifuge) {
            TileEntityClayCentrifuge tecc = (TileEntityClayCentrifuge) worldIn.getTileEntity(pos);
            tecc.importRoutes.replace(EnumFacing.UP, 0);
            tecc.importRoutes.replace(placer.getHorizontalFacing(), -2);
            tecc.exportRoutes.replace(EnumFacing.DOWN, 0);
        }
    }
}
