package mods.clayium.item.common;

import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTiered extends ClayiumItem implements ITieredItem {

    private TierPrefix tier;

    public ItemTiered(String modelPath, TierPrefix tier) {
        super(modelPath + (modelPath.endsWith("_") ? tier.meta() : ""));
        this.tier = tier;
    }

    public ItemTiered(String modelPath, TierPrefix tier, int meta) {
        super(modelPath + (modelPath.endsWith("_") ? meta : ""));
        this.tier = tier;
    }

    public ItemTiered(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier) {
        super(material, shape);
        this.tier = tier;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(getTieredToolTip(this.tier));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static String getTieredToolTip(TierPrefix tier) {
        return TextFormatting.WHITE + UtilLocale.tierGui(tier) + TextFormatting.RESET;
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

    public ItemTiered setTier(TierPrefix tier) {
        if (tier.isValid())
            this.tier = tier;
        return this;
    }
}
