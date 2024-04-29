package mods.clayium.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UtilDirection;

public class ClayRollingPin extends ClayiumItem implements IModifyCC {

    public ClayRollingPin() {
        super("clay_rolling_pin");
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
        setHasSubtypes(false);
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

            int dist = tecc.getImportRoute(side) + 1;
            if (tecc.getListSlotsImport().size() <= dist) {
                if (IClayEnergyConsumer.hasClayEnergy(tecc))
                    dist = IClayInventory.ENERGY_ROUTE;
                else
                    dist = IClayInventory.NONE_ROUTE;
            }

            switch (dist) {
                case IClayInventory.ENERGY_ROUTE:
                    player.sendMessage(new TextComponentString("Set insert route energy"));
                    break;
                case IClayInventory.NONE_ROUTE:
                    player.sendMessage(new TextComponentString("Disabled"));
                    break;
                default:
                    player.sendMessage(new TextComponentString("Set insert route " + dist));
                    break;
            }

            tecc.setImportRoute(side, dist);
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
