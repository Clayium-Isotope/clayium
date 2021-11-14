package mods.clayium.block.tile;

import java.util.ArrayList;

import mods.clayium.block.StorageContainer;
import mods.clayium.item.CItems;
import mods.clayium.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileStorageContainer extends TileClayContainer {
    public int extractSlotNum = 4;
    public int insertSlotNum = 4;
    public int maxStorageSize = 65536;

    protected int alternativeStackSize = 0;

    protected ItemStack[] alternativeItemStacks;
    protected ItemStack[] alternativeItemStacksLastTick;

    public TileStorageContainer() {
        this.insertRoutes = new int[] {-1, 0, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = new ItemStack[2];

        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.maxStorageSize = 65536;
        this.alternativeItemStacks = new ItemStack[this.extractSlotNum + this.insertSlotNum + 1];
        this.alternativeItemStacksLastTick = new ItemStack[this.extractSlotNum + this.insertSlotNum + 1];

        this.listSlotsInsert = new ArrayList<int[]>();
        this.listSlotsExtract = new ArrayList<int[]>();
        int[] slots = new int[this.extractSlotNum + this.insertSlotNum];
        int[] slots2 = new int[this.extractSlotNum + this.insertSlotNum];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
            slots2[i] = slots.length - i - 1;
        }
        this.listSlotsInsert.add(slots);
        this.listSlotsExtract.add(slots2);

        this.slotsDrop = new int[0];
    }

    public void updateEntity() {
        super.updateEntity();

        for (int i = 0; i < this.alternativeItemStacks.length; i++) {
            manageAlternativeStorage(i);
        }

        if (this.containerItemStacks[0] != null && (this.containerItemStacks[0]).stackSize == 0) {
            this.containerItemStacks[0] = null;
        }
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        if (UtilItemStack.areItemDamageEqual(itemStack, CItems.itemMisc.get("ClayCore")))
            return 99;
        return super.getItemUseMode(itemStack, player);
    }

    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, int side, int mode) {
        super.useItemFromSide(itemStack, player, side, mode);

        setSyncFlag();

        this.maxStorageSize = Integer.MAX_VALUE;
        if (mode == 99 && this.maxStorageSize < Integer.MAX_VALUE && --(player.getCurrentEquippedItem()).stackSize == 0)
            player.inventory.mainInventory[player.inventory.currentItem] = null;
    }

    public int getSizeInventory() {
        return this.insertSlotNum + this.extractSlotNum;
    }

    public int getStackSizeInSlot(int slot) {
        if (this.containerItemStacks[0] == null) return 0;
        if (slot < -1 || slot > this.extractSlotNum + this.insertSlotNum) return 0;

        int stackSize = (this.containerItemStacks[0]).stackSize;
        int maxStackSize = this.containerItemStacks[0].getMaxStackSize();
        int containerMaxStorageSize = this.maxStorageSize - this.alternativeStackSize;
        int extractSlotNum = this.extractSlotNum;
        int insertSlotNum = this.insertSlotNum;

        if (slot == this.extractSlotNum + this.insertSlotNum) {
            return Math.min(stackSize, maxStackSize);
        }

        int map[] = new int[this.extractSlotNum + this.insertSlotNum], p = 0;
        for (int i = 0; i < this.extractSlotNum + this.insertSlotNum; i++) {
            if (this.alternativeItemStacksLastTick[i] != null) {
                stackSize -= (this.alternativeItemStacksLastTick[i]).stackSize;
                containerMaxStorageSize -= maxStackSize;
                if (i == 0)
                    containerMaxStorageSize -= this.maxStorageSize - maxStackSize * (this.extractSlotNum + this.insertSlotNum);
                if (i < this.extractSlotNum) {
                    extractSlotNum--;
                } else {
                    insertSlotNum--;
                }
                map[i] = -1;
            } else {
                map[i] = p;
                p++;
            }
        }
        return getStackSizeInSlot(map[slot], stackSize, containerMaxStorageSize, maxStackSize, extractSlotNum, insertSlotNum);
    }

    public int getStackSizeInSlot(int slot, int containerStackSize, int containerMaxStorageSize, int itemMaxStackSize, int extractSlotNum, int insertSlotNum) {
        if (this.containerItemStacks[0] == null) return 0;
        if (slot < -1 || slot >= extractSlotNum + insertSlotNum) return 0;

        int stackSize = containerStackSize;
        int restStackSize = containerMaxStorageSize - stackSize;

        int maxStackSize = itemMaxStackSize;

        int filledSlots = stackSize / maxStackSize;
        int insertAreaStackSize = (insertSlotNum * maxStackSize > restStackSize) ? (insertSlotNum * maxStackSize - restStackSize) : 0;

        if (slot == 0 && extractSlotNum > 0 &&
                filledSlots >= extractSlotNum) {
            return stackSize - insertAreaStackSize - maxStackSize * (extractSlotNum - 1);
        }

        if (slot >= extractSlotNum && slot < extractSlotNum + insertSlotNum) {
            int slot0 = slot - extractSlotNum;
            filledSlots = insertAreaStackSize / maxStackSize;
            return (slot0 < filledSlots) ? maxStackSize : ((slot0 == filledSlots) ? (insertAreaStackSize - filledSlots * maxStackSize) : 0);
        }

        return (slot < filledSlots) ? maxStackSize : ((slot == filledSlots) ? (stackSize - filledSlots * maxStackSize) : 0);
    }

    private void debug(String str) {}

    public ItemStack getStackInSlot(int slot) {
        debug("getStackInSlot(" + slot + ")");

        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            return super.getStackInSlot(1);
        }

        if (this.alternativeItemStacks[slot] == null) {
            int size = getStackSizeInSlot(slot);
            if (size == 0) return null;

            ItemStack res = this.containerItemStacks[0].copy();
            res.stackSize = size;
            if (this.worldObj.isRemote) return res;

            this.alternativeItemStacksLastTick[slot] = res.copy();
            this.alternativeItemStacks[slot] = res.copy();
        }
        return this.alternativeItemStacks[slot];
    }

    public ItemStack decrStackSize(int slot, int size) {
        setSyncFlag();
        debug("decrStackSize(" + slot + "," + size + ")");

        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            return super.decrStackSize(1, size);
        }

        if (this.alternativeItemStacks[slot] == null) {
            int i = getStackSizeInSlot(slot);
            int j = Math.min(size, i);
            if (this.containerItemStacks[0] == null || j == 0) return null;

            ItemStack itemStack = this.containerItemStacks[0].splitStack(j);
            if ((this.containerItemStacks[0]).stackSize == 0) {
                this.containerItemStacks[0] = null;
            }
            return itemStack;
        }

        int stackSize = (this.alternativeItemStacks[slot]).stackSize;
        int resSize = Math.min(size, stackSize);
        ItemStack res = this.alternativeItemStacks[slot].splitStack(resSize);
        return res;
    }

    public ItemStack getStackInSlotOnClosing(int slot) {
        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            return super.getStackInSlotOnClosing(1);
        }
        return decrStackSize(slot, getStackSizeInSlot(slot));
    }

    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        debug("setInventorySlotContents(" + slot + "," + itemstack + "@" + ((itemstack == null) ? "n" : itemstack.hashCode()) + ")");

        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            super.setInventorySlotContents(1, itemstack);
            return;
        }

        if (this.alternativeItemStacks[slot] != null && this.alternativeItemStacks[slot] == itemstack) return;

        setInventorySlotContentsUnsafe(slot, itemstack);
        initializealternativeStorage(slot);
    }

    public void manageAlternativeStorage(int slot) {
        if (slot == 0 && this.alternativeStackSize > 0) {
            addToStorage(this.alternativeStackSize);
            this.alternativeStackSize = 0;
        }

        if (this.alternativeItemStacks[slot] != null) {
            if (this.containerItemStacks[0] == null) {
                if ((this.alternativeItemStacks[slot]).stackSize != (this.alternativeItemStacksLastTick[slot]).stackSize)
                    setInventorySlotContentsUnsafe(slot, this.alternativeItemStacks[slot]);
            } else {
                addToStorage((this.alternativeItemStacks[slot]).stackSize - (this.alternativeItemStacksLastTick[slot]).stackSize);
            }
            initializealternativeStorage(slot);
        }
    }

    public void initializealternativeStorage(int slot) {
        if (this.alternativeItemStacks[slot] != null) {
            (this.alternativeItemStacks[slot]).stackSize = 0;
            this.alternativeItemStacks[slot] = null;
            this.alternativeItemStacksLastTick[slot] = null;
        }

        if (slot == 0 && this.alternativeStackSize > 0) {
            addToStorage(this.alternativeStackSize);
            this.alternativeStackSize = 0;
        }
    }

    public void setInventorySlotContentsUnsafe(int slot, ItemStack itemstack) {
        setSyncFlag();
        if (this.containerItemStacks[0] == null) {
            if (itemstack != null) {
                this.containerItemStacks[0] = itemstack.copy();
            }
        } else {
            int setsize = (itemstack == null) ? 0 : itemstack.stackSize;
            int dif = setsize - getStackSizeInSlot(slot);
            if (this.alternativeItemStacksLastTick[slot] != null) {
                dif = setsize - (this.alternativeItemStacksLastTick[slot]).stackSize;
                (this.alternativeItemStacks[slot]).stackSize = (this.alternativeItemStacksLastTick[slot]).stackSize;
            }
            if (this.alternativeItemStacks[0] != null && dif > 0) {
                this.alternativeStackSize += dif;
            } else {
                addToStorage(dif);
            }
        }

        debug("done setInventorySlotContentsUnsafe(" + slot + "," + itemstack + "@" + ((itemstack == null) ? "n" : itemstack.hashCode()) + ")");
    }

    public void addToStorage(int stackSize) {
        if (this.maxStorageSize - (this.containerItemStacks[0]).stackSize < stackSize) {
            (this.containerItemStacks[0]).stackSize = this.maxStorageSize;
        } else {
            (this.containerItemStacks[0]).stackSize += stackSize;
        }
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return (slot >= this.insertSlotNum + this.extractSlotNum + 1 || ((this.containerItemStacks[0] == null ||
                UtilItemStack.areTypeEqual(itemstack, this.containerItemStacks[0])) && (this.containerItemStacks[1] == null ||
                checkFilterSlot(itemstack, this.containerItemStacks[1]))));
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        int[] slots = new int[this.insertSlotNum + this.extractSlotNum];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    public boolean canInsertItemUnsafe(int slot, ItemStack itemstack, int route) {
        ItemStack itemInSlot = getStackInSlot(slot);
        if (itemInSlot != null && itemInSlot.stackSize > itemInSlot.getMaxStackSize())
            return false;
        return isItemValidForSlot(slot, itemstack);
    }

    public boolean canExtractItemUnsafe(int slot, ItemStack itemstack, int route) {
        return true;
    }

    public boolean checkFilterSlot(ItemStack itemstack, ItemStack filter) {
        return UtilItemStack.areTypeEqual(itemstack, filter);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (this.containerItemStacks[0] != null && tagCompound.hasKey("ItemStackSize"))
            (this.containerItemStacks[0]).stackSize = tagCompound.getInteger("ItemStackSize");
        if (tagCompound.hasKey("MaxStorageSize"))
            this.maxStorageSize = tagCompound.getInteger("MaxStorageSize");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        for (int i = 0; i < this.alternativeItemStacks.length; i++) {
            manageAlternativeStorage(i);
        }

        if (this.containerItemStacks[0] != null) {
            tagCompound.setInteger("ItemStackSize", (this.containerItemStacks[0]).stackSize);
        } else {
            tagCompound.setInteger("ItemStackSize", 0);
        }
        tagCompound.setInteger("MaxStorageSize", this.maxStorageSize);
    }

    public int[] getSlotsDrop() {
        return new int[0];
    }

    public boolean shouldRefresh() {
        return false;
    }

    public boolean hasSpecialDrops() {
        return true;
    }

    public void setSyncFlag() {
        super.setSyncFlag();
        markForWeakUpdate();
    }

    public boolean acceptInterfaceSync() {
        return false;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, Block block, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, block, metadata, fortune);
        if (ret != null)
            for (int i = 0; i < ret.size(); i++) {
                ItemStack item = ret.get(i);
                ret.set(i, StorageContainer.expandStorage(item, StorageContainer.getStorageSize(item)));
            }
        return ret;
    }
}
