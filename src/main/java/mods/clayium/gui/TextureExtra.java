package mods.clayium.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class TextureExtra
        extends TextureAtlasSprite {
    private String[] iconNames;
    private String basePath = "textures/blocks";
    private static final Logger logger = LogManager.getLogger();

    public TextureExtra(String iconId, String... iconNames) {
        super(iconId);
        this.iconNames = iconNames;
    }

    private int[] colorTable;

    public void setColorTable(int[] colors) {
        this.colorTable = colors;
    }


    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }


    public boolean load(IResourceManager manager, ResourceLocation location) {
        int mipmapLevel = Math.max((Minecraft.getMinecraft()).gameSettings.mipmapLevels, 0);
        BufferedImage[] bufferedimage = new BufferedImage[1 + mipmapLevel];
        location = completeResourceLocation(new ResourceLocation(this.iconNames[0]), 0);


        try {
            for (int i = 0; i < this.iconNames.length; i++) {
                String iconName = this.iconNames[i];
                ResourceLocation location0 = completeResourceLocation(new ResourceLocation(iconName), 0);
                IResource iresource = manager.getResource(location0);
                BufferedImage[] abufferedimage = new BufferedImage[1 + mipmapLevel];
                abufferedimage[0] = ImageIO.read(iresource.getInputStream());
                if (this.colorTable != null && i < this.colorTable.length) {
                    abufferedimage[0] = recolorImage(abufferedimage[0], this.colorTable[i]);
                }
                if (bufferedimage[0] == null) {
                    bufferedimage[0] = abufferedimage[0];
                } else {
                    blendImages(bufferedimage[0], abufferedimage[0]);
                }
                TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource.getMetadata("texture");

                if (texturemetadatasection != null) {

                    List list = texturemetadatasection.getListMipmaps();


                    if (!list.isEmpty()) {

                        int k = abufferedimage[0].getWidth();
                        int l = abufferedimage[0].getHeight();

                        if (MathHelper.roundUpToPowerOfTwo(k) != k || MathHelper.roundUpToPowerOfTwo(l) != l) {
                            throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                        }
                    }

                    Iterator<Integer> iterator3 = list.iterator();

                    while (iterator3.hasNext()) {

                        int l = ((Integer) iterator3.next()).intValue();

                        if (l > 0 && l < abufferedimage.length - 1 && abufferedimage[l] == null) {

                            ResourceLocation resourcelocation2 = completeResourceLocation(location0, l);


                            try {
                                abufferedimage[l] = ImageIO.read(manager.getResource(resourcelocation2).getInputStream());
                                if (this.colorTable != null && i < this.colorTable.length) {
                                    abufferedimage[l] = recolorImage(abufferedimage[l], this.colorTable[i]);
                                }
                                if (bufferedimage[l] == null) {
                                    bufferedimage[l] = abufferedimage[l];
                                    continue;
                                }
                                blendImages(bufferedimage[l], abufferedimage[l]);

                            } catch (IOException ioexception) {

                                logger.error("Unable to load miplevel {} from: {}", new Object[] {Integer.valueOf(l), resourcelocation2, ioexception});
                            }
                        }
                    }
                }
            }
            AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection) manager.getResource(location).getMetadata("animation");
            loadSprite(bufferedimage, animationmetadatasection, ((Minecraft.getMinecraft()).gameSettings.anisotropicFiltering > 1.0F));

        } catch (RuntimeException runtimeexception) {


            FMLClientHandler.instance().trackBrokenTexture(location, runtimeexception.getMessage());
            return false;
        } catch (IOException ioexception1) {


            FMLClientHandler.instance().trackMissingTexture(location);
            return false;
        }


        return false;
    }

    public static void blendImages(BufferedImage image0, BufferedImage image1) {
        for (int x = 0; x < image0.getWidth(); x++) {
            for (int y = 0; y < image0.getHeight(); y++) {
                int c0 = image0.getRGB(x, y);
                int r0 = c0 >> 16 & 0xFF;
                int g0 = c0 >> 8 & 0xFF;
                int b0 = c0 & 0xFF;
                int a0 = c0 >> 24 & 0xFF;
                int c1 = image1.getRGB(x, y);
                int r1 = c1 >> 16 & 0xFF;
                int g1 = c1 >> 8 & 0xFF;
                int b1 = c1 & 0xFF;
                int a1 = c1 >> 24 & 0xFF;

                int a2 = a0 + a1 - a0 * a1 / 255;
                int r2 = (a2 == 0) ? r0 : (a0 * (255 - a1) * r0 / (255 * a0 + 255 * a1 - a0 * a1) + a1 * 255 * r1 / (255 * a0 + 255 * a1 - a0 * a1));
                int g2 = (a2 == 0) ? g0 : (a0 * (255 - a1) * g0 / (255 * a0 + 255 * a1 - a0 * a1) + a1 * 255 * g1 / (255 * a0 + 255 * a1 - a0 * a1));
                int b2 = (a2 == 0) ? b0 : (a0 * (255 - a1) * b0 / (255 * a0 + 255 * a1 - a0 * a1) + a1 * 255 * b1 / (255 * a0 + 255 * a1 - a0 * a1));
                image0.setRGB(x, y, (r2 << 16) + (g2 << 8) + b2 + (a2 << 24));
            }
        }
    }

    public static BufferedImage recolorImage(BufferedImage image0, int color) {
        BufferedImage ret = image0;
        for (int x = 0; x < image0.getWidth(); x++) {
            for (int y = 0; y < image0.getHeight(); y++) {
                int c0 = image0.getRGB(x, y);
                int r0 = c0 >> 16 & 0xFF;
                int g0 = c0 >> 8 & 0xFF;
                int b0 = c0 & 0xFF;
                int a0 = c0 >> 24 & 0xFF;
                int r1 = color >> 16 & 0xFF;
                int g1 = color >> 8 & 0xFF;
                int b1 = color & 0xFF;
                int a1 = color >> 24 & 0xFF;

                int a2 = a0 * a1 / 255;
                int r2 = r0 * r1 / 255;
                int g2 = g0 * g1 / 255;
                int b2 = b0 * b1 / 255;
                ret.setRGB(x, y, (r2 << 16) + (g2 << 8) + b2 + (a2 << 24));
            }
        }
        return ret;
    }


    private ResourceLocation completeResourceLocation(ResourceLocation p_147634_1_, int p_147634_2_) {
        return (p_147634_2_ == 0) ? new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath, p_147634_1_.getResourcePath(), ".png"})) : new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] {this.basePath, p_147634_1_.getResourcePath(), Integer.valueOf(p_147634_2_), ".png"}));
    }

    public IIcon register(IIconRegister par1IconRegister) {
        if (par1IconRegister instanceof TextureMap) {
            String name = getIconName();
            if (name == null) {
                throw new IllegalArgumentException("Name cannot be null!");
            }
            if (name.indexOf('\\') == -1) {

                Object object = ((TextureMap) par1IconRegister).getTextureExtry(name);
                if (object == null) {
                    ((TextureMap) par1IconRegister).setTextureEntry(name, this);
                }
                return (IIcon) this;
            }


            throw new IllegalArgumentException("Name cannot contain slashes!");
        }

        return null;
    }
}
