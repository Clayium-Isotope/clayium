package mods.clayium.machine.common;

import net.minecraft.entity.player.EntityPlayer;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface IButtonProvider {

    enum ButtonProperty {
        FAILURE,  // 継続不可
        PERMIT,   // 継続可
        PROPOSE   // 変更
    }

    ButtonProperty canPushButton(int button);

    boolean isButtonEnable(int button);

    void pushButton(EntityPlayer playerIn, int id);
}
