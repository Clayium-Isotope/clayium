package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
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

public class Resonator extends BlockTiered implements ICAResonator {
    private final double resonance;

    public Resonator(int tier, double resonance) {
        super(Material.IRON, "resonator", tier);
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.resonance = resonance;
    }

    @Override
    public double getResonance(IBlockAccess world, BlockPos pos) {
        return this.getResonance();
    }

    public double getResonance() {
        return this.resonance;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.Resonator");
        super.addInformation(stack, player, tooltip, advanced);
        if (UtilLocale.canLocalize("tooltip.Resonator.resonance")) {
            tooltip.add(UtilLocale.localizeAndFormat("tooltip.Resonator.resonance", this.getResonance()));
        }
    }
}
