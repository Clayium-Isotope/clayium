package mods.clayium.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import mods.clayium.block.tile.Inventories;
import mods.clayium.gui.container.ContainerClayCraftingTable;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotCCrafting extends SlotCrafting {
    private final IInventory[] inventories;
    public int[] starts;
    public int[] widths;
    public int[] heights;
    private EntityPlayer thePlayer;
    public ContainerClayCraftingTable listener;

    public SlotCCrafting(EntityPlayer p_i1823_1_, ContainerClayCraftingTable listener, IInventory[] p_i1823_2_, int[] starts, int[] widths, int[] heights, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
        super(p_i1823_1_, (IInventory) new Inventories(p_i1823_2_), p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.thePlayer = p_i1823_1_;
        this.inventories = p_i1823_2_;
        this.starts = starts;
        this.widths = widths;
        this.heights = heights;
        this.listener = listener;
    }


    public boolean isItemValid(ItemStack p_75214_1_) {
        return false;
    }


    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
        IInventory craftMatrix = this.inventories[0];
        FMLCommonHandler.instance().firePlayerCraftingEvent(p_82870_1_, p_82870_2_, craftMatrix);
        if (!this.listener.world.isRemote) {
            onCrafting(p_82870_2_);
        }
        for (int i = this.starts[0]; i < this.widths[0] * this.heights[0]; i++) {

            ItemStack itemstack1 = craftMatrix.getStackInSlot(i);

            if (itemstack1 != null) {

                boolean flag = true;
                int j;
label41:
                for (j = 1; j < this.inventories.length; j++) {
                    for (int k = this.starts[j]; k < this.widths[j] * this.heights[j]; k++) {
                        ItemStack itemstack_ = this.inventories[j].getStackInSlot(k);
                        if (UtilItemStack.areItemDamageEqual(itemstack_, itemstack1)) {
                            flag = false;
                            this.inventories[j].decrStackSize(k, 1);
                            break label41;
                        }
                    }
                }
                if (flag) {
                    craftMatrix.decrStackSize(i, 1);
                }
                if (itemstack1.getItem().hasContainerItem(itemstack1)) {

                    ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

                    if (itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {

                        MinecraftForge.EVENT_BUS.post((Event) new PlayerDestroyItemEvent(this.thePlayer, itemstack2));


                    } else if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {

                        if (craftMatrix.getStackInSlot(i) == null) {

                            craftMatrix.setInventorySlotContents(i, itemstack2);
                        } else {

                            this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
                        }
                    }
                }
            }
        }
        this.listener.onCraftMatrixChanged(this.inventory);
    }
}
