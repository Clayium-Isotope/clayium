package mods.clayium.gui;

import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.gui.client.GuiClayWorkTable;
import mods.clayium.gui.container.ContainerClayWorkTable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
    public GuiHandler() {}

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Block block = world != null ? world.getBlockState(new BlockPos(x, y, z)).getBlock() : null;
        TileEntity tile = world != null ? world.getTileEntity(new BlockPos(x, y, z)) : null;
        InventoryPlayer playerInv = player != null ? player.inventory : null;

        switch(ID) {
            case GuiClayWorkTableId:
                return tile == null ? null : new ContainerClayWorkTable(playerInv, (TileClayWorkTable)tile);
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Block block = world != null ? world.getBlockState(new BlockPos(x, y, z)).getBlock() : null;
        TileEntity tile = world != null ? world.getTileEntity(new BlockPos(x, y, z)) : null;
        InventoryPlayer playerInv = player != null ? player.inventory : null;

        switch(ID) {
            case GuiClayWorkTableId:
                return tile == null ? null : new GuiClayWorkTable(playerInv, (TileClayWorkTable)tile);
        }

        return null;
    }

    public static final int GuiClayWorkTableId = 0;
}
