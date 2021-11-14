package mods.clayium.render.item;

import java.util.Random;

import mods.clayium.item.IItemExRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemDamagedRenderer implements IItemRenderer {
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        switch (type) {
            case ENTITY:
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
            case FIRST_PERSON_MAP:
                return true;

            case INVENTORY:
                return false;
        }

        return false;
    }


    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        switch (helper) {
            case ENTITY_ROTATION:
                return this.renderManager.options.fancyGraphics;

            case ENTITY_BOBBING:
            case EQUIPPED_BLOCK:
                return true;

            case BLOCK_3D:
            case INVENTORY_BLOCK:
                return false;
        }

        return false;
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemstack, Object... data) {
        RenderBlocks render;
        EntityItem entity;
        Item item;
        int j;
        RenderBlocks render1;
        EntityLivingBase entity1;
        Item item1;
        int x;
        RenderBlocks render2;
        EntityLivingBase entity2;
        Item item2;
        int i;
        RenderBlocks render3;
        Item item3;

        TextureManager texturemanager;
        RenderItem renderi;
        Tessellator tessellator;
        int l;
        EntityPlayer player;
        TextureManager engine;
        MapData mapData;
        byte b0 = 1;

        if (itemstack.stackSize > 1) b0 = 2;
        if (itemstack.stackSize > 5) b0 = 3;
        if (itemstack.stackSize > 20) b0 = 4;
        if (itemstack.stackSize > 40) b0 = 5;
        b0 = getMiniItemCount(itemstack, b0);

        switch (type) {
            case ENTITY:
                render = (RenderBlocks) data[0];
                entity = (EntityItem) data[1];

                GL11.glTranslatef(0.0F, -0.05F, 0.0F);

                item = itemstack.getItem();
                if (item instanceof IItemExRenderer) {
                    ((IItemExRenderer) item).preRenderItem(type, itemstack, data);
                }

                for (j = 0; j < item.getRenderPasses(itemstack.getItemDamage()); j++) {
                    this.random.setSeed(187L);
                    IIcon iicon1 = item.getIcon(itemstack, j);

                    int k = item.getColorFromItemStack(itemstack, j);
                    float f5 = (k >> 16 & 0xFF) / 255.0F;
                    float f6 = (k >> 8 & 0xFF) / 255.0F;
                    float f7 = (k & 0xFF) / 255.0F;
                    GL11.glColor4f(f5, f6, f7, 1.0F);

                    if (item instanceof IItemExRenderer)
                        ((IItemExRenderer) item).preRenderItemPass(type, itemstack, j, data);

                    renderDroppedItem(entity, iicon1, b0, 0.0F, f5, f6, f7, j);
                    if (item instanceof IItemExRenderer)
                        ((IItemExRenderer) item).postRenderItemPass(type, itemstack, j, data);
                }

                if (item instanceof IItemExRenderer)
                    ((IItemExRenderer) item).postRenderItem(type, itemstack, data);

                break;

            case FIRST_PERSON_MAP:
                render1 = (RenderBlocks) data[0];
                entity1 = (EntityLivingBase) data[1];

                GL11.glTranslatef(0.5F, 0.5F, 0.5F);

                item1 = itemstack.getItem();
                if (item1 instanceof IItemExRenderer)
                    ((IItemExRenderer) item1).preRenderItem(type, itemstack, data);

                for (x = 0; x < item1.getRenderPasses(itemstack.getItemDamage()); x++) {
                    int k1 = item1.getColorFromItemStack(itemstack, x);
                    float f10 = (k1 >> 16 & 0xFF) / 255.0F;
                    float f11 = (k1 >> 8 & 0xFF) / 255.0F;
                    float f12 = (k1 & 0xFF) / 255.0F;
                    GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);

                    if (item1 instanceof IItemExRenderer)
                        ((IItemExRenderer) item1).preRenderItemPass(type, itemstack, x, data);
                    renderItem(entity1, itemstack, x, IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
                    if (item1 instanceof IItemExRenderer)
                        ((IItemExRenderer) item1).postRenderItemPass(type, itemstack, x, data);
                }

                if (item1 instanceof IItemExRenderer) {
                    ((IItemExRenderer) item1).postRenderItem(type, itemstack, data);
                }
                break;

            case EQUIPPED_FIRST_PERSON:
                render2 = (RenderBlocks) data[0];
                entity2 = (EntityLivingBase) data[1];

                GL11.glTranslatef(0.5F, 0.5F, 0.5F);

                item2 = itemstack.getItem();
                if (item2 instanceof IItemExRenderer)
                    ((IItemExRenderer) item2).preRenderItem(type, itemstack, data);

                for (i = 0; i < item2.getRenderPasses(itemstack.getItemDamage()); i++) {
                    int k1 = item2.getColorFromItemStack(itemstack, i);
                    float f10 = (k1 >> 16 & 0xFF) / 255.0F;
                    float f11 = (k1 >> 8 & 0xFF) / 255.0F;
                    float f12 = (k1 & 0xFF) / 255.0F;
                    GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);

                    if (item2 instanceof IItemExRenderer)
                        ((IItemExRenderer) item2).preRenderItemPass(type, itemstack, i, data);
                    renderItem(entity2, itemstack, i, IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
                    if (item2 instanceof IItemExRenderer)
                        ((IItemExRenderer) item2).postRenderItemPass(type, itemstack, i, data);
                }
                if (item2 instanceof IItemExRenderer)
                    ((IItemExRenderer) item2).postRenderItem(type, itemstack, data);

                break;

            case EQUIPPED:
                render3 = (RenderBlocks) data[0];
                item3 = itemstack.getItem();

                texturemanager = Minecraft.getMinecraft().getTextureManager();
                renderi = RenderItem.getInstance();
                renderi.zLevel = 50.0F;

                OpenGlHelper.glBlendFunc(0, 0, 0, 0);
                GL11.glColorMask(false, false, false, true);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(-1);
                tessellator.addVertex(-2.0D, 18.0D, 0.0D);
                tessellator.addVertex(18.0D, 18.0D, 0.0D);
                tessellator.addVertex(18.0D, -2.0D, 0.0D);
                tessellator.addVertex(-2.0D, -2.0D, 0.0D);
                tessellator.draw();
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(3553);
                GL11.glEnable(3008);

                if (item3 instanceof IItemExRenderer)
                    ((IItemExRenderer) item3).preRenderItem(type, itemstack, data);

                for (l = 0; l < item3.getRenderPasses(itemstack.getItemDamage()); l++) {
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);

                    int i1 = item3.getColorFromItemStack(itemstack, l);
                    float f = (i1 >> 16 & 0xFF) / 255.0F;
                    float f1 = (i1 >> 8 & 0xFF) / 255.0F;
                    float f2 = (i1 & 0xFF) / 255.0F;

                    GL11.glColor4f(f, f1, f2, 1.0F);

                    GL11.glDisable(2896);
                    GL11.glEnable(3008);

                    if (item3 instanceof IItemExRenderer)
                        ((IItemExRenderer) item3).preRenderItemPass(type, itemstack, l, data);

                    texturemanager.bindTexture((itemstack.getItemSpriteNumber() == 0) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
                    IIcon iicon = item3.getIcon(itemstack, l);
                    if (iicon != null)
                        renderi.renderIcon(0, 0, iicon, 16, 16);
                    if (item3 instanceof IItemExRenderer)
                        ((IItemExRenderer) item3).postRenderItemPass(type, itemstack, l, data);

                    GL11.glDisable(3008);
                    GL11.glEnable(2896);

                    if (itemstack.hasEffect(l))
                        renderi.renderEffect(texturemanager, 0, 0);
                }

                if (item3 instanceof IItemExRenderer)
                    ((IItemExRenderer) item3).postRenderItem(type, itemstack, data);
                break;

            case INVENTORY:
                player = (EntityPlayer) data[0];
                engine = (TextureManager) data[1];
                mapData = (MapData) data[2];
                break;
        }
    }

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private RenderManager renderManager = RenderManager.instance;
    private Random random = new Random();

    private void renderDroppedItem(EntityItem p_77020_1_, IIcon p_77020_2_, int p_77020_3_, float p_77020_4_, float p_77020_5_, float p_77020_6_, float p_77020_7_, int pass) {
        TextureAtlasSprite textureAtlasSprite = null;
        Tessellator tessellator = Tessellator.instance;

        if (p_77020_2_ == null) {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(p_77020_1_.getEntityItem().getItemSpriteNumber());
            textureAtlasSprite = ((TextureMap) texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f14 = textureAtlasSprite.getMinU();
        float f15 = textureAtlasSprite.getMaxU();
        float f4 = textureAtlasSprite.getMinV();
        float f5 = textureAtlasSprite.getMaxV();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;

        if (this.renderManager.options.fancyGraphics) {
            GL11.glPushMatrix();

            if (RenderItem.renderInFrame) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            float f9 = 0.0625F;
            float f10 = 0.021875F;
            ItemStack itemstack = p_77020_1_.getEntityItem();
            int j = itemstack.stackSize;

            byte b0 = 0;

            if (j < 2) b0 = 1;
            else if (j < 16) b0 = 2;
            else if (j < 32) b0 = 3;
            else b0 = 4;

            b0 = getMiniItemCount(itemstack, b0);

            GL11.glTranslatef(-f7, -f8, -((f9 + f10) * b0 / 2.0F));

            for (int k = 0; k < b0; k++) {
                if (k > 0 && shouldSpreadItems()) {
                    float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(x, y, f9 + f10);
                } else {
                    GL11.glTranslatef(0.0F, 0.0F, f9 + f10);
                }

                if (itemstack.getItemSpriteNumber() == 0) {
                    bindTexture(TextureMap.locationBlocksTexture);
                } else {
                    bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, textureAtlasSprite.getIconWidth(), textureAtlasSprite.getIconHeight(), f9);

                if (itemstack.hasEffect(pass)) {
                    GL11.glDepthFunc(514);
                    GL11.glDisable(2896);
                    this.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(768, 1);
                    float f11 = 0.76F;
                    GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    float f12 = 0.125F;
                    GL11.glScalef(f12, f12, f12);
                    float f13 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f13, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f12, f12, f12);
                    f13 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f13, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(5888);
                    GL11.glDisable(3042);
                    GL11.glEnable(2896);
                    GL11.glDepthFunc(515);
                }
            }

            GL11.glPopMatrix();
        } else {
            p_77020_5_ = (float) (p_77020_5_ / 1.4D);
            p_77020_6_ = (float) (p_77020_6_ / 1.4D);
            p_77020_7_ = (float) (p_77020_7_ / 1.4D);

            for (int l = 0; l < p_77020_3_; l++) {
                GL11.glPushMatrix();

                if (l > 0) {
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f10, f16, f17);
                }

                if (!RenderItem.renderInFrame) {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                if (p_77020_1_.getEntityItem().getItemSpriteNumber() == 0) {
                    bindTexture(TextureMap.locationBlocksTexture);
                } else {
                    bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);

                tessellator.addVertexWithUV((0.0F - f7), (0.0F - f8), 0.0D, f14, f5);
                tessellator.addVertexWithUV((f6 - f7), (0.0F - f8), 0.0D, f15, f5);
                tessellator.addVertexWithUV((f6 - f7), (1.0F - f8), 0.0D, f15, f4);
                tessellator.addVertexWithUV((0.0F - f7), (1.0F - f8), 0.0D, f14, f4);

                tessellator.setNormal(0.0F, -1.0F, 0.0F);

                tessellator.addVertexWithUV((0.0F - f7), (1.0F - f8), 0.0D, f15, f4);
                tessellator.addVertexWithUV((f6 - f7), (1.0F - f8), 0.0D, f14, f4);
                tessellator.addVertexWithUV((f6 - f7), (0.0F - f8), 0.0D, f14, f5);
                tessellator.addVertexWithUV((0.0F - f7), (0.0F - f8), 0.0D, f15, f5);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
    }

    protected void bindTexture(ResourceLocation p_110776_1_) {
        this.renderManager.renderEngine.bindTexture(p_110776_1_);
    }

    public byte getMiniItemCount(ItemStack stack, byte original) {
        return original;
    }

    public boolean shouldSpreadItems() {
        return true;
    }

    private RenderBlocks renderBlocksIr = new RenderBlocks();

    public void renderItem(EntityLivingBase p_78443_1_, ItemStack p_78443_2_, int p_78443_3_, IItemRenderer.ItemRenderType type) {
        GL11.glPushMatrix();
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();

        IIcon iicon = p_78443_1_.getItemIcon(p_78443_2_, p_78443_3_);

        if (iicon == null) {
            GL11.glPopMatrix();
            return;
        }

        texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
        TextureUtil.func_152777_a(false, false, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        float f = iicon.getMinU();
        float f1 = iicon.getMaxU();
        float f2 = iicon.getMinV();
        float f3 = iicon.getMaxV();
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(32826);
        GL11.glTranslatef(-f4, -f5, 0.0F);
        float f6 = 1.5F;
        GL11.glScalef(f6, f6, f6);
        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
        ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);

        if (p_78443_2_.hasEffect(p_78443_3_)) {

            GL11.glDepthFunc(514);
            GL11.glDisable(2896);
            texturemanager.bindTexture(RES_ITEM_GLINT);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(768, 1, 1, 0);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
        }

        GL11.glDisable(32826);
        texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
        TextureUtil.func_147945_b();

        GL11.glPopMatrix();
    }
}
