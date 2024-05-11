package mods.clayium.gui;

import mods.clayium.item.filter.ContainerFilterString;
import mods.clayium.item.filter.ContainerFilterWhitelist;
import mods.clayium.item.filter.GuiFilterString;
import mods.clayium.item.gadget.ContainerGadgetAutoEat;
import mods.clayium.item.gadget.ContainerGadgetHolder;
import mods.clayium.machine.AreaMiner.ContainerAreaMiner;
import mods.clayium.machine.AreaMiner.GuiAreaMiner;
import mods.clayium.machine.AreaMiner.TileEntityAreaMiner;
import mods.clayium.machine.AutoClayCondenser.ContainerAutoClayCondenser;
import mods.clayium.machine.AutoClayCondenser.TileEntityAutoClayCondenser;
import mods.clayium.machine.AutoCrafter.ContainerAutoCrafter;
import mods.clayium.machine.AutoCrafter.TileEntityAutoCrafter;
import mods.clayium.machine.AutoTrader.ContainerAutoTrader;
import mods.clayium.machine.AutoTrader.GuiAutoTrader;
import mods.clayium.machine.AutoTrader.TileEntityAutoTrader;
import mods.clayium.machine.CACollector.ContainerCACollector;
import mods.clayium.machine.CACollector.TileEntityCACollector;
import mods.clayium.machine.CAReactor.GuiCAReactor;
import mods.clayium.machine.CAReactor.TileEntityCAReactor;
import mods.clayium.machine.ChemicalMetalSeparator.ContainerChemicalMetalSeparator;
import mods.clayium.machine.ChemicalMetalSeparator.GuiChemicalMetalSeparator;
import mods.clayium.machine.ChemicalMetalSeparator.TileEntityChemicalMetalSeparator;
import mods.clayium.machine.ClayAssembler.ContainerClayAssembler;
import mods.clayium.machine.ClayBlastFurnace.GuiClayBlastFurnace;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.machine.ClayCentrifuge.ContainerClayCentrifuge;
import mods.clayium.machine.ClayCentrifuge.TileEntityClayCentrifuge;
import mods.clayium.machine.ClayChemicalReactor.ContainerClayChemicalReactor;
import mods.clayium.machine.ClayChemicalReactor.TileEntityClayChemicalReactor;
import mods.clayium.machine.ClayContainer.GuiMultiPageContainer;
import mods.clayium.machine.ClayCraftingTable.ContainerClayCraftingTable;
import mods.clayium.machine.ClayCraftingTable.GuiClayCraftingTable;
import mods.clayium.machine.ClayCraftingTable.TileEntityClayCraftingTable;
import mods.clayium.machine.ClayDistributor.ContainerClayDistributor;
import mods.clayium.machine.ClayDistributor.TileEntityClayDistributor;
import mods.clayium.machine.ClayEnergyLaser.TileEntityClayEnergyLaser;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayWorkTable.GuiClayWorkTable;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.ClayiumMachine.GuiClayiumMachine;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.MultiblockMachine.TileEntityMultiblockMachine;
import mods.clayium.machine.MultitrackBuffer.ContainerMultitrackBuffer;
import mods.clayium.machine.MultitrackBuffer.TileEntityMultitrackBuffer;
import mods.clayium.machine.StorageContainer.ContainerStorageContainer;
import mods.clayium.machine.StorageContainer.GuiStorageContainer;
import mods.clayium.machine.StorageContainer.TileEntityStorageContainer;
import mods.clayium.machine.VacuumContainer.ContainerVacuumContainer;
import mods.clayium.machine.VacuumContainer.TileEntityVacuumContainer;
import mods.clayium.machine.common.Machine2To1;
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
                return new ContainerClayAssembler(player.inventory, (Machine2To1) tile);
            case GuiIdClayCentrifuge:
                return new ContainerClayCentrifuge(player.inventory, (TileEntityClayCentrifuge) tile);
            case GuiIdClayChemicalReactor:
            case GuiIdClayBlastFurnace:
            case GuiIdClayReactor:
                return new ContainerClayChemicalReactor(player.inventory, (TileEntityMultiblockMachine) tile);
            case GuiIdMultitrackBuffer:
                return new ContainerMultitrackBuffer(player.inventory, (TileEntityMultitrackBuffer) tile);
            case GuiIdItemFilterWhitelist:
                return new ContainerFilterWhitelist(player);
            case GuiIdItemFilterString:
                return new ContainerFilterString(player);
            case GuiIdClayEnergyLaser:
                return new ContainerNothing(player.inventory, (TileEntityClayEnergyLaser) tile);
            case GuiIdGadgetHolder:
                return new ContainerGadgetHolder(player);
            case GuiIdGadgetAutoEat:
                return new ContainerGadgetAutoEat(player);
            case GuiIdChemicalMetalSeparator:
                return new ContainerChemicalMetalSeparator(player.inventory, (TileEntityChemicalMetalSeparator) tile);
            case GuiIdAutoClayCondenser:
                return new ContainerAutoClayCondenser(player.inventory, (TileEntityAutoClayCondenser) tile);
            case GuiIdClayInterface:
                return null;
            case GuiIdClayDistributor:
                return new ContainerClayDistributor(player.inventory, (TileEntityClayDistributor) tile);
            case GuiIdAutoCrafter:
                return new ContainerAutoCrafter(player.inventory, (TileEntityAutoCrafter) tile);
            case GuiIdStorageContainer:
                return new ContainerStorageContainer(player.inventory, (TileEntityStorageContainer) tile);
            case GuiIdVacuumContainer:
                return new ContainerVacuumContainer(player.inventory, (TileEntityVacuumContainer) tile);
            case GuiIdAreaMiner:
                return new ContainerAreaMiner(player.inventory, (TileEntityAreaMiner) tile);
            case GuiIdAutoTrader:
                return new ContainerAutoTrader(player.inventory, (TileEntityAutoTrader) tile);
            case GuiIdCACollector:
                return new ContainerCACollector(player.inventory, (TileEntityCACollector) tile);
            case GuiIdCAReactor:
                return new ContainerClayiumMachine(player.inventory, (TileEntityCAReactor) tile);
        }

        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        switch (ID) {
            case GuiIdClayWorkTable:
                return new GuiClayWorkTable(
                        new ContainerClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile));
            case GuiIdClayCraftingTable:
                return new GuiClayCraftingTable(
                        new ContainerClayCraftingTable(player.inventory, (TileEntityClayCraftingTable) tile));
            case GuiIdClayMachines:
                return new GuiClayiumMachine(
                        new ContainerClayiumMachine(player.inventory, (TileEntityClayiumMachine) tile));
            case GuiIdNormalInventory:
                return new GuiMultiPageContainer(
                        new ContainerNormalInventory(player.inventory, (TileEntityClayBuffer) tile));
            case GuiIdClayAssembler:
                return new GuiIMachine(new ContainerClayAssembler(player.inventory, (Machine2To1) tile));
            case GuiIdClayCentrifuge:
                return new GuiIMachine(new ContainerClayCentrifuge(player.inventory, (TileEntityClayCentrifuge) tile));
            case GuiIdClayChemicalReactor:
                return new GuiIMachine(
                        new ContainerClayChemicalReactor(player.inventory, (TileEntityClayChemicalReactor) tile));
            case GuiIdMultitrackBuffer:
                return new GuiTemp(new ContainerMultitrackBuffer(player.inventory, (TileEntityMultitrackBuffer) tile));
            case GuiIdItemFilterWhitelist:
                return new GuiTemp(new ContainerFilterWhitelist(player));
            case GuiIdItemFilterString:
                return new GuiFilterString(new ContainerFilterString(player));
            case GuiIdClayEnergyLaser:
                return new GuiTemp(new ContainerNothing(player.inventory, (TileEntityClayEnergyLaser) tile));
            case GuiIdGadgetHolder:
                return new GuiTemp(new ContainerGadgetHolder(player));
            case GuiIdGadgetAutoEat:
                return new GuiTemp(new ContainerGadgetAutoEat(player));
            case GuiIdChemicalMetalSeparator:
                return new GuiChemicalMetalSeparator(
                        new ContainerChemicalMetalSeparator(player.inventory, (TileEntityChemicalMetalSeparator) tile));
            case GuiIdAutoClayCondenser:
                return new GuiTemp(
                        new ContainerAutoClayCondenser(player.inventory, (TileEntityAutoClayCondenser) tile));
            case GuiIdClayInterface:
                return null;
            case GuiIdClayBlastFurnace:
            case GuiIdClayReactor:
                return new GuiClayBlastFurnace(
                        new ContainerClayChemicalReactor(player.inventory, (TileEntityMultiblockMachine) tile));
            case GuiIdClayDistributor:
                return new GuiTemp(new ContainerClayDistributor(player.inventory, (TileEntityClayDistributor) tile));
            case GuiIdAutoCrafter:
                return new GuiTemp(new ContainerAutoCrafter(player.inventory, (TileEntityAutoCrafter) tile));
            case GuiIdStorageContainer:
                return new GuiStorageContainer(
                        new ContainerStorageContainer(player.inventory, (TileEntityStorageContainer) tile));
            case GuiIdVacuumContainer:
                return new GuiTemp(new ContainerVacuumContainer(player.inventory, (TileEntityVacuumContainer) tile));
            case GuiIdAreaMiner:
                return new GuiAreaMiner(new ContainerAreaMiner(player.inventory, (TileEntityAreaMiner) tile));
            case GuiIdAutoTrader:
                return new GuiAutoTrader(new ContainerAutoTrader(player.inventory, (TileEntityAutoTrader) tile));
            case GuiIdCACollector:
                return new GuiTemp(new ContainerCACollector(player.inventory, (TileEntityCACollector) tile));
            case GuiIdCAReactor:
                return new GuiCAReactor(new ContainerClayiumMachine(player.inventory, (TileEntityCAReactor) tile));
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
    public static final int GuiIdGadgetAutoEat = 24;
    public static final int GuiIdAreaActivator = 25;
    public static final int GuiIdClayCraftingTable = 30;
    public static final int GuiIdMultitrackBuffer = 31;
    public static final int GuiIdPANAdapter = 40;
    public static final int GuiIdPANCore = 41;
    public static final int GuiIdRFGenerator = 90;
    public static final int GuiIdClayInterface = 99;
}
