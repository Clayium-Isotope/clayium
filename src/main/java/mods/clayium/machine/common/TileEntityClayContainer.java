package mods.clayium.machine.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class TileEntityClayContainer extends TileEntity {
    protected NonNullList<ItemStack> machineInventory;
    protected String customName;

    protected TileEntityClayContainer(int invSize) {
        super();
        machineInventory = NonNullList.withSize(invSize, ItemStack.EMPTY);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public int getSizeInventory() {
        return machineInventory.size();
    }

    public void growCEStorageSize(int dist) {
        clayEnergyStorageSize += dist;
        if (clayEnergyStorageSize > 64) {
            clayEnergyStorageSize = 64;
        }
    }

    public ItemStack getStackInSlot(int index) {
        return machineInventory.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(machineInventory, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(machineInventory, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        machineInventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : machineInventory)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    public void clear() {
        machineInventory.clear();
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

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        machineInventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, machineInventory);

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        ItemStackHelper.saveAllItems(compound, this.machineInventory);

        if (hasCustomName()) {
            compound.setString("CustomName", customName);
        }

        return compound;
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
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) == this)
            return player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        return false;
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}
}
