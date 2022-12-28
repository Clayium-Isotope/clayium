package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.ClayPipingTools;
import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.ItemDamaged;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.item.filter.*;
import mods.clayium.item.gadget.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClayiumItems {
    public static void initItems() {
        items.clear();

        try {
            for (Field field : ClayiumItems.class.getFields()) {
                if (field.get(instance) instanceof Item) {
                    if (field.get(instance) == Items.AIR) continue;

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
    public static final Item rollingPin = new ClayRollingPin();
    public static final Item rawRollingPin = new RawClayTools("raw_clay_rolling_pin");
    public static final Item slicer = new ClaySlicer();
    public static final Item rawSlicer = new RawClayTools("raw_clay_slicer");
    public static final Item spatula = new ClaySpatula();
    public static final Item rawSpatula = new RawClayTools("raw_clay_spatula");

    public static final Item IOTool = new ClayPipingTools("io_tool");
    public static final Item pipingTool = new ClayPipingTools("piping_tool");
    public static final Item memoryCard = new ClayPipingTools("memory_card");
    public static final Item wrench = new ClayWrench();

    public static final Item clayPickaxe = new ClayPickaxe();
    public static final Item clayShovel = new ClayShovel();
    public static final Item claySteelPickaxe = new ClaySteelPickaxe();
    public static final Item claySteelShovel = new ClaySteelShovel();

    public static final Item clayShooter0 = new ClayShooter(10000, "clay_shooter_0", 6, 2, 1.2f, 25.0f, 4, 6, 0);
    public static final Item clayShooter1 = new ClayShooter(1000, "clay_shooter_1", 6, 4, 4.5f, 3.0f, 7, 12, 0);
    public static final Item clayShooter2 = new ClayShooter(8000, "clay_shooter_2", 6, 2, 15.0f, 0.0f, 25, 8, 20);
    public static final Item clayShooter3 = new ClayShooter(2500, "clay_shooter_3", 6, 3, 30.0f, 0.0f, 100, 8, 40);

    public static final Item instantTeleporter = new InstantTeleporter(2500, "instant_teleporter", 11, 3, 30.0F, 0.0F, 100, 8, 40);
    /* ...Tools */

    /* Filters... */
    public static final Item filterDuplicator = new FilterDuplicator();
    public static final Item filterWhitelist = new FilterWhitelist();
    public static final Item filterBlacklist = new FilterBlacklist();
    public static final Item filterFuzzy = new FilterWhitelist("filter_fuzzy", 7, true);
    public static final Item filterOreDict = new FilterOreDict();
    public static final Item filterItemName = new FilterItemName();
    public static final Item filterUnlocalizedName = new FilterUnlocalizedName();
    public static final Item filterUniqueId = new FilterRegistryName();
    public static final Item filterModId = new FilterModId();
    public static final Item filterItemDamage = new FilterItemDamage();
    public static final Item filterBlockMetadata = new FilterBlockMetadata();
    /* ...Filters */

    /* Gadget... */
    public static final Item gadgetHolder = new GadgetHolder();

    public static final Item gadgetAntimatterOverclock = new GadgetOverclock(0, 10);
    public static final Item gadgetPureAntimatterOverclock = new GadgetOverclock(1, 11);
    public static final Item gadgetOECOverclock = new GadgetOverclock(2, 12);
    public static final Item gadgetOPAOverclock = new GadgetOverclock(3, 13);
    public static final Item gadgetFlight0 = new GadgetFlight(0, 12, 0);
    public static final Item gadgetFlight1 = new GadgetFlight(1, 13, 1);
    public static final Item gadgetFlight2 = new GadgetFlight(2, 13, 2);
    public static final Item gadgetHealth0 = new GadgetHealth(0, 6, 1);
    public static final Item gadgetHealth1 = new GadgetHealth(1, 10, 4);
    public static final Item gadgetHealth2 = new GadgetHealth(2, 12, 9);
    public static final Item gadgetAutoEat0 = new GadgetAutoEat(0, 7);
    public static final Item gadgetAutoEat1 = new GadgetAutoEat(1, 7);
    public static final Item gadgetRepeatedlyAttack = new GadgetRepeatedlyAttack();
    public static final Item gadgetLongArm0 = new GadgetLongArm(0, 6, 3.0F);
    public static final Item gadgetLongArm1 = new GadgetLongArm(1, 8, 7.0F);
    public static final Item gadgetLongArm2 = new GadgetLongArm(2, 12, 20.0F);
    /* ...Gadget */
    /* ...Elements */

    private static final List<Item> items = new ArrayList<>();

    public static boolean isWorkTableTool(ItemStack itemstack) {
        for (Item item : new Item[]{
                ClayiumItems.rollingPin,
                ClayiumItems.slicer,
                ClayiumItems.spatula
        }) {
            if (itemstack.getItem() == item) return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static boolean hasPipingTools(EntityPlayer player) {
        ItemStack main = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        ItemStack off = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        for (Item item : new Item[] { IOTool, pipingTool, memoryCard }) {
            if (main.getItem() == item || off.getItem() == item) return true;
        }
        return false;
    }
}
