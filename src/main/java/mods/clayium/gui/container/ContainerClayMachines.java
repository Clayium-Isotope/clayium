package mods.clayium.gui.container;

import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotResultWithTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerClayMachines
        extends ContainerTemp {
    protected long lastCraftTime;
    protected long lastTimeToCraft;
    protected TileClayMachines tileClayMachines;
    protected int materialSlotIndex;
    protected int resultSlotIndex;

    public ContainerClayMachines(InventoryPlayer player, TileClayMachines tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }

    protected void initParameters(InventoryPlayer player) {
        this.materialSlotIndex = 0;
        this.resultSlotIndex = 1;


        super.initParameters(player);
    }


    public void setTileEntity(IInventory tile) {
        super.setTileEntity(tile);
        this.tileClayMachines = (TileClayMachines) tile;
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer((Slot) new SlotWithTexture(this.tile, 0, 44, 35, RectangleTexture.LargeSlotTexture));
        addMachineSlotToContainer((Slot) new SlotResultWithTexture(this.tile, 1, 116, 35, RectangleTexture.LargeSlotTexture));
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return (Recipes.GetRecipes(this.tileClayMachines.getRecipeId()) == null) ? true : Recipes.GetRecipes(this.tileClayMachines.getRecipeId()).isCraftable(itemstack1, this.tileClayMachines.getRecipeTier());
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, this.materialSlotIndex, this.resultSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        if (slot >= this.resultSlotIndex) {
            if (!transferStackToPlayerInventory(itemstack1, true)) {

                return false;

            }
        } else if (!transferStackToPlayerInventory(itemstack1, false)) {
            return false;
        }
        return true;
    }
}
