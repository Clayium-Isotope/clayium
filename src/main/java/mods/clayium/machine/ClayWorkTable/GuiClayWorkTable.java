package mods.clayium.machine.ClayWorkTable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.GuiTemp;
import mods.clayium.machine.common.IHasButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

// TODO still added an integration between JEI. see from ordinal
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

        for (GuiButton button : buttonList) {
            button.enabled = ((IHasButton) tile).isButtonEnable(button.id);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//			if (ClayiumCore.IntegrationID.NEI.loaded()) {
//				int offsetX = (this.field_146294_l - this.field_146999_f) / 2;
//				int offsetY = (this.field_146295_m - this.field_147000_g) / 2;
//				int progressBarPosX = 48;
//				int progressBarPosY = 33;
//				int progressBarSizeX = 80;
//				int progressBarSizeY = 12;
//				if (offsetX + progressBarPosX <= mouseX && offsetX + progressBarPosX + progressBarSizeX > mouseX
//						&& offsetY + progressBarPosY <= mouseY && offsetY + progressBarPosY + progressBarSizeY > mouseY) {
//					GuiCraftingRecipe.openRecipeGui("ClayWorkTable", new Object[0]);
//					return;
//				}
//			}

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        //			if (ClayiumCore.IntegrationID.NEI.loaded()) {
        //				int offsetX = (this.field_146294_l - this.field_146999_f) / 2;
        //				int offsetY = (this.field_146295_m - this.field_147000_g) / 2;
        //				int progressBarPosX = 48;
        //				int progressBarPosY = 33;
        //				int progressBarSizeX = 80;
        //				int progressBarSizeY = 12;
        //				if (offsetX + progressBarPosX <= mousex && offsetY + progressBarPosY <= mousey && offsetX + progressBarPosX + progressBarSizeX > mousex && offsetY + progressBarPosY + progressBarSizeY > mousey) {
        //					List list = new ArrayList();
        //					list.add("Recipes");
        //					this.func_146283_a(list, mousex, mousey);
        //				}
        //			}
    }

    @Override
    public void initGui() {
        super.initGui();
//        Keyboard.enableRepeatEvents(true);

        for (int i = 0; i < 6; i++) {
            buttonList.add(new GuiPictureButton(i, guiLeft + 40 + 16 * i, guiTop + 52, 16 * i, 32));
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