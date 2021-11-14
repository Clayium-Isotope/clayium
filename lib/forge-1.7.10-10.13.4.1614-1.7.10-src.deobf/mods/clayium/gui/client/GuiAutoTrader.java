package mods.clayium.gui.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileAutoTrader;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.container.ContainerAutoTrader;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import org.lwjgl.opengl.GL11;

public class GuiAutoTrader
        extends GuiClayEnergyTemp {
    private static final ResourceLocation field_147038_v = new ResourceLocation("textures/gui/container/villager.png");

    private MerchantButton field_147043_x;
    private MerchantButton field_147042_y;
    private TileAutoTrader trader;
    private int field_147041_z;

    public GuiAutoTrader(ContainerAutoTrader container, TileAutoTrader tile, Block block) {
        super((ContainerTemp) container, (TileClayContainer) tile, block);
        this.trader = tile;
        this.overlayTexture = new ResourceLocation("clayium", "textures/gui/autotrader.png");
    }

    public void addButton() {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.buttonList.add(this.field_147043_x = new MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
        this.buttonList.add(this.field_147042_y = new MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
        this.field_147043_x.enabled = false;
        this.field_147042_y.enabled = false;
    }


    public void updateScreen() {
        super.updateScreen();
        if (this.trader.merchantRecipeList != null) {
            this.field_147043_x.enabled = (this.trader.currentRecipeIndex < this.trader.merchantRecipeList.size() - 1);
            this.field_147042_y.enabled = (this.trader.currentRecipeIndex > 0);
        } else {
            this.field_147043_x.enabled = false;
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseZ);
        if (this.trader.merchantRecipeList != null && !this.trader.merchantRecipeList.isEmpty()) {

            int i1 = this.trader.currentRecipeIndex;
            if (i1 >= 0 && i1 < this.trader.merchantRecipeList.size()) {
                MerchantRecipe merchantrecipe = (MerchantRecipe) this.trader.merchantRecipeList.get(i1);

                if (merchantrecipe.isRecipeDisabled()) {

                    this.mc.getTextureManager().bindTexture(field_147038_v);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(2896);
                    drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                    drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
                }
            }
        }
    }


    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        if (this.trader.merchantRecipeList != null && !this.trader.merchantRecipeList.isEmpty()) {

            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            int i1 = this.trader.currentRecipeIndex;
            if (i1 >= 0 && i1 < this.trader.merchantRecipeList.size()) {
                MerchantRecipe merchantrecipe = (MerchantRecipe) this.trader.merchantRecipeList.get(i1);
                GL11.glPushMatrix();
                ItemStack itemstack = merchantrecipe.getItemToBuy();
                ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
                ItemStack itemstack2 = merchantrecipe.getItemToSell();
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(2896);
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glEnable(2896);
                itemRender.zLevel = 100.0F;
                itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, k + 36, l + 24);
                itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, k + 36, l + 24);

                if (itemstack1 != null) {

                    itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack1, k + 62, l + 24);
                    itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack1, k + 62, l + 24);
                }

                itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack2, k + 120, l + 24);
                itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack2, k + 120, l + 24);
                itemRender.zLevel = 0.0F;
                GL11.glDisable(2896);

                if (func_146978_c(36, 24, 16, 16, p_73863_1_, p_73863_2_)) {

                    renderToolTip(itemstack, p_73863_1_, p_73863_2_);
                } else if (itemstack1 != null && func_146978_c(62, 24, 16, 16, p_73863_1_, p_73863_2_)) {

                    renderToolTip(itemstack1, p_73863_1_, p_73863_2_);
                } else if (func_146978_c(120, 24, 16, 16, p_73863_1_, p_73863_2_)) {

                    renderToolTip(itemstack2, p_73863_1_, p_73863_2_);
                }

                GL11.glPopMatrix();
                GL11.glEnable(2896);
                GL11.glEnable(2929);
                RenderHelper.enableStandardItemLighting();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class MerchantButton
            extends GuiButton {
        private final boolean field_146157_o;
        private static final String __OBFID = "CL_00000763";

        public MerchantButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_, boolean p_i1095_4_) {
            super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }


        public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
            if (this.visible) {

                p_146112_1_.getTextureManager().bindTexture(GuiAutoTrader.field_147038_v);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = (p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height);
                int k = 0;
                int l = 176;

                if (!this.enabled) {

                    l += this.width * 2;
                } else if (flag) {

                    l += this.width;
                }

                if (!this.field_146157_o) {
                    k += this.height;
                }

                drawTexturedModalRect(this.xPosition, this.yPosition, l, k, this.width, this.height);
            }
        }
    }
}
