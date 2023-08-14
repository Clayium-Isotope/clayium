package mods.clayium.gui;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

public abstract class ContainerIMachine extends ContainerTemp {
    /**
     * The first index of material
     */
    protected final int materialSlotIndex;
    /**
     * The first index of result
     */
    protected final int resultSlotIndex;
    protected int timeToCraft;
    protected int craftTime;
    protected int containEnergy;

    /**
     * @param tile MUST BE IMPLEMENTING {@link TileEntityGeneric}
     */
    public ContainerIMachine(InventoryPlayer player, IMachine tile, int materialSlotStart, int resultSlotStart) {
        super(player, (TileEntityGeneric) tile);

        this.materialSlotIndex = materialSlotStart;
        this.resultSlotIndex = resultSlotStart;
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return !((ClayiumRecipeProvider<?>) tileEntity).getRecipe(itemstack1).isFlat();
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return mergeItemStack(itemstack1, this.materialSlotIndex, this.resultSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        if (slot >= this.resultSlotIndex) {
            return transferStackToPlayerInventory(itemstack1, true);
        }
        return transferStackToPlayerInventory(itemstack1, false);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.timeToCraft != this.tileEntity.getField(0)) {
                listener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
            }

            if (this.craftTime != this.tileEntity.getField(1)) {
                listener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
            }

            if (this.containEnergy != this.tileEntity.getField(2)) {
                listener.sendWindowProperty(this, 2, this.tileEntity.getField(2));
            }
        }

        this.timeToCraft = this.tileEntity.getField(0);
        this.craftTime = this.tileEntity.getField(1);
        this.containEnergy = this.tileEntity.getField(2);
    }
}
