package mods.clayium.item.common;

import net.minecraft.client.resources.I18n;
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
    private final int tier;

    public ItemTiered(String modelPath, int tier) {
        super(modelPath + (modelPath.endsWith("_") ? tier : ""));
        this.tier = tier;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (tier >= 0) {
            tooltip.add(getTieredToolTip(tier));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static String getTieredToolTip(int tier) {
        return TextFormatting.WHITE + I18n.format("gui.Common.tier", tier) + TextFormatting.RESET;
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
