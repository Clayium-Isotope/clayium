package mods.clayium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiPictureButton extends GuiButtonExt {
    private final ITexture[] textures = new ITexture[3];

    public GuiPictureButton(int id, int xPos, int yPos, int width, int height, String displayString, ResourceLocation _buttonTextures, int _rXPos, int _rYPos) {
        super(id, xPos, yPos, width, height, displayString);
        for(int i = 0; i < 3; ++i) {
            this.textures[i] = new RectangleTexture(_buttonTextures, xPos, yPos, _rXPos, _rYPos + height * i);
        }
    }

    public GuiPictureButton(int id, int xPos, int yPos, int width, int height, ResourceLocation _buttonTextures, int _rXPos, int _rYPos) {
        this(id, xPos, yPos, width, height, "", _buttonTextures, _rXPos, _rYPos);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (!this.visible) {
            return;
        }

        this.hovered = mouseX >= this.x && mouseX < this.x + this.width
                && mouseY >= this.y && mouseY < this.y + this.height;
        int state = this.getHoverState(this.hovered);
        if (state >= 0 && state < this.textures.length && this.textures[state] != null) {
            this.textures[state].draw(this, this.x, this.y);
        }

        this.mouseDragged(mc, mouseX, mouseY);
        int color = 14737632;
        if (this.packedFGColour != 0) {
            color = this.packedFGColour;
        } else if (!this.enabled) {
            color = 10526880;
        } else if (this.hovered) {
            color = 16777120;
        }

        String buttonText = this.displayString;
        int strWidth = mc.fontRenderer.getStringWidth(buttonText);
        int ellipsisWidth = mc.fontRenderer.getStringWidth("...");
        if (strWidth > this.width - 6 && strWidth > ellipsisWidth) {
            buttonText = mc.fontRenderer.trimStringToWidth(buttonText, this.width - 6 - ellipsisWidth).trim() + "...";
        }

        this.drawCenteredString(mc.fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
    }
}
