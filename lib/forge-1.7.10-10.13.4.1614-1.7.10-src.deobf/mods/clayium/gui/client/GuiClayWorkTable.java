package mods.clayium.gui.client;

import codechicken.nei.recipe.GuiCraftingRecipe;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.container.ContainerClayWorkTable;
import mods.clayium.network.GuiButtonPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class GuiClayWorkTable
        extends GuiContainer {
    public static final ResourceLocation TEXTURE = new ResourceLocation("clayium", "textures/gui/clayworktable.png");
    private TileClayWorkTable tileClayWorkTable;

    public GuiClayWorkTable(int x, int y, int z) {
        super((Container) new ContainerClayWorkTable(x, y, z));
    }

    public GuiClayWorkTable(InventoryPlayer invPlayer, TileClayWorkTable tile) {
        super((Container) new ContainerClayWorkTable(invPlayer, tile));
        this.tileClayWorkTable = tile;
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        String string = this.tileClayWorkTable.hasCustomInventoryName() ? this.tileClayWorkTable.getInventoryName() : I18n.format(this.tileClayWorkTable.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(string, 6, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 94, 4210752);
    }


    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (this.tileClayWorkTable.isBurning()) {

            int j = 6;
            drawTexturedModalRect(k + 56, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }
        int i1 = this.tileClayWorkTable.getCookProgressScaled(80);
        drawTexturedModalRect(k + 48, l + 29, 176, 0, i1, 16);


        for (int i = 0; i < 6; i++) {
            ((GuiPictureButton) this.buttonList.get(i)).enabled = (this.tileClayWorkTable.canPushButton(i + 1) != 0);
        }
    }


    public boolean doesGuiPauseGame() {
        return false;
    }


    public void initGui() {
        super.initGui();


        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiPictureButton(1, k + 40, l + 52, 16, 16, TEXTURE, 176, 32));
        this.buttonList.add(new GuiPictureButton(2, k + 56, l + 52, 16, 16, TEXTURE, 192, 32));
        this.buttonList.add(new GuiPictureButton(3, k + 72, l + 52, 16, 16, TEXTURE, 208, 32));
        this.buttonList.add(new GuiPictureButton(4, k + 88, l + 52, 16, 16, TEXTURE, 224, 32));
        this.buttonList.add(new GuiPictureButton(5, k + 104, l + 52, 16, 16, TEXTURE, 240, 32));
        this.buttonList.add(new GuiPictureButton(6, k + 120, l + 52, 16, 16, TEXTURE, 176, 80));
    }


    protected void actionPerformed(GuiButton guibutton) {
        switch (guibutton.id) {

        }


        if (!guibutton.enabled) {
            return;
        }


        ClayiumCore.packetDispatcher.sendToServer((IMessage) new GuiButtonPacket(this.tileClayWorkTable.xCoord, this.tileClayWorkTable.yCoord, this.tileClayWorkTable.zCoord, guibutton.id));


        this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, guibutton.id);
    }


    public void mouseClicked(int mousex, int mousey, int button) {
        if (ClayiumCore.IntegrationID.NEI.loaded()) {
            int offsetX = (this.width - this.xSize) / 2;
            int offsetY = (this.height - this.ySize) / 2;
            int progressBarPosX = 48;
            int progressBarPosY = 33;
            int progressBarSizeX = 80;
            int progressBarSizeY = 12;
            if (offsetX + progressBarPosX <= mousex && offsetY + progressBarPosY <= mousey && offsetX + progressBarPosX + progressBarSizeX > mousex && offsetY + progressBarPosY + progressBarSizeY > mousey) {


                GuiCraftingRecipe.openRecipeGui("ClayWorkTable", new Object[0]);
                return;
            }
        }
        super.mouseClicked(mousex, mousey, button);
    }


    public void drawScreen(int mousex, int mousey, float p_73863_3_) {
        super.drawScreen(mousex, mousey, p_73863_3_);
        if (ClayiumCore.IntegrationID.NEI.loaded()) {
            int offsetX = (this.width - this.xSize) / 2;
            int offsetY = (this.height - this.ySize) / 2;
            int progressBarPosX = 48;
            int progressBarPosY = 33;
            int progressBarSizeX = 80;
            int progressBarSizeY = 12;
            if (offsetX + progressBarPosX <= mousex && offsetY + progressBarPosY <= mousey && offsetX + progressBarPosX + progressBarSizeX > mousex && offsetY + progressBarPosY + progressBarSizeY > mousey) {


                List<String> list = new ArrayList();
                list.add("Recipes");
                func_146283_a(list, mousex, mousey);
            }
        }
    }
}
