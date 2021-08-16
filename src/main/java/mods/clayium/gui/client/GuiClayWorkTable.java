package mods.clayium.gui.client;

import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.container.ContainerClayWorkTable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 *    1
 * 0 --> 2
 *       3
 */
public class GuiClayWorkTable extends GuiContainer {
    private World world;
    private int x, y, z;
    private EntityPlayer entity;
    public static final ResourceLocation TEXTURE = new ResourceLocation(ClayiumCore.ModId, "textures/gui/clayworktable.png");
    private TileClayWorkTable tileClayWorkTable;

    public GuiClayWorkTable(World world, int x, int y, int z, EntityPlayer entity) {
        super(new ContainerClayWorkTable(world, new BlockPos(x, y, z), entity));
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entity = entity;
        this.xSize = 176;
        this.ySize = 166;
        this.tileClayWorkTable = (TileClayWorkTable) world.getTileEntity(new BlockPos(x, y, z));
    }

    public GuiClayWorkTable(InventoryPlayer invPlayer, TileClayWorkTable tile) {
        super(new ContainerClayWorkTable(invPlayer, tile));
        this.tileClayWorkTable = tile;
    }

    public GuiClayWorkTable(Container inventorySlotsIn) {
        super(inventorySlotsIn);
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
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        GL11.glColor4f(1, 1, 1, 1);
        this.mc.renderEngine.bindTexture(TEXTURE);
        drawModalRectWithCustomSizedTexture(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(this.guiLeft + 48, this.guiTop + 29, 176, 0, this.tileClayWorkTable.getCookProgressScaled(80), 16);

        GuiButton button;
        for (int i = 0; i < this.buttonList.size(); i++) {
            button = this.buttonList.get(i);
            button.enabled = this.tileClayWorkTable.canPushButton(i) != 0;
            this.buttonList.set(i, button);
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
    public void initGui() {
        super.initGui();
//        Keyboard.enableRepeatEvents(true);
//        this.buttonList.clear();
        this.buttonList.add(new GuiPictureButton(0, this.guiLeft + 40, this.guiTop + 52, 16, 16, TEXTURE, 176, 32));
        this.buttonList.add(new GuiPictureButton(1, this.guiLeft + 56, this.guiTop + 52, 16, 16, TEXTURE, 192, 32));
        this.buttonList.add(new GuiPictureButton(2, this.guiLeft + 72, this.guiTop + 52, 16, 16, TEXTURE, 208, 32));
        this.buttonList.add(new GuiPictureButton(3, this.guiLeft + 88, this.guiTop + 52, 16, 16, TEXTURE, 224, 32));
        this.buttonList.add(new GuiPictureButton(4, this.guiLeft + 104, this.guiTop + 52, 16, 16, TEXTURE, 240, 32));
        this.buttonList.add(new GuiPictureButton(5, this.guiLeft + 120, this.guiTop + 52, 16, 16, TEXTURE, 176, 80));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
//            ClayiumCore.packetHandler.sendToServer(new GuiButtonPacket(this.tileClayWorkTable.getPos(), button.id));
//            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
            this.tileClayWorkTable.pushButton(button.id);
        }
    }
}