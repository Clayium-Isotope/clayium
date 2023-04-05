package mods.clayium.machine.ClayWorkTable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.GuiTemp;
import mods.clayium.machine.common.IButtonProvider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiClayWorkTable extends GuiTemp {
    public GuiClayWorkTable(ContainerClayWorkTable container) {
        super(container);
    }

    @Override
    protected void supplyDraw() {
        super.supplyDraw();

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png"));
        drawTexturedModalRect(guiLeft + 48, guiTop + 29, 0, 96, 80, 16);
        drawTexturedModalRect(guiLeft + 48, guiTop + 29, 0, 112, ((TileEntityClayWorkTable) tile).getCookProgressScaled(80), 16);

        for (GuiButton button : this.buttonList) {
            button.enabled = ((IButtonProvider) this.tile).isButtonEnable(button.id);
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        for (int i = 0; i < 6; i++) {
            addButton(new GuiPictureButton(i, guiLeft + 40 + 16 * i, guiTop + 52, 16 * i, 32));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
//            ClayiumCore.packetHandler.sendToServer(new GuiButtonPacket(this.tileEntityClayWorkTable.getPos(), button.id));
            mc.playerController.sendEnchantPacket(inventorySlots.windowId, button.id);
//            this.tileEntity.pushButton(button.id);
        }
    }
}