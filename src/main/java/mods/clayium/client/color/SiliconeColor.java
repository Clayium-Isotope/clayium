package mods.clayium.client.color;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.SiliconeColored;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class SiliconeColor implements IBlockColor, IItemColor {

    private final int colorVal;

    SiliconeColor(EnumDyeColor color) {
        int r = color.getColorValue() >> 16 & 0xff;
        int g = color.getColorValue() >> 8 & 0xff;
        int b = color.getColorValue() & 0xff;
        r = (r * 3) / 4;
        g = (g * 3) / 4;
        b = (b * 3) / 4;
        colorVal = (r << 16) + (g << 8) + b;
    }

    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos,
                               int tintIndex) {
        return this.colorVal;
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return this.colorVal;
    }

    @SubscribeEvent
    public static void registerMaterialColor(ColorHandlerEvent.Item event) {
        registerColorHandler(event.getItemColors(), event.getBlockColors());
    }

    public static void registerColorHandler(ItemColors item, BlockColors block) {
        for (Block elm : ClayiumBlocks.siliconeColored) {
            assert elm instanceof SiliconeColored;

            item.registerItemColorHandler(new SiliconeColor(((SiliconeColored) elm).color), elm);
            block.registerBlockColorHandler(new SiliconeColor(((SiliconeColored) elm).color), elm);
        }
    }
}
