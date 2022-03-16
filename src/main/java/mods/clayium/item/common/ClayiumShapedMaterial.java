package mods.clayium.item.common;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ClayiumShapedMaterial extends ItemTiered implements IItemColor {
    private final ClayiumMaterial material;

    public ClayiumShapedMaterial(ClayiumMaterial material, ClayiumShape shape, int tier) {
        super(material, shape, tier);
        this.material = material;
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        int[] tint = material.getColors()[tintIndex];
        return new Color(tint[0], tint[1], tint[2]).getRGB();
    }
}
