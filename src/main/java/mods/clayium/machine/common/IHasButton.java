package mods.clayium.machine.common;

import net.minecraft.entity.player.EntityPlayer;

public interface IHasButton {
    enum ButtonProperty {
        FAILURE,
        PERMIT,
        PROPOSE
    }

    ButtonProperty canPushButton(int button);

    boolean isButtonEnable(int button);

    void pushButton(EntityPlayer playerIn, int id);
}
