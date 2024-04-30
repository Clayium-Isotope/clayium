package mods.clayium.block.tile;

import net.minecraft.entity.player.EntityPlayer;

public interface IGeneralInterface {

    void markForStrongUpdate();

    void markForWeakUpdate();

    void setSyncFlag();

    void setInstantSyncFlag();

    void setRenderSyncFlag();

    void pushButton(EntityPlayer playerIn, int button);
}
