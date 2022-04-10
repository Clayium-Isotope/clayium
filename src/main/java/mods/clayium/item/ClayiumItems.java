package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.*;
import net.minecraft.item.Item;
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
                            || item.equals(clayShovel)
                            || item.equals(claySteelPickaxe)
                            || item.equals(claySteelShovel)) {
                        items.add(item);
//                        ClayiumCore.jsonHelper.generateSimpleItemJson(item.getUnlocalizedName().substring(5));
                    }
                }
                if (field.get(instance) instanceof ItemDamaged) {
                    for (Item item : (ItemDamaged) field.get(instance)) {
                        items.add(item);
//                        ClayiumCore.jsonHelper.generateSimpleItemJson(item.getUnlocalizedName().substring(5));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            ClayiumCore.logger.catching(e);
        }
    }

    public static List<Item> getItems() {
        if (items.isEmpty()) initItems();
        return items;
    }

    private static final ClayiumItems instance = new ClayiumItems();

    /* Elements... */
    public static final ItemDamaged clayShards = new ItemDamaged("compressed_clay_shard_", new int[] {1, 2, 3});

    /* Misc... */
    public static final Item clayCircuit = new ItemTiered("clay_circuit", 2);
    public static final Item simpleCircuit = new ItemTiered("simple_circuit", 3);
    public static final Item basicCircuit = new ItemTiered("basic_circuit", 4);
    public static final Item advancedCircuit = new ItemTiered("advanced_circuit", 5);
    public static final Item precisionCircuit = new ItemTiered("precision_circuit", 6);
    public static final Item integratedCircuit = new ItemTiered("integrated_circuit", 2);
    public static final Item clayCore = new ItemTiered("clay_core", 8);
    public static final Item clayBrain = new ItemTiered("clay_brain", 9);
    public static final Item claySpirit = new ItemTiered("clay_spirit", 10);
    public static final Item claySoul = new ItemTiered("clay_soul", 11);
    public static final Item clayAnima = new ItemTiered("clay_anima", 12);
    public static final Item clayPsyche = new ItemTiered("clay_psyche", 13);
    public static final Item clayCircuitBoard = new ItemTiered("clay_circuit_board", 2);
    public static final Item CEEBoard = new ItemTiered("cee_board", 3);
    public static final Item CEECircuit = new ItemTiered("cee_circuit", 3);
    public static final Item CEE = new ItemTiered("cee", 3);
    public static final Item laserParts = new ItemTiered("laser_parts", 7);
    public static final Item antimatterSeed = new ItemTiered("antimatter_seed", 9);
    public static final Item synchronousParts = new ItemTiered("synchronous_parts", 9);
    public static final Item teleportationParts = new ItemTiered("teleportation_parts", 11);
    public static final Item manipulator1 = new ItemTiered("manipulator_1", 6);
    public static final Item manipulator2 = new ItemTiered("manipulator_2", 8);
    public static final Item manipulator3 = new ItemTiered("manipulator_3", 12);
    /* ...Misc */

    /* Tools... */
    public static final Item rollingPin = new ClayiumItem("clay_rolling_pin") {{
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
    }};
    public static final Item rawRollingPin = new ClayiumItem("raw_clay_rolling_pin");
    public static final Item slicer = new ClayiumItem("clay_slicer") {{
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
    }};
    public static final Item rawSlicer = new ClayiumItem("raw_clay_slicer");
    public static final Item spatula = new ClayiumItem("clay_spatula") {{
        setMaxDamage(36);
        setMaxStackSize(1);
        setContainerItem(this);
    }};
    public static final Item rawSpatula = new ClayiumItem("raw_clay_spatula");

    public static final Item IOTool = new ClayPipingTools("io_tool");
    public static final Item pipingTool = new ClayPipingTools("piping_tool");
    public static final Item memoryCard = new ClayPipingTools("memory_card");
    public static final Item wrench = new ClayWrench();

    public static final Item clayPickaxe = new ClayPickaxe();
    public static final Item clayShovel = new ClayShovel();
    public static final Item claySteelPickaxe = new ClaySteelPickaxe();
    public static final Item claySteelShovel = new ClaySteelShovel();
    /* ...Tools */
    /* ...Elements */

    private static final List<Item> items = new ArrayList<>();

    public static boolean isItemTool(ItemStack itemstack) {
        for (Item item : new Item[]{
                ClayiumItems.rollingPin,
                ClayiumItems.slicer,
                ClayiumItems.spatula
        }) {
            if (itemstack.getItem() == item) return true;
        }
        return false;
    }
}
