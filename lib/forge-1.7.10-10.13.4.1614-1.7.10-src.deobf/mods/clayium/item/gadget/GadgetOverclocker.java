package mods.clayium.item.gadget;

import mods.clayium.core.ClayiumCore;
import net.minecraft.entity.Entity;

public class GadgetOverclocker
        extends GadgetOrdinal {
    public GadgetOverclocker() {
        super(new String[] {"AntimatterOverclock", "PureAntimatterOverclock", "OECOverclock", "OPAOverclock"});
    }


    public void update(int itemIndex, Entity entity, boolean isRemote) {
        if (itemIndex >= 0 && isRemote && ClayiumCore.proxy.getClientPlayer() == entity)
            ClayiumCore.proxy.overclockPlayer(3 - itemIndex);
    }
}
