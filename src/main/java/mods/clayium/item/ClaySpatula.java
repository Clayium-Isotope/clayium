package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.ClayContainer.BlockStateClayContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClaySpatula extends ClayiumItem implements IModifyCC {
    public ClaySpatula() {
        super("clay_spatula");
        setMaxDamage(36);
        setMaxStackSize(1);
        setContainerItem(this);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

        // from BlockStateClayContainer#checkSurroundConnection
        if (worldIn.getBlockState(pos) instanceof BlockStateClayContainer) {
            TileEntity here = worldIn.getTileEntity(pos);

            NBTTagCompound tag = here.writeToNBT(new NBTTagCompound());

            BlockStateClayContainer state = (BlockStateClayContainer) worldIn.getBlockState(pos);
            worldIn.setBlockState(pos, state.reverseIsPipe(), 3);

            for (EnumFacing side : EnumFacing.VALUES) {
                BlockStateClayContainer.changeConnectionState(worldIn, pos, here, side);
            }
            worldIn.getTileEntity(pos).readFromNBT(tag);

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return IModifyCC.super.getContainerItem(itemStack);
    }
}
