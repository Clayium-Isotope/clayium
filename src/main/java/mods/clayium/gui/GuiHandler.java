package mods.clayium.gui;

import mods.clayium.machine.ClayAssembler.ContainerClayAssembler;
import mods.clayium.machine.ClayAssembler.TileEntityClayAssembler;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.machine.ClayCentrifuge.ContainerClayCentrifuge;
import mods.clayium.machine.ClayCentrifuge.TileEntityClayCentrifuge;
import mods.clayium.machine.ClayChemicalReactor.ContainerClayChemicalReactor;
import mods.clayium.machine.ClayChemicalReactor.TileEntityClayChemicalReactor;
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
import net.minecraft.client.gui.inventory.GuiContainer;
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
                case GuiIdClayWorkTable:
                    return new ContainerClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile);
                case GuiIdClayCraftingTable:
                    return new ContainerClayCraftingTable(player.inventory, (TileEntityClayCraftingTable) tile);
                case GuiIdClayMachines:
                    return new ContainerClayiumMachine(player.inventory, (TileEntityClayiumMachine) tile);
                case GuiIdNormalInventory:
                    return new ContainerNormalInventory(player.inventory, (TileEntityClayBuffer) tile);
                case GuiIdClayAssembler:
                    return new ContainerClayAssembler(player.inventory, (TileEntityClayAssembler) tile);
                case GuiIdClayCentrifuge:
                    return new ContainerClayCentrifuge(player.inventory, (TileEntityClayCentrifuge) tile);
                case GuiIdClayChemicalReactor:
                    return new ContainerClayChemicalReactor(player.inventory, (TileEntityClayChemicalReactor) tile);
            }
        }

        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile != null) {
            switch (ID) {
                case GuiIdClayWorkTable:
                    return new GuiClayWorkTable(new ContainerClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile));
                case GuiIdClayCraftingTable:
                    return new GuiClayCraftingTable(new ContainerClayCraftingTable(player.inventory, (TileEntityClayCraftingTable) tile));
                case GuiIdClayMachines:
                    return new GuiClayiumMachine(new ContainerClayiumMachine(player.inventory, (TileEntityClayiumMachine) tile));
                case GuiIdNormalInventory:
                    return new GuiMultiPageContainer(new ContainerNormalInventory(player.inventory, (TileEntityClayBuffer) tile));
                case GuiIdClayAssembler:
                    return new GuiClayiumMachine(new ContainerClayAssembler(player.inventory, (TileEntityClayAssembler) tile));
                case GuiIdClayCentrifuge:
                    return new GuiClayiumMachine(new ContainerClayCentrifuge(player.inventory, (TileEntityClayCentrifuge) tile));
                case GuiIdClayChemicalReactor:
                    return new GuiClayiumMachine(new ContainerClayChemicalReactor(player.inventory, (TileEntityClayChemicalReactor) tile));
            }
        }

        return null;
    }

    public static final int GuiIdClayWorkTable = 0;
    public static final int GuiIdClayMachines = 1;
    public static final int GuiIdClayAssembler = 2;
    public static final int GuiIdClayCentrifuge = 3;
    public static final int GuiIdClayChemicalReactor = 4;
    public static final int GuiIdAutoClayCondenser = 5;
    public static final int GuiIdChemicalMetalSeparator = 6;
    public static final int GuiIdClayBlastFurnace = 7;
    public static final int GuiIdClayReactor = 8;
    public static final int GuiIdCAReactor = 9;
    public static final int GuiIdClayWaterWheel = 10;
    public static final int GuiIdNormalInventory = 11;
    public static final int GuiIdSolarClayFabricator = 12;
    public static final int GuiIdClayEnergyLaser = 13;
    public static final int GuiIdClayDistributor = 14;
    public static final int GuiIdStorageContainer = 15;
    public static final int GuiIdAreaMiner = 16;
    public static final int GuiIdAutoCrafter = 17;
    public static final int GuiIdCACollector = 18;
    public static final int GuiIdAutoTrader = 19;
    public static final int GuiIdItemFilterWhitelist = 20;
    public static final int GuiIdItemFilterString = 21;
    public static final int GuiIdVacuumContainer = 22;
    public static final int GuiIdGadgetHolder = 23;
    public static final int GuiIdAreaActivator = 24;
    public static final int GuiIdClayCraftingTable = 30;
    public static final int GuiIdMultitrackBuffer = 31;
    public static final int GuiIdPANAdapter = 40;
    public static final int GuiIdPANCore = 41;
    public static final int GuiIdRFGenerator = 90;
    public static final int GuiIdClayInterface = 99;
}
