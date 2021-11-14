package mods.clayium.gui.client;

import mods.clayium.gui.container.ContainerItemFilterString;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.client.gui.GuiTextField;

public class GuiItemFilterString extends GuiTemp {
    public GuiItemFilterString(ContainerItemFilterString container) {
        super((ContainerTemp) container);
    }

    public void addTextField() {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        GuiTextField textField = new GuiTextField(this.fontRendererObj, i + 12, j + 18, this.xSize - 24, 12);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(true);
        textField.setMaxStringLength(128);
        addTextFieldToList(textField);
    }


    protected void keyTyped(char p_73869_1_, int p_73869_2_, int id) {
        ((ContainerItemFilterString) this.inventorySlots).setFilterString(((GuiTextField) this.textFields.get(id)).getText());
    }
}
