package mods.clayium.gui;

import net.minecraft.client.gui.Gui;

public interface ITexture {
    int getSizeX();

    int getSizeY();

    void draw(Gui paramGui, int paramInt1, int paramInt2);
}
