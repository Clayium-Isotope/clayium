package mods.clayium.plugin;

import java.lang.reflect.Method;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.ItemStack;

public class UtilGT {
    private static boolean gtLoaded = true;
    private static Method gtuAddToBlacklist = null;

    public static void addItemToBlackListGTUnification(ItemStack item) {
        if (gtLoaded && ClayiumCore.IntegrationID.GT.loaded())
            try {
                if (gtuAddToBlacklist == null) {
                    Class<?> clazz = null;
                    clazz = Class.forName("gregtech.api.util.GT_OreDictUnificator");
                    if (clazz != null) {
                        gtuAddToBlacklist = clazz.getMethod("addToBlacklist", new Class[] {ItemStack.class});
                    }
                }
                if (gtuAddToBlacklist != null) {
                    gtuAddToBlacklist.invoke(null, new Object[] {item});
                }
            } catch (Exception e) {
                ClayiumCore.logger.info("Failed to add item to black list of GT Unificator.");
                ClayiumCore.logger.catching(e);
                gtLoaded = false;
            }
    }
}
