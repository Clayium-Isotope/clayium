package mods.clayium.gui.client;

import mods.clayium.block.tile.TileCAMachines;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;


public class GuiClayContainerTemp
        extends GuiTemp {
    protected TileClayContainer tile;
    protected Block block;

    public GuiClayContainerTemp(ContainerTemp container, TileClayContainer tile, Block block) {
        super(container);
        this.tile = tile;

        this.block = block;

        setGuiTitle(this.tile.hasCustomInventoryName() ? this.tile.getInventoryName() : I18n.format(this.tile.getInventoryName(), new Object[0]));
    }


    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        drawClayEnergyItem(p_73863_1_, p_73863_2_, p_73863_3_);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);


        drawClayEnergyForegroundLayer();
        drawResonanceForegroundLayer();
        drawOverclockFactorForegroundLayer();
    }


    protected void drawClayEnergyForegroundLayer() {}

    protected void drawResonanceForegroundLayer() {
        if (this.tile instanceof TileCAMachines) {
            ContainerTemp container = (ContainerTemp) this.inventorySlots;

            this.fontRendererObj.drawString(I18n.format("gui.Common.resonance", new Object[] {UtilLocale.CAResonanceNumeral(((TileCAMachines) this.tile).getResonance())}), 80, container.machineGuiSizeY - 12, 4210752);
        }
    }

    protected void drawClayEnergyItem(int p_73863_1_, int p_73863_2_, float p_73863_3_) {}

    protected void drawOverclockFactorForegroundLayer() {
        if (this.tile instanceof TileClayMachines && ((TileClayMachines) this.tile).overclockTotalFactor != 1.0D) {
            ContainerTemp container = (ContainerTemp) this.inventorySlots;

            this.fontRendererObj.drawString(I18n.format("gui.Common.overclockFactor", new Object[] {UtilLocale.CAResonanceNumeral(((TileClayMachines) this.tile).overclockTotalFactor)}), 80, container.machineGuiSizeY, 4210752);
        }
    }
}
