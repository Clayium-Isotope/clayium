package mods.clayium.machine.common;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerNormalInventory extends ContainerTemp {
    public static final int buttonIdPrevious = 6;
    public static final int buttonIdNext = 7;
    private final InventoryMultiPage impl;

    public ContainerNormalInventory(InventoryPlayer player, TileEntityClayBuffer inventory) {
        super(player, inventory);

        this.impl = new InventoryMultiPage(this.tileEntity);

        if (!(this.tileEntity instanceof INormalInventory)) return;

        this.machineGuiSizeY = this.impl.getInventoryY() * 18 + 18;
        if (this.impl.getInventoryY() > 6)
            this.machineGuiSizeY -= 20;

        this.machineGuiSizeX += (this.impl.getInventoryX() > 9) ? ((this.impl.getInventoryX() - 9) * 18) : 0;
        if (isLargeCallback())
            this.machineGuiSizeX += 8;

        postConstruct();
    }

    @Override
    protected boolean earlierConstruct() {
        return false;
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        int offsetY = 18;
        if (this.impl.getInventoryY() > 6)
            offsetY -= 12;

        for (int y = 0; y < this.impl.getInventoryY(); y++) {
            for (int x = 0; x < this.impl.getInventoryX(); x++) {
                addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, this.impl.getInventoryStart() + x + y * this.impl.getInventoryX(),
                        x * 18 + (this.machineGuiSizeX - 18 * this.impl.getInventoryX()) / 2 + 1, y * 18 + offsetY));
            }
        }
    }

    public boolean drawInventoryName() {
        return this.impl.getInventoryY() <= 6;
    }

    public boolean drawPlayerInventoryName() {
        return this.impl.getInventoryY() <= 6;
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return mergeItemStack(itemstack1, 0, this.playerSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }

    public boolean enchantItem(EntityPlayer player, int action) {
        if (action == buttonIdPrevious || action == buttonIdNext) {
            return super.enchantItem(player, action);
        }
        return true;
    }

    public boolean isMultiPage() {
        return this.impl.isMultiPage();
    }

    public int getPresentPageNum() {
        return this.impl.getPresentPage() + 1;
    }

    public int getMaxPageNum() {
        return this.impl.pageNum;
    }

    public boolean isLargeCallback() {
        return !drawPlayerInventoryName();
    }
}
