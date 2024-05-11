package mods.clayium.gui;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RectangleTexture implements ITexture {

    private final int sizeX;
    private final int sizeY;
    private final int textureX;
    private final int textureY;
    private final ResourceLocation location;
    private TextureManager tm;
    static ResourceLocation SlotTexture = new ResourceLocation(ClayiumCore.ModId, "textures/gui/slot.png");
    public static ITexture SmallSlotTexture;
    public static ITexture LargeSlotTexture;
    public static ITexture SmallSlotImport1Texture;
    public static ITexture SmallSlotImport2Texture;
    public static ITexture SmallSlotExport1Texture;
    public static ITexture SmallSlotExport2Texture;
    public static ITexture SmallSlotEClayTexture;
    public static ITexture SmallSlotFilterTexture;
    public static ITexture[] SmallSlotMultitrackTextures;
    public static ITexture[] SmallSlotMultitrackFilterTextures;

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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.tm == null) {
            this.tm = Minecraft.getMinecraft().getTextureManager();
        }

        this.tm.bindTexture(this.location);
        gui.drawTexturedModalRect(posX, posY, this.textureX, this.textureY, this.sizeX, this.sizeY);
    }

    static {
        SmallSlotTexture = new RectangleTexture(SlotTexture, 18, 18, 0, 0);
        LargeSlotTexture = new RectangleTexture(SlotTexture, 26, 26, 0, 32);
        SmallSlotImport1Texture = new RectangleTexture(SlotTexture, 18, 18, 32, 0);
        SmallSlotImport2Texture = new RectangleTexture(SlotTexture, 18, 18, 32, 32);
        SmallSlotExport1Texture = new RectangleTexture(SlotTexture, 18, 18, 64, 0);
        SmallSlotExport2Texture = new RectangleTexture(SlotTexture, 18, 18, 64, 32);
        SmallSlotEClayTexture = new RectangleTexture(SlotTexture, 18, 18, 96, 0);
        SmallSlotFilterTexture = new RectangleTexture(SlotTexture, 18, 18, 96, 32);
        SmallSlotMultitrackTextures = new ITexture[6];
        SmallSlotMultitrackFilterTextures = new ITexture[6];

        for (int i = 0; i < 6; ++i) {
            SmallSlotMultitrackTextures[i] = new RectangleTexture(SlotTexture, 18, 18, 32 + i * 32, 96);
            SmallSlotMultitrackFilterTextures[i] = new RectangleTexture(SlotTexture, 18, 18, 32 + i * 32, 128);
        }
    }
}
