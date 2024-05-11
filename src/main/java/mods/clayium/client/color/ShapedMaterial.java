package mods.clayium.client.color;

import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumShapedMaterial;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ShapedMaterial implements IItemColor {

    private final ClayiumShapedMaterial mat;

    public ShapedMaterial(ClayiumShapedMaterial mat) {
        this.mat = mat;
    }

    @SubscribeEvent
    public void registerMaterialColor(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler(this, this.mat);
    }

    @SideOnly(Side.CLIENT)
    public static void requestTint(ItemColors event) {
        for (ItemStack stack : ClayiumMaterials.getMaterials()) {
            if (stack.getItem() instanceof ClayiumShapedMaterial &&
                    ((ClayiumShapedMaterial) stack.getItem()).useGeneralIcon()) {
                // ((ClayiumShapedMaterial) stack.getItem()).registerMaterialColor(event);
                event.registerItemColorHandler(new ShapedMaterial((ClayiumShapedMaterial) stack.getItem()),
                        stack.getItem());
            }
        }
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        int[] tint = mat.getMaterial().getColors()[tintIndex];
        return new Color(tint[0], tint[1], tint[2]).getRGB();
    }
}
