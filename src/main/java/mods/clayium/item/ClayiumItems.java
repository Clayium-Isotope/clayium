package mods.clayium.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClayiumItems {
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

    /* Tools... */
    public static final Item clayRollingPin = new ClayRollingPin();
    public static final Item rawClayRollingPin = new ClayiumItem("raw_clay_rolling_pin");
    public static final Item claySlicer = new ClaySlicer();
    public static final Item rawClaySlicer = new ClayiumItem("raw_clay_slicer");
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

    /* Materials... */
    /* Clay... */
    public static final Item clayPlate = new ClayiumItem("clay_plate");
    public static final Item clayStick = new ClayiumItem("clay_stick");
    public static final Item shortClayStick = new ClayiumItem("short_clay_stick");
    public static final Item clayRing = new ClayiumItem("clay_ring");
    public static final Item smallClayRing = new ClayiumItem("small_clay_ring");
    public static final Item clayGear = new ClayiumItem("clay_gear");
    public static final Item clayBlade = new ClayiumItem("clay_blade");
    public static final Item clayNeedle = new ClayiumItem("clay_needle");
    public static final Item clayDisc = new ClayiumItem("clay_disc");
    public static final Item smallClayDisc = new ClayiumItem("small_clay_disc");
    public static final Item clayCylinder = new ClayiumItem("clay_cylinder");
    public static final Item clayPipe = new ClayiumItem("clay_pipe");
    public static final Item largeClayBall = new ClayiumItem("large_clay_ball");
    public static final Item largeClayPlate = new ClayiumItem("large_clay_plate");
    public static final Item clayGrindingHead = new ClayiumItem("clay_grinding_head");
    public static final Item clayBearing = new ClayiumItem("clay_bearing");
    public static final Item claySpindle = new ClayiumItem("clay_spindle");
    public static final Item clayCuttingHead = new ClayiumItem("clay_cutting_head");
    public static final Item clayWaterWheel = new ClayiumItem("clay_water_wheel");
    /* ...Clay*/

    /* Dense Clay... */
    public static final Item denseClayPlate = new ClayiumItem("dense_clay_plate");
    public static final Item denseClayStick = new ClayiumItem("dense_clay_stick");
    /* ...Dense Clay */
    /* ...Materials */

    /* ...Elements */

    private static final List<Item> items = new ArrayList<>();

    public static boolean isItemTool(ItemStack itemstack) {
        for (Item item : new Item[]{
                ClayiumItems.clayRollingPin,
                ClayiumItems.claySlicer,
                ClayiumItems.claySpatula
        }) {
            if (itemstack.getItem() == item) return true;
        }
        return false;
    }
}
