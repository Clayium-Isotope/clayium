package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ClayRollingPin extends ClayiumItem implements IModifyCC {
    public ClayRollingPin() {
        super("clay_rolling_pin");
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayContainer) {
            TileEntityClayContainer tecc = (TileEntityClayContainer) worldIn.getTileEntity(pos);

            int dist = tecc.importRoutes.get(facing) + 1;
            if (tecc.listSlotsImport.size() <= dist) {
                if (tecc instanceof TileEntityClayiumMachine)
                    dist = -2;
                else
                    dist = -1;
            }

            switch (dist) {
                case -2:
                    player.sendMessage(new TextComponentString("Set insert route energy"));
                    break;
                case -1:
                    player.sendMessage(new TextComponentString("Disabled"));
                    break;
                default:
                    player.sendMessage(new TextComponentString("Set insert route " + tecc.importRoutes.get(facing)));
                    break;
            }

            tecc.importRoutes.replace(facing, dist);
            tecc.updateEntity();
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
