package mods.clayium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;

public class RectangleTexture implements ITexture {
    private final int sizeX;
    private final int sizeY;
    private final int textureX;
    private final int textureY;
    private final ResourceLocation location;
    private TextureManager tm;

    public RectangleTexture(ResourceLocation location, int sizeX, int sizeY, int textureX, int textureY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.textureX = textureX;
        this.textureY = textureY;
        this.location = location;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public void draw(Gui gui, int posX, int posY) {
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.tm == null) {
            this.tm = Minecraft.getMinecraft().getTextureManager();
        }

        this.tm.bindTexture(this.location);
        gui.drawTexturedModalRect(posX, posY, this.textureX, this.textureY, this.sizeX, this.sizeY);
    }
}
