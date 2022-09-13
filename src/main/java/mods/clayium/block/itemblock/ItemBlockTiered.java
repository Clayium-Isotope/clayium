package mods.clayium.block.itemblock;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.item.common.ITieredItem;
import mods.clayium.item.common.ItemTiered;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockTiered extends ItemBlock implements ITieredItem {
    private final int tier;

    public ItemBlockTiered(Block block) {
        super(block);
        tier = block instanceof ITieredBlock ? ((ITieredBlock) block).getTier(ItemStack.EMPTY) : -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        this.block.addInformation(stack, worldIn, tooltip, flagIn);

        if (tier >= 0) {
            tooltip.add(ItemTiered.getTieredToolTip(tier));
        }
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
}
