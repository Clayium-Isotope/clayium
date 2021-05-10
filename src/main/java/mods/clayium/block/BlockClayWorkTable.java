
package mods.clayium.block;

import mods.clayium.ClayiumMod;
import mods.clayium.ElementsClayiumMod;
import mods.clayium.core.ClayiumCore;
import mods.clayium.creativetab.TabClayium;
import mods.clayium.gui.GuiClayWorkTableGUI;
import mods.clayium.item.ItemClayRollingPin;
import mods.clayium.item.ItemClaySlicer;
import mods.clayium.item.ItemClaySpatula;
import mods.clayium.item.ItemLargeClayBall;
import mods.clayium.item.crafting.ClayWorkTableRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ElementsClayiumMod.ModElement.Tag
public class BlockClayWorkTable extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_work_table")
	public static final Block block = new BlockCustom();
	public BlockClayWorkTable(ElementsClayiumMod instance) {
		super(instance, 4);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> block.setRegistryName("clay_work_table"));
		elements.items.add(() -> new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityCustom.class, "clayium:tileentityclay_work_table");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation("clayium:clay_work_table", "inventory"));
	}
	public static class BlockCustom extends Block implements ITileEntityProvider {
		public BlockCustom() {
			super(Material.CLAY);
			setUnlocalizedName("clay_work_table");
			setSoundType(SoundType.GROUND);
			setHarvestLevel("shovel", 0);
			setHardness(1F);
			setResistance(4F);
			setLightLevel(0F);
			setLightOpacity(0);
			setCreativeTab(TabClayium.tab);
		}

		@Override
		public TileEntity createNewTileEntity(World worldIn, int meta) {
			return new TileEntityCustom();
		}

		@Override
		public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
			super.eventReceived(state, worldIn, pos, eventID, eventParam);
			TileEntity tileentity = worldIn.getTileEntity(pos);
			return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
		}

		@Override
		public EnumBlockRenderType getRenderType(IBlockState state) {
			return EnumBlockRenderType.MODEL;
		}

		@Override
		public void breakBlock(World world, BlockPos pos, IBlockState state) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityCustom)
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityCustom) tileentity);
			world.removeTileEntity(pos);
			super.breakBlock(world, pos, state);
		}

		@Override
		public boolean hasComparatorInputOverride(IBlockState state) {
			return true;
		}

		@Override
		public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing direction,
				float hitX, float hitY, float hitZ) {
			super.onBlockActivated(world, pos, state, entity, hand, direction, hitX, hitY, hitZ);
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			if (entity != null) {
				((EntityPlayer) entity).openGui(ClayiumMod.instance, GuiClayWorkTableGUI.GUIID, world, x, y, z);
			}
			return true;
		}
	}

	public static class TileEntityCustom extends TileEntityLockableLoot implements ISidedInventory {
		private static final int[] slotsTop = new int[]{0};
		private static final int[] slotsBottom = new int[]{2, 1};
		private static final int[] slotsSides = new int[]{1};
		private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);
		public int furnaceBurnTime;
		public int furnaceCookTime;
		private String furnaceName = "container.clay_work_table";
		public int furnaceTimeToCook;
		public int furnaceCookingMethod = 0;
		private ForgeChunkManager.Ticket ticket;
		public IMerchant merchant;
		public MerchantRecipe currentRecipe;
		public int currentRecipeIndex = 0;
		private int toSellSlotIndex = 2;

		private NonNullList<ItemStack> stacks = NonNullList.withSize(4, ItemStack.EMPTY);
		@Override
		public int getSizeInventory() {
			return this.furnaceItemStacks.size();
		}

		@Override
		public boolean isEmpty() {
			for (ItemStack itemstack : this.furnaceItemStacks)
				if (!itemstack.isEmpty())
					return false;
			return true;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return this.furnaceItemStacks.get(slot);
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			if (this.furnaceItemStacks.get(index) == ItemStack.EMPTY) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack;
			if (this.furnaceItemStacks.get(index).getCount() <= count) {
				itemstack = this.furnaceItemStacks.get(index);
				this.furnaceItemStacks.set(index, ItemStack.EMPTY);
			} else {
				itemstack = this.furnaceItemStacks.get(index).splitStack(count);
				if (this.furnaceItemStacks.get(index).getCount() == 0) {
					this.furnaceItemStacks.set(index, ItemStack.EMPTY);
				}
			}

			if (this.inventoryResetNeededOnSlotChange(index)) {
				this.resetRecipeAndSlots();
			}

			return itemstack;
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			if (this.furnaceItemStacks.get(index) == ItemStack.EMPTY) {
				return ItemStack.EMPTY;
			}
				ItemStack itemstack = this.furnaceItemStacks.get(index);
				this.furnaceItemStacks.set(index, ItemStack.EMPTY);
				return itemstack;
		}

		@Override
		public void setInventorySlotContents(int slot, ItemStack itemstack) {
			this.furnaceItemStacks.set(slot, itemstack);
			if (itemstack != ItemStack.EMPTY && itemstack.getCount() > this.getInventoryStackLimit()) {
				itemstack.setCount(this.getInventoryStackLimit());
			}

			if (this.inventoryResetNeededOnSlotChange(slot)) {
				this.resetRecipeAndSlots();
			}
		}

		@Override
		public String getName() {
			return this.hasCustomName() ? this.furnaceName : this.getBlockType().getLocalizedName();
		}

		@Override
		public boolean hasCustomName() {
			return this.furnaceName != null && this.furnaceName.length() > 0;
		}


		@Override
		public void readFromNBT(NBTTagCompound tagCompound) {
			super.readFromNBT(tagCompound);
			NBTTagList tagList = tagCompound.getTagList("Items", 10);
			this.furnaceItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);

			for(int i = 0; i < tagList.tagCount(); ++i) {
				NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
				byte byte0 = tagCompound1.getByte("Slot");
				if (byte0 >= 0 && byte0 < this.furnaceItemStacks.size()) {
					this.furnaceItemStacks.set(byte0, new ItemStack(tagCompound1));
				}
			}

			this.furnaceCookTime = tagCompound.getShort("CookTime");
			this.furnaceBurnTime = tagCompound.getShort("BurnTime");
			this.furnaceTimeToCook = tagCompound.getShort("TimeToCook");
			this.furnaceCookingMethod = tagCompound.getShort("CookingMethod");
			if (tagCompound.hasKey("CustomName", 8)) {
				this.furnaceName = tagCompound.getString("CustomName");
			}
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
			super.writeToNBT(tagCompound);

			tagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
			tagCompound.setShort("CookTime", (short)this.furnaceCookTime);
			tagCompound.setShort("TimeToCook", (short)this.furnaceTimeToCook);
			tagCompound.setShort("CookingMethod", (short)this.furnaceCookingMethod);
			NBTTagList tagList = new NBTTagList();

			for(int i = 0; i < this.furnaceItemStacks.size(); ++i) {
				if (this.furnaceItemStacks.get(i) != ItemStack.EMPTY) {
					NBTTagCompound tagCompound1 = new NBTTagCompound();
					tagCompound1.setByte("Slot", (byte)i);
					this.furnaceItemStacks.get(i).writeToNBT(tagCompound1);
					tagList.appendTag(tagCompound1);
				}
			}

			tagCompound.setTag("Items", tagList);
			if (this.hasCustomName()) {
				tagCompound.setString("CustomName", this.furnaceName);
			}

			return tagCompound;
		}

		@Override
		public SPacketUpdateTileEntity getUpdatePacket() {
			return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
		}

		@Override
		public NBTTagCompound getUpdateTag() {
			return this.writeToNBT(new NBTTagCompound());
		}

		@Override
		public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
			this.readFromNBT(pkt.getNbtCompound());
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public String getGuiID() {
			return "clayium:clay_work_table";
		}

		@SideOnly(Side.CLIENT)
		public int getCookProgressScaled(int par1) {
			return this.furnaceTimeToCook != 0 && this.furnaceCookingMethod != 0 ? this.furnaceCookTime * par1 / this.furnaceTimeToCook : 0;
		}

		public boolean isBurning() {
			return this.furnaceBurnTime > 0;
		}

		public void updateEntity() {
			int maxTransfer = 1;
			int[] fromSlots = new int[]{2, 3};
			EnumFacing[] var4 = EnumFacing.VALUES;

			for (EnumFacing direction : var4) {
				this.transfer(this, fromSlots, direction, maxTransfer);
			}

			this.resetRecipeAndSlots();
			if (!this.world.isRemote && this.ticket == null) {
				this.ticket = ForgeChunkManager.requestTicket(ClayiumCore.INSTANCE, this.world, ForgeChunkManager.Type.NORMAL);
				ForgeChunkManager.forceChunk(this.ticket, new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4));
			}
		}

		public void releaseTicket() {
			if (this.ticket != null) {
				ForgeChunkManager.releaseTicket(this.ticket);
			}
		}

		public void transfer(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer) {
			EnumFacing fromSide = direction;
			EnumFacing toSide = direction.getOpposite();
			TileEntity te = this.world.getTileEntity(from.getPos().add(direction.getFrontOffsetX(), direction.getFrontOffsetY(), direction.getFrontOffsetZ()));
			if (te instanceof IInventory) {
				IInventory to = (IInventory)te;
				int[] toSlots;
				if (to instanceof ISidedInventory) {
					toSlots = ((ISidedInventory)to).getSlotsForFace(toSide);
				} else {
					toSlots = new int[to.getSizeInventory()];

					for(int i = 0; i < to.getSizeInventory(); toSlots[i] = i++) {}
				}

				this.transfer((IInventory)from, to, fromSlots, toSlots, fromSide, toSide, maxTransfer);
			}
		}

		public void transfer(IInventory from, IInventory to, int[] fromSlots, int[] toSlots, EnumFacing fromSide, EnumFacing toSide, int maxTransfer) {
			int oldTransfer = maxTransfer;
			ISidedInventory fromSided = from instanceof ISidedInventory ? (ISidedInventory)from : null;
			ISidedInventory toSided = to instanceof ISidedInventory ? (ISidedInventory)to : null;

			for (int fromSlot : fromSlots) {
				ItemStack fromItem = from.getStackInSlot(fromSlot);
				if (fromItem != ItemStack.EMPTY && fromItem.getCount() > 0 && (fromSided == null || fromSided.canExtractItem(fromSlot, fromItem, fromSide))) {
					int[] var16;
					int var17;
					int var18;
					int toSlot;
					ItemStack toItem;
					if (fromItem.isStackable()) {
						for (var18 = 0; var18 < toSlots.length; ++var18) {
							toSlot = toSlots[var18];
							toItem = to.getStackInSlot(toSlot);
							if (toItem != ItemStack.EMPTY && toItem.getCount() > 0 && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide)) && fromItem.isItemEqual(toItem) && ItemStack.areItemStackTagsEqual(toItem, fromItem)) {
								int maxSize = Math.min(toItem.getMaxStackSize(), to.getInventoryStackLimit());
								int maxMove = Math.min(maxSize - toItem.getCount(), Math.min(maxTransfer, fromItem.getCount()));
								toItem.setCount(toItem.getCount() + maxMove);
								maxTransfer -= maxMove;
								fromItem.setCount(fromItem.getCount() - maxMove);
								if (fromItem.getCount() == 0) {
									from.setInventorySlotContents(fromSlot, ItemStack.EMPTY);
								}

								if (maxTransfer == 0) {
									return;
								}

								if (fromItem.getCount() == 0) {
									break;
								}
							}
						}
					}

					if (fromItem.getCount() > 0) {
						for (var18 = 0; var18 < toSlots.length; ++var18) {
							toSlot = toSlots[var18];
							toItem = to.getStackInSlot(toSlot);
							if (toItem == ItemStack.EMPTY && to.isItemValidForSlot(toSlot, fromItem) && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))) {
								toItem = fromItem.copy();
								toItem.setCount(Math.min(maxTransfer, fromItem.getCount()));
								to.setInventorySlotContents(toSlot, toItem);
								maxTransfer -= toItem.getCount();
								fromItem.setCount(fromItem.getCount() + toItem.getCount());
								if (fromItem.getCount() == 0) {
									from.setInventorySlotContents(fromSlot, ItemStack.EMPTY);
								}

								if (maxTransfer == 0) {
									return;
								}

								if (fromItem.getCount() == 0) {
									break;
								}
							}
						}
					}
				}
			}

			if (oldTransfer != maxTransfer) {
				to.markDirty();
				from.markDirty();
			}
		}

		private boolean canSmelt() {
			if (this.furnaceItemStacks.get(0) == ItemStack.EMPTY) {
				return false;
			}

			ItemStack itemstack = ClayWorkTableRecipes.smelting().getSmeltingResult(this.furnaceItemStacks.get(0));
			if (itemstack == null) {
				return false;
			}
			if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
				return true;
			}
			if (!this.furnaceItemStacks.get(2).isItemEqual(itemstack)) {
				return false;
			}

			int result = this.furnaceItemStacks.get(2).getCount() + itemstack.getCount();
			return result <= this.getInventoryStackLimit() && result <= this.furnaceItemStacks.get(2).getMaxStackSize();
		}

		private boolean canKnead(ItemStack material, int method) {
			if (material == null) {
				return false;
			}
			
			ItemStack itemstack = ClayWorkTableRecipes.smelting().getKneadingResult(material, method);
			ItemStack itemstack2 = ClayWorkTableRecipes.smelting().getKneadingResult2(material, method);
			if (itemstack == null) {
				return false;
			}
			if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
				return true;
			}
			if (!this.furnaceItemStacks.get(2).isItemEqual(itemstack)) {
				return false;
			}
			
			int result2;
			if (itemstack2 != null && this.furnaceItemStacks.get(3) != ItemStack.EMPTY) {
				if (!this.furnaceItemStacks.get(3).isItemEqual(itemstack2)) {
					return false;
				}
				
				result2 = this.furnaceItemStacks.get(3).getCount() + itemstack2.getCount();
				if (result2 > this.getInventoryStackLimit() || result2 > this.furnaceItemStacks.get(3).getMaxStackSize()) {
					return false;
				}
			}

			result2 = this.furnaceItemStacks.get(2).getCount() + itemstack.getCount();
			return result2 <= this.getInventoryStackLimit() && result2 <= this.furnaceItemStacks.get(2).getMaxStackSize();
		}

		public int canPushButton(int buttonId) {
			if (buttonId == 3 && (this.furnaceItemStacks.get(1) == ItemStack.EMPTY || this.furnaceItemStacks.get(1).getItem() != ItemClayRollingPin.block)) {
				return 0;
			}
			
			if ((buttonId == 4 || buttonId == 6) && (this.furnaceItemStacks.get(1) == ItemStack.EMPTY || this.furnaceItemStacks.get(1).getItem() != ItemClaySlicer.block && this.furnaceItemStacks.get(1).getItem() != ItemClaySpatula.block)) {
				return 0;
			}
				
			if (buttonId == 5 && (this.furnaceItemStacks.get(1) == ItemStack.EMPTY || this.furnaceItemStacks.get(1).getItem() != ItemClaySpatula.block)) {
				return 0;
			}
				
			ItemStack itemstack;
			if (this.furnaceCookingMethod != 0) {
				itemstack = this.furnaceItemStacks.get(4);
				if (this.canKnead(itemstack, buttonId) && this.furnaceCookingMethod == buttonId) {
					return 1;
				}
			}

			itemstack = this.furnaceItemStacks.get(0);
			if (this.canKnead(itemstack, buttonId)) {
				return this.furnaceCookingMethod == 0 ? 1 : 2;
			}
			
			return 0;
		}

		public void pushButton(int buttonId) {
			int canPushButton = this.canPushButton(buttonId);
			if (canPushButton != 0 && buttonId >= 3 && this.furnaceItemStacks.get(1) != ItemStack.EMPTY && this.furnaceItemStacks.get(1).getItem().hasContainerItem(this.furnaceItemStacks.get(1))) {
				this.furnaceItemStacks.set(1, this.furnaceItemStacks.get(1).getItem().getContainerItem(this.furnaceItemStacks.get(1)));
			}

			if (canPushButton == 1) {
				if (this.furnaceCookingMethod == 0) {
					this.furnaceTimeToCook = ClayWorkTableRecipes.smelting().getKneadingTime(this.furnaceItemStacks.get(0), buttonId);
					this.furnaceCookingMethod = buttonId;
					this.furnaceItemStacks.set(4, this.furnaceItemStacks.get(0).splitStack(ClayWorkTableRecipes.smelting().getConsumedStackSize(this.furnaceItemStacks.get(0), buttonId)));
					if (this.furnaceItemStacks.get(0).getCount() <= 0) {
						this.furnaceItemStacks.set(0, ItemStack.EMPTY);
					}
				}

				++this.furnaceCookTime;
				if (this.furnaceCookTime >= this.furnaceTimeToCook) {
					int consumedStackSize = ClayWorkTableRecipes.smelting().getConsumedStackSize(this.furnaceItemStacks.get(4), buttonId);
					ItemStack itemstack = ClayWorkTableRecipes.smelting().getKneadingResult(this.furnaceItemStacks.get(4), buttonId);
					ItemStack itemstack2 = ClayWorkTableRecipes.smelting().getKneadingResult2(this.furnaceItemStacks.get(4), buttonId);
					this.furnaceCookTime = 0;
					this.furnaceCookingMethod = 0;
					ItemStack var10000;

					if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
						this.furnaceItemStacks.set(2, itemstack.copy());
					} else if (this.furnaceItemStacks.get(2).getItem() == itemstack.getItem()) {
						var10000 = this.furnaceItemStacks.get(2);
						var10000.setCount(var10000.getCount() + itemstack.getCount());
					}

					if (itemstack2 != null) {
						if (this.furnaceItemStacks.get(3) == ItemStack.EMPTY) {
							this.furnaceItemStacks.set(3, itemstack2.copy());
						} else if (this.furnaceItemStacks.get(3).getItem() == itemstack2.getItem()) {
							var10000 = this.furnaceItemStacks.get(3);
							var10000.setCount(var10000.getCount() + itemstack2.getCount());
						}
					}

					var10000 = this.furnaceItemStacks.get(4);
					var10000.setCount(var10000.getCount() - consumedStackSize);
					if (var10000.getCount() <= 0) {
						this.furnaceItemStacks.set(4, ItemStack.EMPTY);
					}
				}
			}

			if (canPushButton == 2) {
				this.furnaceCookTime = 0;
				this.furnaceCookingMethod = 0;
				this.furnaceItemStacks.set(4, ItemStack.EMPTY);
			}

			ClayWorkTable.updateBlockState(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
			this.markDirty();
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}

		public void smeltItem() {
			if (this.canSmelt()) {
				ItemStack itemstack = ClayWorkTableRecipes.smelting().getSmeltingResult(this.furnaceItemStacks.get(0));
				if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
					this.furnaceItemStacks.set(2, itemstack.copy());
				} else if (this.furnaceItemStacks.get(2).getItem() == itemstack.getItem()) {
					ItemStack var10000 = this.furnaceItemStacks.get(2);
					var10000.setCount(var10000.getCount() + itemstack.getCount());
				}

				this.furnaceItemStacks.get(0).setCount(this.furnaceItemStacks.get(0).getCount() - 1);
			}
		}

		public static int getItemBurnTime(ItemStack itemstack) {
			if (itemstack == null) {
				return 0;
			}
			
			Item item = itemstack.getItem();
			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
				Block block = Block.getBlockFromItem(item);
				if (block.getDefaultState().getMaterial() == Material.ROCK) {
					return 300;
				}
			}

			return item == ItemLargeClayBall.block ? 1600 : ForgeEventFactory.getItemBurnTime(itemstack);
		}

		public static boolean isItemTool(ItemStack itemstack) {
			return itemstack.getItem() == ItemClayRollingPin.block;
		}

		public static boolean isItemFuel(ItemStack itemstack) {
			return getItemBurnTime(itemstack) > 0;
		}

		public boolean isUsableByPlayer(EntityPlayer player) {
			return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64.0D;
		}

		public boolean isItemValidForSlot(int par1, ItemStack itemstack) {
			return par1 < 2 && (par1 != 1 || isItemTool(itemstack));
		}

		public int[] getSlotsForFace(EnumFacing par1) {
			return par1 == EnumFacing.DOWN ? slotsBottom : (par1 == EnumFacing.UP ? slotsTop : slotsSides);
		}

		@Override
		public boolean canInsertItem(int par1, ItemStack itemstack, EnumFacing par3) {
			return this.isItemValidForSlot(par1, itemstack);
		}

		@Override
		public boolean canExtractItem(int par1, ItemStack itemstack, EnumFacing par3) {
			return par3 != EnumFacing.DOWN || par1 != 1 || itemstack.getItem() == Items.BUCKET;
		}

		public void resetRecipeAndSlots() {
			this.resetRecipeAndSlots(this.merchant, this.currentRecipeIndex, this.furnaceItemStacks.get(0), this.furnaceItemStacks.get(1));
		}

		public void resetRecipeAndSlots(IMerchant merchant, int currentRecipeIndex, ItemStack itemstack, ItemStack itemstack1) {
			if (merchant != null && this.furnaceItemStacks.get(this.toSellSlotIndex) == ItemStack.EMPTY) {
				this.currentRecipe = null;
				if (itemstack == null) {
					itemstack = itemstack1;
					itemstack1 = null;
				}

				if (itemstack == null) {
					this.setInventorySlotContents(this.toSellSlotIndex, ItemStack.EMPTY);
				} else {
					MerchantRecipeList merchantrecipelist = merchant.getRecipes((EntityPlayer)null);
					if (merchantrecipelist != null) {
						MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, currentRecipeIndex);
						if (merchantrecipe == null) {}

						if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
							this.currentRecipe = merchantrecipe;
							this.setInventorySlotContents(this.toSellSlotIndex, merchantrecipe.getItemToSell().copy());
							this.onPickupFromMerchantSlot(merchantrecipe);
						} else if (itemstack1 != ItemStack.EMPTY) {
							merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);
							if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
								this.currentRecipe = merchantrecipe;
								this.setInventorySlotContents(this.toSellSlotIndex, merchantrecipe.getItemToSell().copy());
								this.onPickupFromMerchantSlot(merchantrecipe);
							}
						}
					}
				}

				merchant.verifySellingItem(this.getStackInSlot(this.toSellSlotIndex));
			}
		}

		public void setCurrentRecipeIndex(int p_70471_1_) {
			this.currentRecipeIndex = p_70471_1_;
			this.resetRecipeAndSlots();
		}

		public boolean inventoryResetNeededOnSlotChange(int par1) {
			return par1 == 0 || par1 == 1;
		}

		public void onPickupFromMerchantSlot(MerchantRecipe currentRecipe) {
			if (this.merchant != null) {
				if (currentRecipe != null) {
					ItemStack itemstack1 = this.furnaceItemStacks.get(0);
					ItemStack itemstack2 = this.furnaceItemStacks.get(1);
					if (this.doTrade(currentRecipe, itemstack1, itemstack2) || this.doTrade(currentRecipe, itemstack2, itemstack1)) {
						this.merchant.useRecipe(currentRecipe);
						if (itemstack1 != ItemStack.EMPTY && itemstack1.getCount() <= 0) {
							itemstack1 = null;
						}

						if (itemstack2 != ItemStack.EMPTY && itemstack2.getCount() <= 0) {
							itemstack2 = null;
						}

						this.setInventorySlotContents(0, itemstack1);
						this.setInventorySlotContents(1, itemstack2);
					}
				}

				this.markDirty();
				this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
			}
		}

		private boolean doTrade(MerchantRecipe recipe, ItemStack itemstack, ItemStack itemstack1) {
			ItemStack itemstack2 = recipe.getItemToBuy();
			ItemStack itemstack3 = recipe.getSecondItemToBuy();
			if (itemstack != ItemStack.EMPTY && itemstack.getItem() == itemstack2.getItem()) {
				if (itemstack3 != ItemStack.EMPTY && itemstack1 != ItemStack.EMPTY && itemstack3.getItem() == itemstack1.getItem()) {
					itemstack.setCount(itemstack.getCount() - itemstack2.getCount());
					itemstack1.setCount(itemstack.getCount() - itemstack3.getCount());
					return true;
				}

				if (itemstack3 == ItemStack.EMPTY && itemstack1 == ItemStack.EMPTY) {
					itemstack.setCount(itemstack.getCount() - itemstack2.getCount());
					return true;
				}
			}

			return false;
		}
		
		@Override
		public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
			return new GuiClayWorkTableGUI.GuiContainerMod(this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), playerIn);
		}

		@Override
		protected NonNullList<ItemStack> getItems() {
			return this.stacks;
		}
	}
}
