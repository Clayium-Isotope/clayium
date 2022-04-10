package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class SiliconeColored extends BlockTiered implements IBlockColor, IItemColor {
    private final EnumDyeColor color;
    private final int colorVal;

    public SiliconeColored(EnumDyeColor color) {
        super(Material.IRON,  "silicone_" + color.getName(), 5, MapColor.getBlockColor(color));
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.color = color;

        int r = color.getColorValue() >> 16 & 0xff;
        int g = color.getColorValue() >> 8 & 0xff;
        int b = color.getColorValue() & 0xff;
        r = (r * 3) / 4;
        g = (g * 3) / 4;
        b = (b * 3) / 4;
        colorVal = (r << 16) + (g << 8) + b;
    }

    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
        return colorVal;
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return colorVal;
    }

    @SubscribeEvent
    public void registerMaterialColor(ColorHandlerEvent.Item event){
        event.getItemColors().registerItemColorHandler(this, this);
        event.getBlockColors().registerBlockColorHandler(this, this);
    }
}
