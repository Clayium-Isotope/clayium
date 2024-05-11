package mods.clayium.block.itemblock;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.item.common.ITieredItem;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.util.TierPrefix;
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
    protected final TierPrefix tier;

    public ItemBlockTiered(Block block) {
        super(block);
        if (!(block instanceof ITieredBlock))
            throw new IllegalArgumentException("Expect ITieredBlock but " + block.getClass().getName());
        tier = ((ITieredBlock) block).getTier(ItemStack.EMPTY);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        this.block.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(ItemTiered.getTieredToolTip(tier));
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        switch (this.tier) {
            case none:
            case clay:
            case denseClay:
            case simple:
            default:
                return EnumRarity.COMMON;
            case basic:
            case advanced:
            case precision:
            case claySteel:
                return EnumRarity.UNCOMMON;
            case clayium:
            case ultimate:
            case antimatter:
            case pureAntimatter:
                return EnumRarity.RARE;
            case OEC:
            case OPA:
                return EnumRarity.EPIC;
        }
    }

    @Override
    public TierPrefix getTier() {
        return tier;
    }
}
