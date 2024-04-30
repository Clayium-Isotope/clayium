package mods.clayium.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;

public class CAReactorCoil extends BlockTiered {

    public CAReactorCoil(int meta, TierPrefix tier) {
        super(Material.IRON, "ca_reactor_coil_", meta, tier);
        setHardness(8.0F);
        setResistance(5.0F);
        setSoundType(SoundType.METAL);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.CAReactorCoil");
        super.addInformation(stack, player, tooltip, advanced);
    }
}
