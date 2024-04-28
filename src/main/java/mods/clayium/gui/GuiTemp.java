package mods.clayium.gui;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.ClayEnergyHolder;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiTemp extends GuiContainer {
    protected final IInventory tile;
    protected final ContainerTemp container;
    protected final int machineHeight;
    protected ResourceLocation coverTexture;

    public GuiTemp(ContainerTemp container) {
        super(container);

        this.tile = container.tileEntity;
        this.container = container;
        xSize = 176;
        ySize = this.container.machineGuiSizeY + 94;
        machineHeight = this.container.machineGuiSizeY;
    }

    @Override
    public void initGui() {
        super.initGui();
        addButtons();
    }

    protected void addButtons() {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(this.container.getInventoryName(), 6, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, machineHeight, 4210752);

        if (this.tile instanceof TileEntity
                && ((TileEntity) this.tile).hasCapability(ClayiumCore.RESONANCE_CAPABILITY, null)) {
            ContainerTemp container = (ContainerTemp)this.inventorySlots;
            double resonance = ((TileEntity) this.tile).getCapability(ClayiumCore.RESONANCE_CAPABILITY, null).getResonance();
//            this.fontRenderer.drawString(I18n.format("gui.Common.resonance", UtilLocale.CAResonanceNumeral(((TileEntityCAMachine)this.tile).getResonance())), 80, container.machineGuiSizeY - 12, 4210752);
            this.fontRenderer.drawString(I18n.format("gui.Common.resonance", UtilLocale.CAResonanceNumeral(resonance)), 80, container.machineGuiSizeY - 12, 4210752);
        }

        if (this.tile instanceof ClayEnergyHolder && (!(this.tile instanceof IClayEnergyConsumer) || ((IClayEnergyConsumer) this.tile).acceptClayEnergy()))
            this.fontRenderer.drawString(I18n.format("gui.Common.energy", UtilLocale.ClayEnergyNumeral(((ClayEnergyHolder) this.tile).containEnergy().get(), false)), 4, this.container.machineGuiSizeY - 12, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/back.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, machineHeight);
        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/playerinventory.png"));
        drawTexturedModalRect(guiLeft, guiTop + machineHeight, 0, 0, xSize, 94);

        supplyDraw();

        if (coverTexture != null) {
            mc.getTextureManager().bindTexture(coverTexture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, machineHeight);
        }
    }

    protected void supplyDraw() {
        for(int i = 0; i < container.inventorySlots.size(); ++i) {
            if (container.inventorySlots.get(i) instanceof SlotWithTexture)
                ((SlotWithTexture) container.inventorySlots.get(i)).draw(this, guiLeft, guiTop);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            mc.playerController.sendEnchantPacket(inventorySlots.windowId, button.id);
        }
    }
}
