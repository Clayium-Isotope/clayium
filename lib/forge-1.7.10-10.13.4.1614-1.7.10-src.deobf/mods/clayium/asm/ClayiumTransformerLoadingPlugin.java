package mods.clayium.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;


public class ClayiumTransformerLoadingPlugin
        implements IFMLLoadingPlugin {
    static volatile File location;

    public String[] getLibraryRequestClass() {
        return null;
    }


    public String[] getASMTransformerClass() {
        return new String[] {"mods.clayium.asm.ClayiumTransformer"};
    }


    public String getModContainerClass() {
        return "mods.clayium.asm.ClayiumTransformerCore";
    }


    public String getSetupClass() {
        return null;
    }


    public void injectData(Map<String, Object> data) {
        if (location == null && data.containsKey("coremodLocation")) {
            location = (File) data.get("coremodLocation");
        }
    }


    public String getAccessTransformerClass() {
        return "mods.clayium.asm.ClayiumTransformer";
    }
}
