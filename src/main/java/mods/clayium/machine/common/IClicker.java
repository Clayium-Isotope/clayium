package mods.clayium.machine.common;

import net.minecraft.entity.player.EntityPlayer;

public interface IClicker {
    enum ButtonProperty {
        FAILURE,
        PERMIT,
        PROPOSE
    }

    void pushButton(EntityPlayer playerIn, int id);
}
