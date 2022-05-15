package mods.clayium.gui;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public interface IMultipleRenderIcons {
    int getRenderPasses();

    IIcon getIconFromPass(int paramInt);

    int getColorFromPass(int paramInt);

    void registerIcons(IIconRegister paramIIconRegister);
}
