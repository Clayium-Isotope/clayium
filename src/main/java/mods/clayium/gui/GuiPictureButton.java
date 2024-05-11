package mods.clayium.gui;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.util.ResourceLocation;

public class GuiPictureButton extends GuiButtonToggle {

    private int xTex, yTex;

    public GuiPictureButton(int id, int xPos, int yPos, int xTex, int yTex) {
        super(id, xPos, yPos, 16, 16, true);
        initTextureValues(0, 0, 16, 16, new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
        this.xTex = xTex;
        this.yTex = yTex;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (visible) {
            stateTriggered = enabled;
            super.drawButton(mc, mouseX, mouseY, partial);

            mc.getTextureManager().bindTexture(resourceLocation);
            drawTexturedModalRect(x, y, xTex, yTex, 16, 16);
        }
    }
}
