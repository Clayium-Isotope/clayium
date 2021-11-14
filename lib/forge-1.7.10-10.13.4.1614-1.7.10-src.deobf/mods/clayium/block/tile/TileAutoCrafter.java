package mods.clayium.block.tile;

import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;

public class TileAutoCrafter
        extends TileClayContainerTiered {
    public int machineCraftTime;
    public int machineTimeToCraft = 20;
    public long machineConsumingEnergy = 10L;
    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public int numAutomation = 1;

    public void initParams() {
        super.initParams();


        this.containerItemStacks = new ItemStack[43];
        this.clayEnergySlot = 33;

        this.listSlotsInsert.add(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8});
        this.listSlotsExtract.add(new int[] {9, 10, 11, 12, 13, 14});
        this.insertRoutes = new int[] {-1, 0, -1, -1, -1, -1};
        this.maxAutoExtract = new int[] {-1, 1};
        this.extractRoutes = new int[] {0, -1, -1, -1, -1, -1};
        this.maxAutoInsert = new int[] {-1};
        this.slotsDrop = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};

        this.insertRoutes = new int[] {-1, 0, -1, 1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public void initParamsByTier(int tier) {
        switch (tier) {
            case 4:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
                this.autoExtractInterval = this.autoInsertInterval = 8;
                break;
            case 5:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 4;
                this.autoExtractInterval = this.autoInsertInterval = 4;
                this.multConsumingEnergy = 0.0F;
                break;
            case 6:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
                this.autoExtractInterval = this.autoInsertInterval = 2;
                this.multCraftTime = 0.05F;
                break;
            case 7:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.multCraftTime = 0.05F;
                this.multConsumingEnergy = 4.0F;
                this.numAutomation = 16;
                break;
            case 8:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.multCraftTime = 0.05F;
                this.multConsumingEnergy = 16.0F;
                this.numAutomation = 64;
                break;
            case 9:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 576;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.multCraftTime = 0.05F;
                this.multConsumingEnergy = 64.0F;
                this.numAutomation = 256;
                break;
            default:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
                this.autoExtractInterval = this.autoInsertInterval = 8;
                break;
        }

        if ((float) this.machineConsumingEnergy * this.multConsumingEnergy > 0.0F) {
            this.listSlotsInsert.add(new int[] {33});
        } else {
            for (int i = 0; i < 6; i++) {
                if (this.insertRoutes[i] == 1) {
                    this.insertRoutes[i] = -1;
                }
            }
        }
    }


    public void openInventory() {}


    public void closeInventory() {}

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot >= 15 && slot < 24)
            return true;
        boolean res = super.isItemValidForSlot(slot, itemstack);
        if (res && slot >= 0 && slot < 9) {
            ItemStack item = this.containerItemStacks[slot + 15];
            if (item != null) {
                if (ItemFilterTemp.isFilter(item)) {
                    return ItemFilterTemp.match(item, itemstack);
                }
                return UtilItemStack.areTypeEqual(item, itemstack);
            }
            return false;
        }
        return res;
    }


    public boolean acceptEnergyClay() {
        return ((float) this.machineConsumingEnergy * this.multConsumingEnergy > 0.0F);
    }


    public void updateEntity() {
        boolean breakflag = false;
        boolean update = true;
        for (int i = 0; i < this.numAutomation; i++) {
            if (update)
                super.updateEntity();
            if (!this.worldObj.isRemote) {
                if (canProceedCraft()) {
                    proceedCraft();
                    if (!this.succeedConsumingCE)
                        breakflag = true;
                    update = false;
                } else if (update == true) {
                    breakflag = true;
                } else {
                    update = true;
                }
                int k;
                for (k = 0; k < 9; k++) {
                    if (this.containerItemStacks[k] != null && (this.containerItemStacks[k]).stackSize > 1)
                        for (int l = 0; l < 9; l++) {
                            if (this.containerItemStacks[l] == null && isItemValidForSlot(l, this.containerItemStacks[k])) {
                                this.containerItemStacks[l] = this.containerItemStacks[k].splitStack(1);
                                if ((this.containerItemStacks[k]).stackSize == 1) {
                                    break;
                                }
                            }
                        }
                }
                for (k = 0; k < 9; k++) {
                    if (this.containerItemStacks[34 + k] != null &&
                            UtilTransfer.canProduceItemStack(this.containerItemStacks[34 + k], this.containerItemStacks, 9, 15, 64) >= (this.containerItemStacks[34 + k]).stackSize) {
                        UtilTransfer.produceItemStack(this.containerItemStacks[34 + k], this.containerItemStacks, 9, 15, 64);
                        this.containerItemStacks[34 + k] = null;
                    }
                }
            } else {

                breakflag = true;
            }
            if (breakflag && i >= 1) {
                break;
            }
        }
    }

    public boolean canProceedCraft() {
        ItemStack result = getResult();
        if (result == null)
            return false;
        for (int i = 0; i < 9; i++) {
            if (this.containerItemStacks[34 + i] != null)
                return false;
        }
        return (UtilTransfer.canProduceItemStack(result, this.containerItemStacks, 9, 15, 64) >= result.stackSize);
    }

    private boolean succeedConsumingCE = false;

    public void proceedCraft() {
        int consumingEnergy = (int) ((float) this.machineConsumingEnergy * this.multConsumingEnergy);
        int timeToCraft = (int) (this.machineTimeToCraft * this.multCraftTime);
        this.succeedConsumingCE = consumeClayEnergy(consumingEnergy);
        if (this.succeedConsumingCE) {
            boolean flag = true;
            int i;
            for (i = 0; i < 9; i++) {
                if (this.containerItemStacks[24 + i] != null) flag = false;
            }
            if (flag)
                for (i = 0; i < 9; i++) {
                    if (this.containerItemStacks[i] != null && this.containerItemStacks[15 + i] != null) {
                        this.containerItemStacks[24 + i] = this.containerItemStacks[i].splitStack(1);
                        if ((this.containerItemStacks[i]).stackSize <= 0) this.containerItemStacks[i] = null;

                    }
                }
            this.machineCraftTime++;
            if (this.machineCraftTime >= timeToCraft) {
                ItemStack itemstack = getResult();
                UtilTransfer.produceItemStack(itemstack, this.containerItemStacks, 9, 15, 64);
                for (int j = 0; j < 9; j++) {
                    if (this.containerItemStacks[24 + j] != null) {
                        ItemStack container = this.containerItemStacks[24 + j].getItem().getContainerItem(this.containerItemStacks[24 + j]);
                        if (container != null)
                            UtilTransfer.produceItemStack(container, this.containerItemStacks, 34, 43, 64);
                        (this.containerItemStacks[24 + j]).stackSize--;

                        if ((this.containerItemStacks[24 + j]).stackSize <= 0) this.containerItemStacks[24 + j] = null;
                    }
                }
                this.machineCraftTime = 0;
            }

            setSyncFlag();
        }
    }

    protected ItemStack getResult() {
        int i;
        for (i = 0; i < 9; i++) {
            if (this.containerItemStacks[i + 24] != null) {
                return getResult(this.containerItemStacks, 24);
            }
        }

        for (i = 0; i < 9; i++) {
            if ((this.containerItemStacks[i + 15] != null && this.containerItemStacks[i] == null) || (this.containerItemStacks[i + 15] == null && this.containerItemStacks[i] != null)) {
                return null;
            }
        }
        return getResult(this.containerItemStacks, 0);
    }

    private ItemStack[] oldContainer = new ItemStack[9];
    private ItemStack oldResult;

    protected ItemStack getResult(ItemStack[] container, int s) {
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            if (!UtilItemStack.areTypeEqual(this.oldContainer[i], container[i + s])) {
                flag = true;
                this.oldContainer[i] = (container[i + s] == null) ? null : container[i + s].copy();
            }
        }

        if (flag) {
            this.oldResult = CraftingManager.getInstance().findMatchingRecipe(InventoryCraftingInTile.getNormalCraftingGrid(this.containerItemStacks, s), this.worldObj);
        }

        return this.oldResult;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.machineCraftTime = tagCompound.getInteger("CraftTime");
        this.machineTimeToCraft = tagCompound.getInteger("TimeToCraft");
        this.machineConsumingEnergy = tagCompound.getLong("ConsumingEnergy");
        this.multCraftTime = tagCompound.getFloat("CraftTimeMultiplier");
        this.multConsumingEnergy = tagCompound.getFloat("ConsumingEnergyMultiplier");
        this.numAutomation = tagCompound.getInteger("NumAutoMation");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("CraftTime", this.machineCraftTime);
        tagCompound.setInteger("TimeToCraft", this.machineTimeToCraft);
        tagCompound.setLong("ConsumingEnergy", this.machineConsumingEnergy);
        tagCompound.setFloat("CraftTimeMultiplier", this.multCraftTime);
        tagCompound.setFloat("ConsumingEnergyMultiplier", this.multConsumingEnergy);
        tagCompound.setInteger("NumAutoMation", this.numAutomation);
    }
}
