package mods.clayium.sample;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class ClayiumTransformerCore
        extends DummyModContainer {
    public ClayiumTransformerCore() {
        super(new ModMetadata());


        ModMetadata meta = getMetadata();
        meta.modId = "clayiumtransformer";
        meta.name = "ClayiumTransformer";
        meta.version = "0.0.forDebug";
    }


    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
