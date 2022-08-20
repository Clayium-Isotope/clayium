package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiTemp;
import net.minecraft.util.ResourceLocation;

public class GuiClayCraftingTable extends GuiTemp {
    public GuiClayCraftingTable(ContainerClayCraftingTable container) {
        super(container);
        coverTexture = new ResourceLocation(ClayiumCore.ModId, "textures/gui/claycraftingtable.png");
    }
}
