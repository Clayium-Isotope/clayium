package mods.clayium.machine.ClayiumMachine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.GuiTemp;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.util.UtilLocale;
import mods.clayium.util.UtilTier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

// TODO still added an integration between JEI. see from ordinal
public class GuiClayiumMachine extends GuiTemp {
    private final int progressBarSizeX = 22;
    private final int progressBarSizeY = 16;
    private final int progressBarPosX = (xSize - this.progressBarSizeX) / 2;
    private final int progressBarPosY = 35;

    public GuiClayiumMachine(ContainerClayiumMachine container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRenderer.drawString(I18n.format("gui.Common.energy", UtilLocale.ClayEnergyNumeral(((TileEntityClayiumMachine) tile).containEnergy, false)), 4, machineHeight - 12, 4210752);
    }

    @Override
    protected void supplyDraw() {
        super.supplyDraw();

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 96, progressBarSizeX, progressBarSizeY);
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 112, ((TileEntityClayiumMachine) tile).getCraftProgressScaled(progressBarSizeX), progressBarSizeY);

        if (UtilTier.canManufactureCraft(((TileEntityClayiumMachine) this.tile).getTier()))
            buttonList.get(0).enabled = ((TileEntityClayiumMachine) this.tile).canPushButton(0) == IHasButton.ButtonProperty.PERMIT;
    }

    @Override
    protected void addButtons() {
        if (UtilTier.canManufactureCraft(((TileEntityClayiumMachine) this.tile).getTier()))
            buttonList.add(new GuiPictureButton(0, guiLeft + (xSize - 16) / 2, guiTop + 56, 0, 48));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            mc.playerController.sendEnchantPacket(inventorySlots.windowId, button.id);
        }
    }
}
