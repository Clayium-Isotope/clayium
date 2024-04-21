package mods.clayium.machine.AreaMiner;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.GuiTemp;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.resources.I18n;

public class GuiAreaMiner extends GuiTemp {
    public GuiAreaMiner(ContainerAreaMiner container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        long energy = ((TileEntityAreaMiner)this.tile).laserEnergy;

        this.fontRenderer.drawString(UtilLocale.laserGui(energy), 64, container.machineGuiSizeY - 12, 4210752);
        if (TierPrefix.comparator.compare(((TileEntityAreaMiner)this.tile).getHullTier(), TierPrefix.ultimate) >= 0) {
            this.fontRenderer.drawString(I18n.format("gui.AreaMiner.harvest"), container.machineGuiSizeX - 48, 21, 4210752);
            this.fontRenderer.drawString(I18n.format("gui.AreaMiner.fortune"), container.machineGuiSizeX - 48, 39, 4210752);
            this.fontRenderer.drawString(I18n.format("gui.AreaMiner.silktouch"), container.machineGuiSizeX - 48, 57, 4210752);
        }
    }

    @Override
    public void addButtons() {
        int offsetX = this.guiLeft;
        int offsetY = this.guiTop;
        if (TierPrefix.comparator.compare(((TileEntityAreaMiner)this.tile).getHullTier(), TierPrefix.ultimate) >= 0) {
            offsetX = this.guiLeft - 10;
        }

        /**
         *   0: |>  |  1: ||
         *   2: @   |  3: []
         */

        if (TierPrefix.comparator.compare(((TileEntityAreaMiner)this.tile).getHullTier(), TierPrefix.ultimate) <= 0) {
            this.buttonList.add(new GuiPictureButton(0, offsetX + 16, offsetY + 16, 16, 48));
            this.buttonList.add(new GuiPictureButton(1, offsetX + 16 + 18, offsetY + 16, 32, 48));
            this.buttonList.add(new GuiPictureButton(2, offsetX + 16, offsetY + 16 + 18, 64, 48));
            this.buttonList.add(new GuiPictureButton(3, offsetX + 16 + 18, offsetY + 16 + 18, 48, 48));
        } else {
            this.buttonList.add(new GuiPictureButton(0, offsetX + 16, offsetY + 16, 16, 48));
            this.buttonList.add(new GuiPictureButton(1, offsetX + 16, offsetY + 16 + 18, 32, 48));
            this.buttonList.add(new GuiPictureButton(2, offsetX + 16, offsetY + 16 + 36, 64, 48));
            this.buttonList.add(new GuiPictureButton(3, offsetX + 16, offsetY + 16 + 54, 48, 48));
        }
    }

//    @Override
//    protected void supplyDraw() {
//        super.supplyDraw();
//
//        int offsetX = this.guiLeft;
//        int offsetY = this.guiTop;
//        if (TierPrefix.comparator.compare(((TileEntityAreaMiner)this.tile).getHullTier(), TierPrefix.ultimate) >= 0) {
//            offsetX = this.guiLeft - 10;
//        }
//
//        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
//        if (TierPrefix.comparator.compare(((TileEntityAreaMiner)this.tile).getHullTier(), TierPrefix.ultimate) <= 0) {
//            drawTexturedModalRect(offsetX + 16, offsetY + 16, 16, 64, 16, 16);
//            drawTexturedModalRect(offsetX + 16 + 18, offsetY + 16, 32, 64, 16, 16);
//            drawTexturedModalRect(offsetX + 16, offsetY + 16 + 18, 64, 64, 16, 16);
//            drawTexturedModalRect(offsetX + 16 + 18, offsetY + 16, 48, 64, 16, 16);
//        } else {
//            drawTexturedModalRect(offsetX + 16, offsetY + 16, 16, 64, 16, 16);
//            drawTexturedModalRect(offsetX + 16, offsetY + 16 + 18, 32, 64, 16, 16);
//            drawTexturedModalRect(offsetX + 16, offsetY + 16 + 36, 64, 64, 16, 16);
//            drawTexturedModalRect(offsetX + 16, offsetY + 16 + 54, 48, 64, 16, 16);
//        }
//    }
}