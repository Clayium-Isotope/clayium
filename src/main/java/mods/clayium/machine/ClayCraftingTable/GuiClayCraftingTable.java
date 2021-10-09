package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.GuiClayMachineTemp;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class GuiClayCraftingTable extends GuiClayMachineTemp {
    public GuiClayCraftingTable(ContainerClayCraftingTable container, TileEntityClayCraftingTable tile, Block block) {
        super(container, tile, block, container.machineGuiHeight);
        coverTexture = new ResourceLocation(ClayiumCore.ModId, "textures/gui/claycraftingtable.png");
    }
}
