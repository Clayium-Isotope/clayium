package mods.clayium.gui.client;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayRFGenerator;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

public class GuiClayRFGenerator extends GuiClayEnergyTemp {
    public GuiClayRFGenerator(ContainerTemp container, TileClayRFGenerator tile, Block block) {
        super(container, (TileClayContainer) tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        TileClayRFGenerator tileg = (TileClayRFGenerator) this.tile;
        this.fontRendererObj.drawString(I18n.format("gui.RFGenerator.storage", new Object[] {UtilLocale.rfNumeral(tileg.rfStored), UtilLocale.rfNumeral(tileg.maxRfStored)}), 12, 20, 4210752);
        this.fontRendererObj.drawString(I18n.format("gui.RFGenerator.convertRate", new Object[] {UtilLocale.ClayEnergyNumeral(tileg.ceConsumptionPerTick), UtilLocale.rfNumeral(tileg.rfProductionPerTick)}), 12, 40, 4210752);
        this.fontRendererObj.drawString(I18n.format("gui.RFGenerator.output", new Object[] {UtilLocale.rfNumeral(tileg.rfOutputPerTick)}), 12, 52, 4210752);
        if (tileg.overclockExponent == 1.0D) {
            this.fontRendererObj.drawString(I18n.format("gui.RFGenerator.overclock", new Object[] {UtilLocale.CAResonanceNumeral(tileg.overclockTotalFactor)}), 12, 64, 4210752);
        } else {
            this.fontRendererObj.drawString(I18n.format("gui.RFGenerator.overclockExponen", new Object[] {UtilLocale.CAResonanceNumeral(tileg.overclockTotalFactor), UtilLocale.CAResonanceNumeral(tileg.overclockExponent), UtilLocale.CAResonanceNumeral(Math.pow(tileg.overclockTotalFactor, tileg.overclockExponent))}), 12, 64, 4210752);
        }
    }
}
