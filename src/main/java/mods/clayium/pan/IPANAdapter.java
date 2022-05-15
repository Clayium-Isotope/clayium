package mods.clayium.pan;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPANAdapter {
    ItemStack[] getPatternItems();

    ItemStack[] getSubItems();

    World getConnectedWorld();

    int getConnectedXCoord();

    int getConnectedYCoord();

    int getConnectedZCoord();
}
