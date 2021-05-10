
package mods.clayium.gui;

import mods.clayium.ClayiumMod;
import mods.clayium.ElementsClayiumMod;
import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.gui.container.ContainerClayWorkTable;
import mods.clayium.network.GuiButtonPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ElementsClayiumMod.ModElement.Tag
public class GuiClayWorkTableGUI extends ElementsClayiumMod.ModElement {
	public static int GUIID = 3;
	public static HashMap guistate = new HashMap();
	public GuiClayWorkTableGUI(ElementsClayiumMod instance) {
		super(instance, 5);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		elements.addNetworkMessage(GUIButtonPressedMessageHandler.class, GUIButtonPressedMessage.class, Side.SERVER);
		elements.addNetworkMessage(GUISlotChangedMessageHandler.class, GUISlotChangedMessage.class, Side.SERVER);
	}
	public static class GuiContainerMod extends Container implements Supplier<Map<Integer, Slot>> {
		private IInventory internal;
		private World world;
		private EntityPlayer entity;
		private int x, y, z;
		private Map<Integer, Slot> customSlots = new HashMap<>();
		public GuiContainerMod(World world, int x, int y, int z, EntityPlayer player) {
			this.world = world;
			this.entity = player;
			this.x = x;
			this.y = y;
			this.z = z;
			this.internal = new InventoryBasic("", true, 4);
			TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));
			if (ent instanceof IInventory)
				this.internal = (IInventory) ent;
			this.customSlots.put(0, this.addSlotToContainer(new Slot(internal, 0, 80, 17) {
			}));
			this.customSlots.put(1, this.addSlotToContainer(new Slot(internal, 1, 17, 30) {
			}));
			this.customSlots.put(2, this.addSlotToContainer(new Slot(internal, 2, 143, 30) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
			}));
			this.customSlots.put(3, this.addSlotToContainer(new Slot(internal, 3, 143, 55) {
			}));
			int si;
			int sj;
			for (si = 0; si < 3; ++si)
				for (sj = 0; sj < 9; ++sj)
					this.addSlotToContainer(new Slot(player.inventory, sj + (si + 1) * 9, 0 + 8 + sj * 18, 0 + 84 + si * 18));
			for (si = 0; si < 9; ++si)
				this.addSlotToContainer(new Slot(player.inventory, si, 0 + 8 + si * 18, 0 + 142));
		}

		public Map<Integer, Slot> get() {
			return customSlots;
		}

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return internal.isUsableByPlayer(player);
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
			ItemStack itemstack = ItemStack.EMPTY;
			Slot slot = this.inventorySlots.get(index);
			if (slot != null && slot.getHasStack()) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				if (index < 4) {
					if (!this.mergeItemStack(itemstack1, 4, this.inventorySlots.size(), true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				} else if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
					if (index < 4 + 27) {
						if (!this.mergeItemStack(itemstack1, 4 + 27, this.inventorySlots.size(), true)) {
							return ItemStack.EMPTY;
						}
					} else {
						if (!this.mergeItemStack(itemstack1, 4, 4 + 27, false)) {
							return ItemStack.EMPTY;
						}
					}
					return ItemStack.EMPTY;
				}
				if (itemstack1.getCount() == 0) {
					slot.putStack(ItemStack.EMPTY);
				} else {
					slot.onSlotChanged();
				}
				if (itemstack1.getCount() == itemstack.getCount()) {
					return ItemStack.EMPTY;
				}
				slot.onTake(playerIn, itemstack1);
			}
			return itemstack;
		}

		@Override /**
					 * Merges provided ItemStack with the first avaliable one in the
					 * container/player inventor between minIndex (included) and maxIndex
					 * (excluded). Args : stack, minIndex, maxIndex, negativDirection. /!\ the
					 * Container implementation do not check if the item is valid for the slot
					 */
		protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
			boolean flag = false;
			int i = startIndex;
			if (reverseDirection) {
				i = endIndex - 1;
			}
			if (stack.isStackable()) {
				while (!stack.isEmpty()) {
					if (reverseDirection) {
						if (i < startIndex) {
							break;
						}
					} else if (i >= endIndex) {
						break;
					}
					Slot slot = this.inventorySlots.get(i);
					ItemStack itemstack = slot.getStack();
					if (slot.isItemValid(itemstack) && !itemstack.isEmpty() && itemstack.getItem() == stack.getItem()
							&& (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata())
							&& ItemStack.areItemStackTagsEqual(stack, itemstack)) {
						int j = itemstack.getCount() + stack.getCount();
						int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
						if (j <= maxSize) {
							stack.setCount(0);
							itemstack.setCount(j);
							slot.putStack(itemstack);
							flag = true;
						} else if (itemstack.getCount() < maxSize) {
							stack.shrink(maxSize - itemstack.getCount());
							itemstack.setCount(maxSize);
							slot.putStack(itemstack);
							flag = true;
						}
					}
					if (reverseDirection) {
						--i;
					} else {
						++i;
					}
				}
			}
			if (!stack.isEmpty()) {
				if (reverseDirection) {
					i = endIndex - 1;
				} else {
					i = startIndex;
				}
				while (true) {
					if (reverseDirection) {
						if (i < startIndex) {
							break;
						}
					} else if (i >= endIndex) {
						break;
					}
					Slot slot1 = this.inventorySlots.get(i);
					ItemStack itemstack1 = slot1.getStack();
					if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
						if (stack.getCount() > slot1.getSlotStackLimit()) {
							slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
						} else {
							slot1.putStack(stack.splitStack(stack.getCount()));
						}
						slot1.onSlotChanged();
						flag = true;
						break;
					}
					if (reverseDirection) {
						--i;
					} else {
						++i;
					}
				}
			}
			return flag;
		}

		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			super.onContainerClosed(playerIn);
			if ((internal instanceof InventoryBasic) && (playerIn instanceof EntityPlayerMP)) {
				this.clearContainer(playerIn, playerIn.world, internal);
			}
		}

		private void slotChanged(int slotid, int ctype, int meta) {
			if (this.world != null && this.world.isRemote) {
				ClayiumMod.PACKET_HANDLER.sendToServer(new GUISlotChangedMessage(slotid, x, y, z, ctype, meta));
				handleSlotAction(entity, slotid, ctype, meta, x, y, z);
			}
		}
	}

	public static class GuiWindow extends GuiContainer {
		private World world;
		private int x, y, z;
		private EntityPlayer entity;
		public static final ResourceLocation TEXTURE = new ResourceLocation("clayium", "textures/gui/clayworktable.png");
		private TileClayWorkTable tileClayWorkTable;

		public GuiWindow(World world, int x, int y, int z, EntityPlayer entity) {
			super(new GuiContainerMod(world, x, y, z, entity));
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
			this.entity = entity;
			this.xSize = 176;
			this.ySize = 166;
		}

		public GuiWindow(InventoryPlayer invPlayer, TileClayWorkTable tile) {
			super(new ContainerClayWorkTable(invPlayer, tile));
			this.tileClayWorkTable = tile;
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			super.drawScreen(mouseX, mouseY, partialTicks);

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
			GL11.glColor4f(1, 1, 1, 1);
			this.mc.renderEngine.bindTexture(TEXTURE);
			int k = (this.width - this.xSize) / 2;
			int l = (this.height - this.ySize) / 2;
			this.drawModalRectWithCustomSizedTexture(k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
			if (this.tileClayWorkTable.isBurning()) {
				int i1 = 6;
				this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
			}

			int i1 = this.tileClayWorkTable.getCookProgressScaled(80);
			this.drawTexturedModalRect(k + 48, l + 29, 176, 0, i1, 16);

			for(int i = 0; i < 6; ++i) {
				this.buttonList.get(i).enabled = this.tileClayWorkTable.canPushButton(i + 1) != 0;
			}
		}

		@Override
		public void updateScreen() {
			super.updateScreen();
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
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			super.keyTyped(typedChar, keyCode);
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		}

		@Override
		public void onGuiClosed() {
			super.onGuiClosed();
			Keyboard.enableRepeatEvents(false);
		}

		@Override
		public void initGui() {
			super.initGui();
			this.guiLeft = (this.width - 176) / 2;
			this.guiTop = (this.height - 166) / 2;
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();
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
				ClayiumMod.PACKET_HANDLER.sendToServer(new GuiButtonPacket(this.tileClayWorkTable.getPos().getX(), this.tileClayWorkTable.getPos().getY(), this.tileClayWorkTable.getPos().getZ(), button.id));
				this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
			}
		}

		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}
	}

	public static class GUIButtonPressedMessageHandler implements IMessageHandler<GUIButtonPressedMessage, IMessage> {
		@Override
		public IMessage onMessage(GUIButtonPressedMessage message, MessageContext context) {
			EntityPlayerMP entity = context.getServerHandler().player;
			entity.getServerWorld().addScheduledTask(() -> {
				int buttonID = message.buttonID;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleButtonAction(entity, buttonID, x, y, z);
			});
			return null;
		}
	}

	public static class GUISlotChangedMessageHandler implements IMessageHandler<GUISlotChangedMessage, IMessage> {
		@Override
		public IMessage onMessage(GUISlotChangedMessage message, MessageContext context) {
			EntityPlayerMP entity = context.getServerHandler().player;
			entity.getServerWorld().addScheduledTask(() -> {
				int slotID = message.slotID;
				int changeType = message.changeType;
				int meta = message.meta;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleSlotAction(entity, slotID, changeType, meta, x, y, z);
			});
			return null;
		}
	}

	public static class GUIButtonPressedMessage implements IMessage {
		int buttonID, x, y, z;
		public GUIButtonPressedMessage() {
		}

		public GUIButtonPressedMessage(int buttonID, int x, int y, int z) {
			this.buttonID = buttonID;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void toBytes(io.netty.buffer.ByteBuf buf) {
			buf.writeInt(buttonID);
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
		}

		@Override
		public void fromBytes(io.netty.buffer.ByteBuf buf) {
			buttonID = buf.readInt();
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
		}
	}

	public static class GUISlotChangedMessage implements IMessage {
		int slotID, x, y, z, changeType, meta;
		public GUISlotChangedMessage() {
		}

		public GUISlotChangedMessage(int slotID, int x, int y, int z, int changeType, int meta) {
			this.slotID = slotID;
			this.x = x;
			this.y = y;
			this.z = z;
			this.changeType = changeType;
			this.meta = meta;
		}

		@Override
		public void toBytes(io.netty.buffer.ByteBuf buf) {
			buf.writeInt(slotID);
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
			buf.writeInt(changeType);
			buf.writeInt(meta);
		}

		@Override
		public void fromBytes(io.netty.buffer.ByteBuf buf) {
			slotID = buf.readInt();
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
			changeType = buf.readInt();
			meta = buf.readInt();
		}
	}
	private static void handleButtonAction(EntityPlayer entity, int buttonID, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;
	}

	private static void handleSlotAction(EntityPlayer entity, int slotID, int changeType, int meta, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;
	}
}
