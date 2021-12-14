package mods.clayium.machine.ClayBendingMachine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.machine.common.GuiClayMachineTemp;
import mods.clayium.machine.common.IClicker;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

// TODO still added an integration between JEI. see from ordinal
public class GuiClayBendingMachine extends GuiClayMachineTemp {
    private final int progressBarSizeX = 22;
    private final int progressBarSizeY = 16;
    private final int progressBarPosX = (xSize - this.progressBarSizeX) / 2;
    private final int progressBarPosY = 35;

    public GuiClayBendingMachine(InventoryPlayer invPlayer, TileEntityClayBendingMachine tile, Block block) {
        super(new ContainerClayBendingMachine(invPlayer, tile), tile, block, 72);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRenderer.drawString(I18n.format("gui.Common.energy", UtilLocale.ClayEnergyNumeral(((TileEntityClayBendingMachine) tile).containEnergy, false)), 4, machineHeight - 12, 4210752);
    }

    @Override
    protected void supplyDraw() {
        super.supplyDraw();

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 96, progressBarSizeX, progressBarSizeY);
        drawTexturedModalRect(guiLeft + progressBarPosX, guiTop + progressBarPosY, 80, 112, ((TileEntityClayBendingMachine) tile).getCraftProgressScaled(progressBarSizeX), progressBarSizeY);

        buttonList.get(0).enabled = ((TileEntityClayBendingMachine) tile).canPushButton(0) == IClicker.ButtonProperty.PERMIT;
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.add(new GuiPictureButton(1, guiLeft + (xSize - 16) / 2, guiTop + 56, 0, 48));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        itemRender.renderItemAndEffectIntoGUI(((TileEntityClayBendingMachine) tile).getStackInSlot(((TileEntityClayBendingMachine) tile).clayEnergySlot), guiLeft - 12, guiTop + machineHeight - 16);
        itemRender.renderItemOverlayIntoGUI(fontRenderer, ((TileEntityClayBendingMachine) tile).getStackInSlot(((TileEntityClayBendingMachine) tile).clayEnergySlot), guiLeft - 12, guiTop + machineHeight - 16, null);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            mc.playerController.sendEnchantPacket(inventorySlots.windowId, button.id);
        }
    }
}
