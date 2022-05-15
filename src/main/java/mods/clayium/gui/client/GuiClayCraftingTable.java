package mods.clayium.gui.client;

import codechicken.nei.recipe.GuiCraftingRecipe;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.block.Block;

public class GuiClayCraftingTable
        extends GuiClayContainerTemp {
    public GuiClayCraftingTable(ContainerTemp container, TileClayContainer tile, Block block) {
        super(container, tile, block);
    }


    public void mouseClicked(int mousex, int mousey, int button) {
        if (ClayiumCore.IntegrationID.NEI.loaded()) {
            int progressBarSizeX = 24, progressBarSizeY = 17;
            int progressBarPosX = 90;
            int progressBarPosY = 34;
            int offsetX = (this.width - this.xSize) / 2;
            int offsetY = (this.height - this.ySize) / 2;
            if (offsetX + progressBarPosX <= mousex && offsetY + progressBarPosY <= mousey && offsetX + progressBarPosX + progressBarSizeX > mousex && offsetY + progressBarPosY + progressBarSizeY > mousey) {


                GuiCraftingRecipe.openRecipeGui("crafting", new Object[0]);
                return;
            }
        }
        super.mouseClicked(mousex, mousey, button);
    }


    public void drawScreen(int mousex, int mousey, float p_73863_3_) {
        super.drawScreen(mousex, mousey, p_73863_3_);
        if (ClayiumCore.IntegrationID.NEI.loaded()) {
            int progressBarSizeX = 24, progressBarSizeY = 17;
            int progressBarPosX = 90;
            int progressBarPosY = 34;
            int offsetX = (this.width - this.xSize) / 2;
            int offsetY = (this.height - this.ySize) / 2;
            if (offsetX + progressBarPosX <= mousex && offsetY + progressBarPosY <= mousey && offsetX + progressBarPosX + progressBarSizeX > mousex && offsetY + progressBarPosY + progressBarSizeY > mousey) {


                List<String> list = new ArrayList();
                list.add("Recipes");
                func_146283_a(list, mousex, mousey);
            }
        }
    }
}
