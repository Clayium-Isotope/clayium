package mods.clayium.gui.container;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import mods.clayium.block.tile.TilePANCore;
import mods.clayium.core.ClayiumCore;
import mods.clayium.network.PANCoreListPacket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerPANCore extends ContainerTemp {
    public ContainerPANCore(InventoryPlayer player, TilePANCore tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY += 72;
        super.initParameters(player);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {}


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return false;
    }


    public void addCraftingToCrafters(ICrafting p_75132_1_) {
        super.addCraftingToCrafters(p_75132_1_);
        if (p_75132_1_ instanceof EntityPlayerMP) {
            ClayiumCore.packetDispatcher.sendTo((IMessage) new PANCoreListPacket(this.windowId, (TilePANCore) this.tile), (EntityPlayerMP) p_75132_1_);
        }
    }
}
