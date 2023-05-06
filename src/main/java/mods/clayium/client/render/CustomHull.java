package mods.clayium.client.render;

import mods.clayium.util.UsedFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The value is texture path which TESR base.
 * <p>
 *
 * If you want to put <pre>"clayium:textures/blocks/machinehull-0.png"</pre>, then <pre>"blocks/machinehull-0"</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@UsedFor(UsedFor.Type.TileEntity)
public @interface CustomHull {
    String value();
}
