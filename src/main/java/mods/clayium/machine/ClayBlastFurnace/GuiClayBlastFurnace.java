package mods.clayium.machine.ClayBlastFurnace;

import mods.clayium.gui.ContainerIMachine;
import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.GuiIMachine;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.UtilLocale;

public class GuiClayBlastFurnace extends GuiIMachine {

    public GuiClayBlastFurnace(ContainerIMachine container) {
        super(container);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        this.fontRenderer.drawString(UtilLocale.tierGui(((TileEntityClayiumMachine) this.tile).getRecipeTier()), 64,
                container.machineGuiSizeY - 12, 4210752);
    }
}
