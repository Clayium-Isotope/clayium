package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.common.IClayInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RawClayTools extends ClayiumItem {

    public RawClayTools(String modelPath) {
        super(modelPath);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof IClayInventory) {
            IClayInventory tecc = (IClayInventory) worldIn.getTileEntity(pos);

            ItemStack filter = tecc.getFilters().get(facing);
            if (!filter.isEmpty()) {
                player.sendMessage(new TextComponentString("Removed " + filter.getDisplayName()));
                List<String> list = new ArrayList<>();
                ((IFilter) filter.getItem()).addFilterInformation(filter, player, list, true);

                for (String s : list) {
                    player.sendMessage(new TextComponentString(" " + s));
                }

                tecc.getFilters().put(facing, ItemStack.EMPTY);

                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
