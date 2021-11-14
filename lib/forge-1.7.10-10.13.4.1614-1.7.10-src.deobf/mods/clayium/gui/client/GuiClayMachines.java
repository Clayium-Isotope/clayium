package mods.clayium.gui.client;

import codechicken.nei.recipe.GuiCraftingRecipe;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.container.ContainerClayMachines;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilTier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;


public class GuiClayMachines
        extends GuiClayEnergyTemp {
    protected static ResourceLocation TEXTURE = new ResourceLocation("clayium", "textures/gui/clayworktable.png");


    protected int progressBarPosX;


    protected int progressBarPosY;


    protected int progressBarSizeX;


    protected int progressBarSizeY;


    public GuiClayMachines(InventoryPlayer invPlayer, TileClayMachines tile, Block block) {
        super((ContainerTemp) new ContainerClayMachines(invPlayer, tile, block), (TileClayContainer) tile, block);
    }

    public GuiClayMachines(ContainerTemp container, TileClayContainer tile, Block block) {
        super(container, tile, block);
    }


    public void drawScreen(int mousex, int mousey, float p_73863_3_) {
        super.drawScreen(mousex, mousey, p_73863_3_);
        calculateProgressBarOffsets();
        if (ClayiumCore.IntegrationID.NEI.loaded() && !getOutputId().equals("")) {
            int offsetX = (this.width - this.xSize) / 2;
            int offsetY = (this.height - this.ySize) / 2;
            if (offsetX + this.progressBarPosX <= mousex && offsetY + this.progressBarPosY <= mousey && offsetX + this.progressBarPosX + this.progressBarSizeX > mousex && offsetY + this.progressBarPosY + this.progressBarSizeY > mousey) {


                List<String> list = new ArrayList();
                list.add("Recipes");
                func_146283_a(list, mousex, mousey);
            }
        }
    }


    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseZ);


        calculateProgressBarOffsets();
        TileClayMachines tileClayMachines = (TileClayMachines) this.tile;

        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;

        ResourceLocation ProgressBarTexture = new ResourceLocation("clayium", "textures/gui/progressbarfurnace.png");


        calculateProgressBarOffsets();
        this.mc.getTextureManager().bindTexture(ProgressBarTexture);

        drawTexturedModalRect(offsetX + this.progressBarPosX, offsetY + this.progressBarPosY, 0, 0, this.progressBarSizeX, this.progressBarSizeY);
        drawTexturedModalRect(offsetX + this.progressBarPosX, offsetY + this.progressBarPosY, 0, this.progressBarSizeY, tileClayMachines.getCraftProgressScaled(this.progressBarSizeX), this.progressBarSizeY);


        drawButton();
    }


    public void addButton() {
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        TileClayMachines tileClayMachines = (TileClayMachines) this.tile;
        ContainerClayMachines container = (ContainerClayMachines) this.inventorySlots;
        if (UtilTier.canManufactualCraft(tileClayMachines.getTier())) {
            this.buttonList.add(new GuiPictureButton(1, offsetX + (container.machineGuiSizeX - 16) / 2, offsetY + 56, 16, 16, GuiTemp.ButtonTexture, 0, 0));
        }
    }

    public void drawButton() {
        TileClayMachines tileClayMachines = (TileClayMachines) this.tile;
        if (UtilTier.canManufactualCraft(tileClayMachines.getTier()))
            ((GuiPictureButton) this.buttonList.get(0)).enabled = (tileClayMachines.canPushButton() != 0);
    }

    protected boolean contains(Block[] blocks, Block block) {
        for (Block b : blocks) {
            if (b == block)
                return true;
        }
        return false;
    }


    public void mouseClicked(int mousex, int mousey, int button) {
        calculateProgressBarOffsets();
        String outputId = getOutputId();
        if (ClayiumCore.IntegrationID.NEI.loaded() && !outputId.equals("")) {
            int offsetX = (this.width - this.xSize) / 2;
            int offsetY = (this.height - this.ySize) / 2;
            if (offsetX + this.progressBarPosX <= mousex && offsetY + this.progressBarPosY <= mousey && offsetX + this.progressBarPosX + this.progressBarSizeX > mousex && offsetY + this.progressBarPosY + this.progressBarSizeY > mousey) {


                GuiCraftingRecipe.openRecipeGui(outputId, new Object[0]);
                return;
            }
        }
        super.mouseClicked(mousex, mousey, button);
    }

    protected void calculateProgressBarOffsets() {
        ContainerClayMachines container = (ContainerClayMachines) this.inventorySlots;
        this.progressBarSizeX = 24;
        this.progressBarSizeY = 17;
        this.progressBarPosX = (container.machineGuiSizeX - this.progressBarSizeX) / 2;
        this.progressBarPosY = 35;
    }

    protected String getOutputId() {
        TileClayMachines tileClayMachines = (TileClayMachines) this.tile;


        String recipeid = tileClayMachines.getNEIOutputId();
        return (recipeid == null) ? "" : recipeid;
    }
}
