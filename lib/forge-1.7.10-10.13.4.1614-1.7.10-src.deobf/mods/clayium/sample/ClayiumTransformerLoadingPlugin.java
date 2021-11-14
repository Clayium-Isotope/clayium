package mods.clayium.sample;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;


public class ClayiumTransformerLoadingPlugin
        implements IFMLLoadingPlugin {
    static File location;

    public String[] getLibraryRequestClass() {
        return null;
    }


    public String[] getASMTransformerClass() {
        return new String[] {"mods.clayium.sample.ClayiumTransformer"};
    }


    public String getModContainerClass() {
        return "mods.clayium.sample.ClayiumTransformerCore";
    }


    public String getSetupClass() {
        return null;
    }


    public void injectData(Map<String, Object> data) {
        if (data.containsKey("coremodLocation")) {
            location = (File) data.get("coremodLocation");
        }
    }


    public String getAccessTransformerClass() {
        return "mods.clayium.sample.ClayiumTransformer";
    }
}
