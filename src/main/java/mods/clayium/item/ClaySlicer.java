package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UtilDirection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ClaySlicer extends ClayiumItem implements IModifyCC {

    public ClaySlicer() {
        super("clay_slicer");
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

        if (worldIn.getTileEntity(pos) instanceof IClayInventory) {
            IClayInventory tecc = (IClayInventory) worldIn.getTileEntity(pos);
            EnumSide side = UtilDirection.getSideOfDirection(tecc.getFront(), facing);
            int dist = tecc.getExportRoute(side) + 1;

            if (tecc.getListSlotsExport().size() <= dist || tecc.getListSlotsExport().get(0).length >= 2 &&
                    dist == tecc.getListSlotsExport().get(0).length + 1) {
                dist = IClayInventory.NONE_ROUTE;
                player.sendMessage(new TextComponentString("Disabled"));
            } else {
                player.sendMessage(new TextComponentString("Set extract route " + dist));
            }
            tecc.setExportRoute(side, dist);

            tecc.markDirty();
            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return IModifyCC.super.getContainerItem(itemStack);
    }
}
