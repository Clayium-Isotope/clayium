package mods.clayium.machine.ClayiumMachine;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import mods.clayium.core.ClayiumCore;
import mods.clayium.core.ClayiumIntegration;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.GuiTemp;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.plugin.jei.JEICompatibility;
import mods.clayium.util.UtilTier;

public class GuiClayiumMachine extends GuiTemp {

    protected int progressBarSizeX = 24;
    protected int progressBarSizeY = 17;
    protected int progressBarPosX = (xSize - this.progressBarSizeX) / 2;
    protected int progressBarPosY = 35;

    public GuiClayiumMachine(ContainerClayiumMachine container) {
        super(container);
        this.calculateProgressBarOffsets();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (ClayiumIntegration.JEI.loaded() && ((TileEntityClayiumMachine) this.tile).kind.hasRecipe()) {
            if (this.guiLeft + this.progressBarPosX <= mouseX && this.guiTop + this.progressBarPosY <= mouseY &&
                    this.guiLeft + this.progressBarPosX + this.progressBarSizeX > mouseX &&
                    this.guiTop + this.progressBarPosY + this.progressBarSizeY > mouseY) {
                this.drawHoveringText("Show Recipes", mouseX - this.guiLeft, mouseY - this.guiTop);
            }
        }
    }

    @Override
    protected void supplyDraw() {
        super.supplyDraw();

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 96, progressBarSizeX,
                progressBarSizeY);
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 112,
                this.getCraftProgressScaled(progressBarSizeX), progressBarSizeY);

        if (UtilTier.canManufactureCraft(((TileEntityClayiumMachine) this.tile).getHullTier()))
            buttonList.get(0).enabled = ((TileEntityClayiumMachine) this.tile).canPushButton(0) ==
                    IButtonProvider.ButtonProperty.PERMIT;
    }

    @Override
    protected void addButtons() {
        if (UtilTier.canManufactureCraft(((TileEntityClayiumMachine) this.tile).getHullTier()))
            buttonList.add(new GuiPictureButton(0, guiLeft + (xSize - 16) / 2, guiTop + 56, 0, 48));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            mc.playerController.sendEnchantPacket(inventorySlots.windowId, button.id);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        EnumMachineKind kind = ((TileEntityClayiumMachine) this.tile).kind;

        if (ClayiumIntegration.JEI.loaded() && kind.hasRecipe()) {
            if (this.guiLeft + this.progressBarPosX <= mouseX && this.guiTop + this.progressBarPosY <= mouseY &&
                    this.guiLeft + this.progressBarPosX + this.progressBarSizeX > mouseX &&
                    this.guiTop + this.progressBarPosY + this.progressBarSizeY > mouseY) {
                ClayiumCore.logger.info("Will show category of " + kind.getRegisterName());
                JEICompatibility.showMachineRecipes(kind);
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void calculateProgressBarOffsets() {}

    private int getCraftProgressScaled(int width) {
        TileEntityClayiumMachine tecm = (TileEntityClayiumMachine) this.tile;

        if (tecm.getField(0) == 0) return 0;
        return tecm.getField(1) * width / tecm.getField(0);
    }
}
