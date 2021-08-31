package mods.clayium.gui;

import mods.clayium.machines.ClayWorkTable.TileEntityClayWorkTable;
import mods.clayium.machines.ClayWorkTable.GuiClayWorkTable;
import mods.clayium.machines.ClayWorkTable.ContainerClayWorkTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile != null) {
            switch (ID) {
                case clayWorkTableGuiID:
                    return new ContainerClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile);
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile != null) {
            switch (ID) {
                case clayWorkTableGuiID:
                    return new GuiClayWorkTable(player.inventory, (TileEntityClayWorkTable) tile);
            }
        }

        return null;
    }

    public static final int clayWorkTableGuiID = 0;
}
