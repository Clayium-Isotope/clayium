package mods.clayium.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.util.TierPrefix;

public class SiliconeColored extends BlockTiered {

    public final EnumDyeColor color;

    public SiliconeColored(EnumDyeColor color) {
        super(Material.IRON, "silicone_" + color.getName(), TierPrefix.advanced, MapColor.getBlockColor(color));
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.color = color;
    }
}
