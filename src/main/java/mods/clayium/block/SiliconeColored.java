package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class SiliconeColored extends BlockTiered implements IBlockColor {
    public SiliconeColored(EnumDyeColor color) {
        super(Material.IRON, color + "_silicone", 5, MapColor.getBlockColor(color));
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);
    }

    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
        int r = blockMapColor.colorValue >> 16 & 0xFF;
        int g = blockMapColor.colorValue >> 8 & 0xFF;
        int b = blockMapColor.colorValue >> 0 & 0xFF;
        r = (r * 3 + 256) / 4;
        g = (g * 3 + 256) / 4;
        b = (b * 3 + 256) / 4;
        return (r << 16) + (g << 8) + b;
    }
}
