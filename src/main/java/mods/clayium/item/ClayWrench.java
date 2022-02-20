package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.util.UtilBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayWrench extends ClayiumItem {
    public ClayWrench() {
        super("clay_wrench");
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return UtilBuilder.rotateBlockByWrench(world, pos, side) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }
}
