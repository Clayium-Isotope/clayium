package mods.clayium.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * It helps you know what the interface is used for.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface UsedFor {

    Type value();

    enum Type {
        Block,
        Item, // Not ItemStack
        TileEntity,
        Entity,
        Gui,
        Container;
    }
}
