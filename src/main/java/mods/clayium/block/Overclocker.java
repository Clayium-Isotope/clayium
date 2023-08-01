package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Overclocker extends BlockTiered implements IOverclocker {
    private final double overclockFactor;

    public Overclocker(int meta, TierPrefix tier, double overclockFactor) {
        super(Material.IRON, "overclocker_", meta, tier);
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.overclockFactor = overclockFactor;
    }

    @Override
    public double getOverclockFactor(IBlockAccess world, BlockPos pos) {
        return getOverclockFactor();
    }

    public double getOverclockFactor() {
        return overclockFactor;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.Overclocker");
        super.addInformation(stack, player, tooltip, advanced);
        if (UtilLocale.canLocalize("tooltip.Overclocker.overclockFactor")) {
            tooltip.add(UtilLocale.localizeAndFormat("tooltip.Overclocker.overclockFactor", getOverclockFactor()));
        }
    }
}
