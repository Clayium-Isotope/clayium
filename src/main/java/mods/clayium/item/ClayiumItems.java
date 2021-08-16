package mods.clayium.item;

import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClayiumItems {
    public ClayiumItems() {}

    public static void initItems() {
        items.clear();

        try {
            for (Field field : ClayiumItems.class.getFields()) {
                if (field.get(instance) instanceof Item) {
                    Item item = (Item) field.get(instance);
                    items.add(item);
                }
            }
        } catch (IllegalAccessException ignore) {}
    }

    public static List<Item> getItems() {
        return items;
    }

    private static final ClayiumItems instance = new ClayiumItems();

    /* Elements... */

    /* Materials... */
    public static final Item clayBlade = new ClayBlade();
    public static final Item clayCylinder = new ClayCylinder();
    public static final Item clayDisc = new ClayDisc();
    public static final Item clayNeedle = new ClayNeedle();
    public static final Item clayPlate = new ClayPlate();
    public static final Item clayRing = new ClayRing();
    public static final Item clayStick = new ClayStick();
    public static final Item largeClayBall = new LargeClayBall();
    public static final Item largeClayPlate = new LargeClayPlate();
    public static final Item shortClayStick = new ShortClayStick();
    public static final Item smallClayDisc = new SmallClayDisc();
    public static final Item smallClayRing = new SmallClayRing();
    /* ...Materials */

    /* Tools... */
    public static final Item clayRollingPin = new ClayRollingPin();
    public static final Item rawClayRollingPin = new RawClayRollingPin();
    public static final Item claySpatula = new ClaySpatula();
    public static final Item rawClaySpatula = new RawClaySpatula();
    public static final Item claySlicer = new ClaySlicer();
    public static final Item rawClaySlicer = new RawClaySlicer();

    public static final Item[] clayTools = new Item[]{
            ClayiumItems.clayRollingPin,
            ClayiumItems.claySlicer,
            ClayiumItems.claySpatula
    };
    /* ...Tools */

    /* ...Elements */

    private static final List<Item> items = new ArrayList<>();
}
