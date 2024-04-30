package mods.clayium.item.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import mods.clayium.util.UtilBuilder;

public class ClayPipingTools extends ClayiumItem {

    public ClayPipingTools(String name) {
        super(name);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
                                           float hitY, float hitZ, EnumHand hand) {
        return player.isSneaking() && UtilBuilder.rotateBlockByWrench(world, pos, side) ? EnumActionResult.SUCCESS :
                EnumActionResult.PASS;
    }
}
