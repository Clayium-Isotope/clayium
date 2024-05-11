package mods.clayium.item.filter;

import mods.clayium.gui.GuiTemp;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiFilterString extends GuiTemp {

    protected GuiTextField textField;

    public GuiFilterString(ContainerFilterString container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();

        this.textField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 12, this.guiTop + 18, this.xSize - 24,
                12);
        this.textField.setTextColor(-1);
        this.textField.setDisabledTextColour(-1);
        this.textField.setEnableBackgroundDrawing(true);
        this.textField.setMaxStringLength(128);
        this.textField.setText(((ContainerFilterString) this.inventorySlots).getFilterString());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.textField.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            // if you want Original's behavior, uncomment out.
            // ((ContainerFilterString) this.inventorySlots).setFilterString(this.textField.getText().trim());

            this.mc.displayGuiScreen(null);
        } else if (keyCode == Keyboard.KEY_RETURN && !this.textField.isFocused()) {
            ((ContainerFilterString) this.inventorySlots).setFilterString(this.textField.getText().trim());

            this.mc.displayGuiScreen(null);
        } else if (keyCode == Keyboard.KEY_RETURN) {
            this.textField.setFocused(false);
        } else {
            this.textField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
