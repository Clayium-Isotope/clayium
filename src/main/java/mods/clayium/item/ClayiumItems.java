package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.ItemDamaged;
import mods.clayium.item.common.MaterialShape.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClayiumItems {
    public static void initItems() {
        items.clear();

        try {
            for (Field field : ClayiumItems.class.getFields()) {
                if (field.get(instance) instanceof Item) {
                    Item item = (Item) field.get(instance);

                    if (item instanceof ClayiumItem
                            || item.equals(clayPickaxe)
                            || item.equals(clayShovel)
                            || item.equals(claySteelPickaxe)
                            || item.equals(claySteelShovel)) {
                        items.add(item);
                    }
                }
                if (field.get(instance) instanceof ItemDamaged) {
                    items.addAll((ItemDamaged) field.get(instance));
                }
                if (field.get(instance) == materialMap) {
                    for (Material material : ClayMaterial.values()) {
                        for (ClayShape shape : ClayShape.values()) {
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
    public static final Item claySteelPickaxe = new ClaySteelPickaxe();
    public static final Item claySteelShovel = new ClaySteelShovel();
    /* ...Tools */

    /* Compressed Clay Shards ... */
    public static final ItemDamaged clayShards = new ItemDamaged("compressed_clay_shard_", new int[] {1, 2, 3});
    /* ... Compressed Clay Shards */

    /* Materials... */
    public static final Map<Material, Map<IShape, Item>> materialMap = new HashMap<>();
    static {
        // ===== PARTS PART =====
        for (Material material : ClayMaterial.values()) {
            materialMap.put(material, new HashMap<>());
        }

        materialMap.get(ClayMaterial.clay).put(ClayShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(0)));
        materialMap.get(ClayMaterial.clay).put(ClayShape.ball, Items.CLAY_BALL);
        for (ClayShape shape : ClayShape.values()) {
            materialMap.get(ClayMaterial.clay).putIfAbsent(shape, new ClayiumItem(ClayMaterial.clay, shape));
        }

        materialMap.get(ClayMaterial.denseClay).put(ClayShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(1)));
        materialMap.get(ClayMaterial.denseClay).put(ClayShape.ball, Items.CLAY_BALL);
        materialMap.get(ClayMaterial.denseClay).put(ClayShape.largeBall, Items.AIR);
        for (ClayShape shape : ClayShape.values()) {
            materialMap.get(ClayMaterial.denseClay).putIfAbsent(shape, new ClayiumItem(ClayMaterial.denseClay, shape));
        }

        materialMap.get(ClayMaterial.indClay).put(ClayShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(3)));
        materialMap.get(ClayMaterial.indClay).put(ClayShape.plate, new ClayiumItem(ClayMaterial.indClay, ClayShape.plate));
        materialMap.get(ClayMaterial.indClay).put(ClayShape.largePlate, new ClayiumItem(ClayMaterial.indClay, ClayShape.largePlate));

        materialMap.get(ClayMaterial.advIndClay).put(ClayShape.block, new ItemBlock(ClayiumBlocks.compressedClays.get(4)));
        materialMap.get(ClayMaterial.advIndClay).put(ClayShape.plate, new ClayiumItem(ClayMaterial.advIndClay, ClayShape.plate));
        materialMap.get(ClayMaterial.advIndClay).put(ClayShape.largePlate, new ClayiumItem(ClayMaterial.advIndClay, ClayShape.largePlate));

        // ===== METAL PART =====
//        for (Material material : GenericMaterial.values()) {
//            materialMap.put(material, new HashMap<>());
//        }
//
//        materialMap.get(GenericMaterial.aluminum).put(GenericShape.ingot, new ClayiumItem(GenericMaterial.aluminum, GenericShape.ingot));

    }
    public static Item get(Material material, IShape shape) {
        return materialMap.get(material).get(shape);
    }
    public static ItemStack getOD(Material material, IShape shape, int amount) {
        List<ItemStack> oreList = OreDictionary.getOres(shape.getName() + material.getODName());
        if (oreList == null || oreList.isEmpty()) return ItemStack.EMPTY;

        ItemStack res = oreList.get(0).copy();
        res.setCount(amount);
        return res;
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
