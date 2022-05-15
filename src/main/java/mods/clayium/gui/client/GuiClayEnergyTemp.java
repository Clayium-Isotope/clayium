package mods.clayium.gui.client;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;


public class GuiClayEnergyTemp
        extends GuiClayContainerTemp {
    public GuiClayEnergyTemp(ContainerTemp container, TileClayContainer tile, Block block) {
        super(container, tile, block);
    }


    protected void drawClayEnergyForegroundLayer() {
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        this.fontRendererObj.drawString(I18n.format("gui.Common.energy", UtilLocale.ClayEnergyNumeral(this.tile.clayEnergy, false)), 4, container.machineGuiSizeY - 12, 4210752);
    }


    protected void drawClayEnergyItem(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        if (!isShiftKeyDown())
            return;
        if (this.tile == null)
            return;
        if (this.tile.containerItemStacks == null)
            return;
        if (this.tile.containerItemStacks.length <= this.tile.clayEnergySlot || this.tile.clayEnergySlot < 0)
            return;
        ItemStack itemstack = this.tile.containerItemStacks[this.tile.clayEnergySlot];
        if (itemstack == null)
            return;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        int itemX = -12;
        int itemY = ((ContainerTemp) this.inventorySlots).machineGuiSizeY - 16;

        GL11.glPushMatrix();

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2896);
        itemRender.zLevel = 100.0F;
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, k + itemX, l + itemY);
        itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, k + itemX, l + itemY);

        itemRender.zLevel = 0.0F;
        GL11.glDisable(2896);

        if (func_146978_c(itemX, itemY, 16, 16, p_73863_1_, p_73863_2_)) {
            renderToolTip(itemstack, p_73863_1_, p_73863_2_);
        }

        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        RenderHelper.enableStandardItemLighting();
    }
}
