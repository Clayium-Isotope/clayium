package mods.clayium.machine.ClayContainer;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.item.ClayiumItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.EnumMap;
import java.util.Map;

public abstract class TileEntityClayContainer extends TileGeneric implements ISidedInventory {
    protected NonNullList<ItemStack> containerItemStacks;
    protected String customName;
    protected int clayEnergyStorageSize = 1;

    protected boolean isPipe = false;

    public Map<EnumFacing, Integer> insertRoutes = new EnumMap<EnumFacing, Integer>(EnumFacing.class) {{
        put(EnumFacing.UP,    -1);
        put(EnumFacing.DOWN,  -1);
        put(EnumFacing.NORTH, -1);
        put(EnumFacing.SOUTH, -1);
        put(EnumFacing.WEST,  -1);
        put(EnumFacing.EAST,  -1);
    }};
    public Map<EnumFacing, Integer> extractRoutes = new EnumMap<EnumFacing, Integer>(EnumFacing.class) {{
        put(EnumFacing.UP,    -1);
        put(EnumFacing.DOWN,  -1);
        put(EnumFacing.NORTH, -1);
        put(EnumFacing.SOUTH, -1);
        put(EnumFacing.WEST,  -1);
        put(EnumFacing.EAST,  -1);
    }};
    public EnumFacing cePort;

    protected TileEntityClayContainer(int invSize) {
        super();
        containerItemStacks = NonNullList.withSize(invSize, ItemStack.EMPTY);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public int getSizeInventory() {
        return containerItemStacks.size();
    }

    public void growCEStorageSize(int dist) {
        clayEnergyStorageSize += dist;
        if (clayEnergyStorageSize > 64) {
            clayEnergyStorageSize = 64;
        }
    }

    public int getClayEnergyStorageSize() {
        return clayEnergyStorageSize;
    }

    public ItemStack getStackInSlot(int index) {
        return containerItemStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(containerItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(containerItemStacks, index);
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        ItemStack itemstack = this.containerItemStacks.get(par1);
        this.containerItemStacks.set(par1, ItemStack.EMPTY);
        return itemstack;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        containerItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : containerItemStacks)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    public void clear() {
        containerItemStacks.clear();
    }

    public String getName() {
        return hasCustomName() ? customName : getBlockType().getLocalizedName();
    }

    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName());
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) == this)
            return player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        containerItemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, containerItemStacks);

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        int[] temp;
        temp = compound.getIntArray("InsertRoutes");
        if (temp.length >= EnumFacing.VALUES.length) {
            for (int i = 0; i < EnumFacing.VALUES.length; i++) {
                this.insertRoutes.replace(EnumFacing.VALUES[i], temp[i]);
            }
        }

        temp = compound.getIntArray("ExtractRoutes");
        if (temp.length >= EnumFacing.VALUES.length) {
            for (int i = 0; i < EnumFacing.VALUES.length; i++) {
                this.extractRoutes.replace(EnumFacing.VALUES[i], temp[i]);
            }
        }

        this.isPipe = compound.getBoolean("IsPipe");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.containerItemStacks);

        if (hasCustomName()) {
            compound.setString("CustomName", customName);
        }

        compound.setIntArray("InsertRoutes", this.insertRoutes.values().stream().mapToInt(e -> e).toArray());
        compound.setIntArray("ExtractRoutes", this.extractRoutes.values().stream().mapToInt(e -> e).toArray());

        compound.setBoolean("IsPipe", this.isPipe);

        return compound;
    }

    // TODO: Divide to each Item#onItemUse()
    @Override
    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, EnumFacing side, int mode) {
        if (itemStack.getItem() == ClayiumItems.spatula) {
            this.isPipe = !this.isPipe;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 0, 0);
        }

        if (itemStack.getItem() == ClayiumItems.rollingPin) {
            this.insertRoutes.replace(side, this.insertRoutes.get(side) == 0 ? -1 : 0);
            player.sendMessage(new TextComponentString("Changed " + side.getName() + " Insert route to: " + this.insertRoutes.get(side)));
        }
        if (itemStack.getItem() == ClayiumItems.slicer) {
            this.extractRoutes.replace(side, this.extractRoutes.get(side) == 0 ? -1 : 0);
            player.sendMessage(new TextComponentString("Changed " + side.getName() + " Extract route to: " + this.extractRoutes.get(side)));
        }

        updateEntity();
    }

    @Override
    public void onLoad() {
        this.world.addBlockEvent(this.pos, this.getBlockType(), 0, 0);
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    /*
     * TESR のために、TE と Blockstate / Block の上手い橋渡しについて、模索中。
     */
    public ClayContainer.ClayContainerState getBlockState() {
        if (this.world.getBlockState(this.pos) instanceof ClayContainer.ClayContainerState)
            return (ClayContainer.ClayContainerState) this.world.getBlockState(this.pos);
        return null;
    }

    public boolean isPipe() {
        return isPipe;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }
}
