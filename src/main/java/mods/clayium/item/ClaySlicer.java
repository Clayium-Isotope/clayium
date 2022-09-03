package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import net.minecraft.entity.player.EntityPlayer;
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
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayContainer) {
            TileEntityClayContainer tecc = (TileEntityClayContainer) worldIn.getTileEntity(pos);
            int dist = tecc.exportRoutes.get(facing) + 1;

            if (tecc.listSlotsExport.size() <= dist) {
                dist = -1;
                player.sendMessage(new TextComponentString("Disabled"));
            } else {
                player.sendMessage(new TextComponentString("Set extract route " + dist));
            }
            tecc.exportRoutes.replace(facing, dist);

            tecc.updateEntity();
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
