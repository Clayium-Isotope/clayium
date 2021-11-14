package mods.clayium.gui;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class MultipleIcon
        implements IMultipleRenderIcons {
    int[] colors;
    IIcon[] iicons;
    String[] iconStrings;
    public int pos;

    public MultipleIcon(int passes) {
        this.colors = new int[passes];
        this.iconStrings = new String[passes];
    }

    public MultipleIcon addIcon(String iconString) {
        return setIcon(this.pos, iconString);
    }

    public MultipleIcon setIcon(int i, String iconString) {
        return setIcon(i, iconString, 255, 255, 255);
    }

    public MultipleIcon addIcon(String iconString, int r, int g, int b) {
        return setIcon(this.pos, iconString, r, g, b);
    }

    public MultipleIcon setIcon(int i, String iconString, int r, int g, int b) {
        return setIcon(i, iconString, GenericMaterialIcon.rgb2int(r, g, b));
    }

    public MultipleIcon addIcon(String iconString, int color) {
        return setIcon(this.pos, iconString, color);
    }

    public MultipleIcon setIcon(int i, String iconString, int color) {
        setColor(i, color);
        this.iconStrings[i] = iconString;
        this.pos++;
        return this;
    }

    public MultipleIcon addIcons(String... iconStrings) {
        for (String string : iconStrings)
            setIcon(this.pos, string);
        return this;
    }

    public MultipleIcon setColor(int i, int r, int g, int b) {
        return setColor(i, GenericMaterialIcon.rgb2int(r, g, b));
    }

    public MultipleIcon setColor(int i, int color) {
        this.colors[i] = color;
        return this;
    }


    public int getRenderPasses() {
        return this.iicons.length;
    }


    public IIcon getIconFromPass(int pass) {
        return this.iicons[pass];
    }


    public int getColorFromPass(int pass) {
        return this.colors[pass];
    }


    public void registerIcons(IIconRegister iiconRegister) {
        this.iicons = new IIcon[this.iconStrings.length];
        for (int i = 0; i < this.iicons.length; i++)
            this.iicons[i] = iiconRegister.registerIcon("clayium:" + this.iconStrings[i]);
    }
}
