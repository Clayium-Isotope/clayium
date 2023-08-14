package mods.clayium.gui;

import mods.clayium.core.ClayiumCore;
import mods.clayium.core.ClayiumIntegration;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IMachine;
import mods.clayium.plugin.jei.JEICompatibility;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiIMachine extends GuiTemp {
    protected int progressBarSizeX = 24;
    protected int progressBarSizeY = 17;
    protected int progressBarPosX = (xSize - this.progressBarSizeX) / 2;
    protected int progressBarPosY = 35;

    public GuiIMachine(ContainerIMachine container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (ClayiumIntegration.JEI.loaded() && ((IMachine) this.tile).getKind().hasRecipe()) {
            if (this.guiLeft + this.progressBarPosX <= mouseX && this.guiTop + this.progressBarPosY <= mouseY
                    && this.guiLeft + this.progressBarPosX + this.progressBarSizeX > mouseX && this.guiTop + this.progressBarPosY + this.progressBarSizeY > mouseY) {
                this.drawHoveringText("Show Recipes", mouseX - this.guiLeft, mouseY - this.guiTop);
            }
        }
    }

    @Override
    protected void supplyDraw() {
        super.supplyDraw();

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 96, progressBarSizeX, progressBarSizeY);
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 112, this.getCraftProgressScaled(progressBarSizeX), progressBarSizeY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        EnumMachineKind kind = ((IMachine) this.tile).getKind();

        if (ClayiumIntegration.JEI.loaded() && kind.hasRecipe()) {
            if (this.guiLeft + this.progressBarPosX <= mouseX && this.guiTop + this.progressBarPosY <= mouseY
                    && this.guiLeft + this.progressBarPosX + this.progressBarSizeX > mouseX && this.guiTop + this.progressBarPosY + this.progressBarSizeY > mouseY) {
                ClayiumCore.logger.info("Will show category of " + kind.getRegisterName());
                JEICompatibility.showMachineRecipes(kind);
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private int getCraftProgressScaled(int width) {
        IMachine tecm = (IMachine) this.tile;

        if (tecm.getField(0) == 0) return 0;
        return tecm.getField(1) * width / tecm.getField(0);
    }
}
