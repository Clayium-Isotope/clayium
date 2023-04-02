package mods.clayium.item;

import mods.clayium.item.common.IModifyCC;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.machine.Interface.ClayInterface.TileEntityClayInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SynchronousParts extends ItemTiered implements IModifyCC {
    public SynchronousParts() {
        super("synchronous_parts", 9);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayInterface) {
            if (((TileEntityClayInterface) worldIn.getTileEntity(pos)).markEnableSync()) {
                player.getHeldItem(hand).shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack;
    }
}
