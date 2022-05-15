package mods.clayium.block.tile;

import java.util.ArrayList;

import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSaltExtractor
        extends TileCobblestoneGenerator {
    public static int energyPerWork = 30;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, 0, -1, -1};
        this.extractRoutes = new int[] {-1, -1, 0, -1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = true;
        this.clayEnergySlot = this.containerItemStacks.length;
        this.containerItemStacks = new ItemStack[this.clayEnergySlot + 1];
    }


    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        this.listSlotsInsert = new ArrayList<int[]>();
        this.listSlotsInsert.add(new int[] {this.clayEnergySlot});

        int[] slots = new int[this.slotsDrop.length + 1];
        for (int i = 0; i < this.slotsDrop.length; i++) {
            slots[i] = i;
        }
        slots[this.slotsDrop.length] = this.clayEnergySlot;
        this.slotsDrop = slots;
    }


    public void produce() {
        int count = countWater();
        if (this.externalControlState >= 0 && count > 0 && consumeClayEnergy(((long) this.progressEfficiency * energyPerWork))) {
            this.progress += this.progressEfficiency * count;
            while (this.progress >= progressMax) {
                setSyncFlag();
                ItemStack salt = CMaterials.get(CMaterials.SALT, CMaterials.DUST).copy();
                this.progress -= progressMax;


                UtilTransfer.produceItemStack(salt, this.containerItemStacks, 0, this.inventoryX * this.inventoryY, getInventoryStackLimit());

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;

                }
            }
        }
    }

    public boolean isScheduled() {
        return (countWater() > 0);
    }

    public int countWater() {
        ForgeDirection[] sides = {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.DOWN};

        int count = 0;
        for (ForgeDirection side : sides) {
            Block block = this.worldObj.getBlock(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ);
            if (block.getMaterial() == Material.water) {
                count++;
            }
        }
        return count;
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        return getAccessibleSlotsFromSideDefault(side);
    }


    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return canInsertItemDefault(slot, itemstack, side);
    }


    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return canExtractItemDefault(slot, itemstack, side);
    }


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return isItemValidForSlotDefault(slot, itemstack);
    }
}
