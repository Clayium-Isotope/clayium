package mods.clayium.gui;

import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.machine.ClayContainer.GuiMultiPageContainer;
import mods.clayium.machine.ClayCraftingTable.ContainerClayCraftingTable;
import mods.clayium.machine.ClayCraftingTable.GuiClayCraftingTable;
import mods.clayium.machine.ClayCraftingTable.TileEntityClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayWorkTable.GuiClayWorkTable;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.ClayiumMachine.GuiClayiumMachine;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.ContainerNormalInventory;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile != null) {
            switch (ID) {
                case clayWorkTableGuiID:
                    return new ContainerClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile);
                case clayCraftingTableGuiID:
                    return new ContainerClayCraftingTable(player.inventory, (TileEntityClayCraftingTable) tile);
                case clayBendingMachineGuiID:
                    return new ContainerClayiumMachine(player.inventory, (TileEntityClayiumMachine) tile);
                case clayBufferGuiID:
                    return new ContainerNormalInventory(player.inventory, (TileEntityClayBuffer) tile);
            }
        }

        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Gui getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile != null) {
            switch (ID) {
                case clayWorkTableGuiID:
                    return new GuiClayWorkTable(new ContainerClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile));
                case clayCraftingTableGuiID:
                    return new GuiClayCraftingTable(new ContainerClayCraftingTable(player.inventory, (TileEntityClayCraftingTable) tile));
                case clayBendingMachineGuiID:
                    return new GuiClayiumMachine(new ContainerClayiumMachine(player.inventory, (TileEntityClayiumMachine) tile));
                case clayBufferGuiID:
                    return new GuiMultiPageContainer(new ContainerNormalInventory(player.inventory, (TileEntityClayBuffer) tile));
            }
        }

        return null;
    }

    public static final int clayWorkTableGuiID = 0;
    public static final int clayBendingMachineGuiID = 9;
    public static final int clayBufferGuiID = 11;
    public static final int clayCraftingTableGuiID = 30;
}
