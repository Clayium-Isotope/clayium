package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.ItemDamaged;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.*;

public class ClayiumItems {
    public static void initItems() {
        items.clear();

        try {
            for (Field field : ClayiumItems.class.getFields()) {
                if (field.get(instance) instanceof Item) {
                    Item item = (Item) field.get(instance);

                    if (item instanceof ClayiumItem
                            || item.equals(clayPickaxe)
                            || item.equals(clayShovel)) {
                        items.add(item);
                    }
                }
                if (field.get(instance) instanceof ItemDamaged) {
                    items.addAll((ItemDamaged) field.get(instance));
                }
                if (field.get(instance) == materialMap) {
                    for (CMaterial material : CMaterial.values()) {
                        for (CShape shape : CShape.values()) {
                            if (get(material, shape) instanceof ClayiumItem) {
                                items.add(get(material, shape));
                            }
                        }
                    }
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
    public static final Item rawClaySpatula = new ClayiumItem("raw_clay_spatula");

    public static final Item clayPickaxe = new ClayPickaxe();
    public static final Item clayShovel = new ClayShovel();
    /* ...Tools */

    /* Compressed Clay Shards ... */
    public static final ItemDamaged clayShards = new ItemDamaged("compressed_clay_shard_", new int[] {1, 2, 3});
    /* ... Compressed Clay Shards */

    /* Materials... */
    public enum CMaterial {
        clay("clay"),
        denseClay("dense_clay"),
        indClay("ind_clay"),
        advIndClay("adv_ind_clay");

        CMaterial(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        private final String name;
    }

    public enum CShape {
        plate("plate"),
        stick("stick"),
        shortStick("short_stick"),
        ring("ring"),
        smallRing("small_ring"),
        gear("gear"),
        blade("blade"),
        needle("needle"),
        disc("disc"),
        smallDisc("small_disc"),
        cylinder("cylinder"),
        pipe("pipe"),
        largeBall("large_ball"),
        largePlate("large_plate"),
        grindingHead("grinding_head"),
        bearing("bearing"),
        spindle("spindle"),
        cuttingHead("cutting_head"),
        waterWheel("water_wheel"),
        block("block"),
        ball("ball"),
        dust("dust"),
        plural("plural");

        CShape(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        private final String name;
    }

    public static final Map<CMaterial, Map<CShape, Item>> materialMap = new HashMap<>();
    static {
        for (CMaterial material : CMaterial.values()) {
            materialMap.put(material, new HashMap<>());
        }

        materialMap.get(CMaterial.clay).put(CShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(0)));
        materialMap.get(CMaterial.clay).put(CShape.ball, Items.CLAY_BALL);
        materialMap.get(CMaterial.clay).put(CShape.plural, Items.AIR);
        for (CShape shape : CShape.values()) {
            materialMap.get(CMaterial.clay).putIfAbsent(shape, new ClayiumItem(CMaterial.clay.getName() + "_" + shape.getName()));
        }

        materialMap.get(CMaterial.denseClay).put(CShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(1)));
        materialMap.get(CMaterial.denseClay).put(CShape.ball, Items.CLAY_BALL);
        materialMap.get(CMaterial.denseClay).put(CShape.largeBall, Items.AIR);
        materialMap.get(CMaterial.denseClay).put(CShape.plural, Items.AIR);
        for (CShape shape : CShape.values()) {
            materialMap.get(CMaterial.denseClay).putIfAbsent(shape, new ClayiumItem(CMaterial.denseClay.getName() + "_" + shape.getName()));
        }

        materialMap.get(CMaterial.indClay).putIfAbsent(CShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(3)));
        materialMap.get(CMaterial.indClay).putIfAbsent(CShape.plate, new ClayiumItem(CMaterial.indClay.getName() + "_" + CShape.plate.getName()));
        materialMap.get(CMaterial.indClay).putIfAbsent(CShape.largePlate, new ClayiumItem(CMaterial.indClay.getName() + "_" + CShape.largePlate.getName()));

        materialMap.get(CMaterial.advIndClay).putIfAbsent(CShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(4)));
        materialMap.get(CMaterial.advIndClay).putIfAbsent(CShape.plate, new ClayiumItem(CMaterial.advIndClay.getName() + "_" + CShape.plate.getName()));
        materialMap.get(CMaterial.advIndClay).putIfAbsent(CShape.largePlate, new ClayiumItem(CMaterial.advIndClay.getName() + "_" + CShape.largePlate.getName()));
    }
    public static Item get(CMaterial material, CShape shape) {
        return materialMap.get(material).get(shape);
    }
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
