package mods.clayium.block.itemblock;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.item.common.ITieredItem;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockTiered extends ItemBlock implements ITieredItem {
    private final int tier;

    public ItemBlockTiered(BlockTiered block) {
        super(block);
        tier = block.getTier(ItemStack.EMPTY);
    }

    public ItemBlockTiered(Block block) {
        super(block);
        tier = -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (tier >= 0) {
            tooltip.add(ItemTiered.getTieredToolTip(tier));
        }

        UtilLocale.localizeTooltip(tooltip, getUnlocalizedName());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        if (0 <= tier && tier <= 3) return EnumRarity.COMMON;
        if (4 <= tier && tier <= 7) return EnumRarity.UNCOMMON;
        if (8 <= tier && tier <= 11) return EnumRarity.RARE;
        if (12 <= tier && tier <= 15) return EnumRarity.EPIC;

        return EnumRarity.COMMON;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        return player.isSneaking()
//            && replace(stack, player, world, pos, side, hitX, hitY, hitZ)
            && canPlaceBlockOnSide(world, pos, side, player, stack);
    }
}
