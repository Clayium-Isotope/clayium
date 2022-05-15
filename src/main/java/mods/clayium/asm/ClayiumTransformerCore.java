package mods.clayium.asm;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.ArrayList;

public class ClayiumTransformerCore
        extends DummyModContainer {
    public ClayiumTransformerCore() {
        super(new ModMetadata());


        ModMetadata meta = getMetadata();
        meta.modId = "clayiumtransformer";
        meta.name = "Clayium Transformer";
        meta.version = "0.4.1";
        meta.authorList = new ArrayList();
        meta.authorList.add("deb_rk");
    }


    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
