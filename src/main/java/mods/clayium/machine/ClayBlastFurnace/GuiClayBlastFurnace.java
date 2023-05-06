package mods.clayium.machine.ClayBlastFurnace;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.ClayiumMachine.GuiClayiumMachine;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.UtilLocale;

public class GuiClayBlastFurnace extends GuiClayiumMachine {
    public GuiClayBlastFurnace(ContainerClayiumMachine container) {
        super(container);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp)this.inventorySlots;
        this.fontRenderer.drawString(UtilLocale.tierGui(((TileEntityClayiumMachine) this.tile).getRecipeTier()), 64, container.machineGuiSizeY - 12, 4210752);
    }
}