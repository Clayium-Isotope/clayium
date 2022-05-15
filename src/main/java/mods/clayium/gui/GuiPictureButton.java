package mods.clayium.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;


public class GuiPictureButton
        extends GuiButtonExt {
    protected ITexture[] textures;

    public GuiPictureButton(int id, int xPos, int yPos, int width, int height, String displaystring, ResourceLocation _buttonTextures, int _rXPos, int _rYPos) {
        super(id, xPos, yPos, width, height, displaystring);

        setTexture(_buttonTextures, _rXPos, _rYPos);
    }

    public GuiPictureButton(int id, int xPos, int yPos, int width, int height, ResourceLocation _buttonTextures, int _rXPos, int _rYPos) {
        this(id, xPos, yPos, width, height, "", _buttonTextures, _rXPos, _rYPos);
    }


    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {

            this.field_146123_n = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            int k = getHoverState(this.field_146123_n);


            if (this.textures != null && k >= 0 && k < this.textures.length && this.textures[k] != null) {
                this.textures[k].draw((Gui) this, this.xPosition, this.yPosition);
            }
            mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            if (this.packedFGColour != 0) {

                color = this.packedFGColour;
            } else if (!this.enabled) {

                color = 10526880;
            } else if (this.field_146123_n) {

                color = 16777120;
            }

            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

            if (strWidth > this.width - 6 && strWidth > ellipsisWidth) {
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, this.width - 6 - ellipsisWidth).trim() + "...";
            }
            drawCenteredString(mc.fontRenderer, buttonText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color);
        }
    }

    public void setTexture(ITexture tex, int hoverState) {
        if (this.textures == null)
            this.textures = new ITexture[3];
        if (hoverState < 0 || hoverState >= this.textures.length)
            return;
        this.textures[hoverState] = tex;
    }

    public void setTexture(ResourceLocation _buttonTextures, int _rXPos, int _rYPos, int hoverState) {
        setTexture(new RectangleTexture(_buttonTextures, this.width, this.height, _rXPos, _rYPos), hoverState);
    }

    public void setTexture(ResourceLocation _buttonTextures, int _rXPos, int _rYPos) {
        for (int i = 0; i < 3; i++)
            setTexture(_buttonTextures, _rXPos, _rYPos + this.height * i, i);
    }
}
