package mods.clayium.item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.ClayPipingTools;
import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.ItemDamaged;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.item.filter.*;
import mods.clayium.item.gadget.*;
import mods.clayium.util.TierPrefix;

public class ClayiumItems {

    public static void initItems() {
        items.clear();

        try {
            for (Field field : ClayiumItems.class.getFields()) {
                if (field.get(instance) instanceof Item) {
                    if (field.get(instance) == Items.AIR) continue;

                    Item item = (Item) field.get(instance);

                    if (item instanceof ClayiumItem || item.equals(clayPickaxe) || item.equals(clayShovel) ||
                            item.equals(claySteelPickaxe) || item.equals(claySteelShovel)) {
                        items.add(item);
                        // ClayiumCore.jsonHelper.generateSimpleItemJson(item.getUnlocalizedName().substring(5));
                    }
                }
                if (field.get(instance) instanceof ItemDamaged) {
                    for (Item item : (ItemDamaged) field.get(instance)) {
                        items.add(item);
                        // ClayiumCore.jsonHelper.generateSimpleItemJson(item.getUnlocalizedName().substring(5));
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
    public static final ItemDamaged clayShards = new ItemDamaged("compressed_clay_shard_",
            new TierPrefix[] { TierPrefix.clay, TierPrefix.denseClay, TierPrefix.simple });

    /* Misc... */
    public static final Item clayCircuit = new ItemTiered("clay_circuit", TierPrefix.denseClay);
    public static final Item simpleCircuit = new ItemTiered("simple_circuit", TierPrefix.simple);
    public static final Item basicCircuit = new ItemTiered("basic_circuit", TierPrefix.basic);
    public static final Item advancedCircuit = new ItemTiered("advanced_circuit", TierPrefix.advanced);
    public static final Item precisionCircuit = new ItemTiered("precision_circuit", TierPrefix.precision);
    public static final Item integratedCircuit = new ItemTiered("integrated_circuit", TierPrefix.claySteel);
    public static final Item clayCore = new ItemTiered("clay_core", TierPrefix.clayium);
    public static final Item clayBrain = new ItemTiered("clay_brain", TierPrefix.ultimate);
    public static final Item claySpirit = new ItemTiered("clay_spirit", TierPrefix.antimatter);
    public static final Item claySoul = new ItemTiered("clay_soul", TierPrefix.pureAntimatter);
    public static final Item clayAnima = new ItemTiered("clay_anima", TierPrefix.OEC);
    public static final Item clayPsyche = new ItemTiered("clay_psyche", TierPrefix.OPA);
    public static final Item clayCircuitBoard = new ItemTiered("clay_circuit_board", TierPrefix.denseClay);
    public static final Item CEEBoard = new ItemTiered("cee_board", TierPrefix.simple);
    public static final Item CEECircuit = new ItemTiered("cee_circuit", TierPrefix.simple);
    public static final Item CEE = new ItemTiered("cee", TierPrefix.simple);
    public static final Item laserParts = new ItemTiered("laser_parts", TierPrefix.claySteel);
    public static final Item antimatterSeed = new ItemTiered("antimatter_seed", TierPrefix.ultimate);
    public static final Item synchronousParts = new SynchronousParts();
    public static final Item teleportationParts = new ItemTiered("teleportation_parts", TierPrefix.pureAntimatter);
    public static final Item manipulator1 = new ItemTiered("manipulator_1", TierPrefix.precision);
    public static final Item manipulator2 = new ItemTiered("manipulator_2", TierPrefix.clayium);
    public static final Item manipulator3 = new ItemTiered("manipulator_3", TierPrefix.OEC);
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

    public static final Item clayShooter0 = new ClayShooter(10000, "clay_shooter_0", TierPrefix.precision, 2, 1.2f,
            25.0f, 4, 6, 0);
    public static final Item clayShooter1 = new ClayShooter(1000, "clay_shooter_1", TierPrefix.precision, 4, 4.5f, 3.0f,
            7, 12, 0);
    public static final Item clayShooter2 = new ClayShooter(8000, "clay_shooter_2", TierPrefix.precision, 2, 15.0f,
            0.0f, 25, 8, 20);
    public static final Item clayShooter3 = new ClayShooter(2500, "clay_shooter_3", TierPrefix.precision, 3, 30.0f,
            0.0f, 100, 8, 40);

    public static final Item instantTeleporter = new InstantTeleporter(2500, "instant_teleporter",
            TierPrefix.pureAntimatter, 3, 30.0F, 0.0F, 100, 8, 40);

    public static final Item synchronizer = new Synchronizer();
    public static final Item directionMemory = new DirectionMemory();
    /* ...Tools */

    /* Filters... */
    public static final Item filterDuplicator = new FilterDuplicator();
    public static final Item filterWhitelist = new FilterWhitelist();
    public static final Item filterBlacklist = new FilterBlacklist();
    public static final Item filterFuzzy = new FilterWhitelist("filter_fuzzy", TierPrefix.claySteel, true);
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

    public static final Item gadgetAntimatterOverclock = new GadgetOverclock(0, TierPrefix.antimatter);
    public static final Item gadgetPureAntimatterOverclock = new GadgetOverclock(1, TierPrefix.pureAntimatter);
    public static final Item gadgetOECOverclock = new GadgetOverclock(2, TierPrefix.OEC);
    public static final Item gadgetOPAOverclock = new GadgetOverclock(3, TierPrefix.OPA);
    public static final Item gadgetFlight0 = new GadgetFlight(0, TierPrefix.OEC, 0);
    public static final Item gadgetFlight1 = new GadgetFlight(1, TierPrefix.OPA, 1);
    public static final Item gadgetFlight2 = new GadgetFlight(2, TierPrefix.OPA, 2);
    public static final Item gadgetHealth0 = new GadgetHealth(0, TierPrefix.precision, 1);
    public static final Item gadgetHealth1 = new GadgetHealth(1, TierPrefix.antimatter, 4);
    public static final Item gadgetHealth2 = new GadgetHealth(2, TierPrefix.OEC, 9);
    public static final Item gadgetAutoEat0 = new GadgetAutoEat(0, TierPrefix.claySteel);
    public static final Item gadgetAutoEat1 = new GadgetAutoEat(1, TierPrefix.claySteel);
    public static final Item gadgetRepeatedlyAttack = new GadgetRepeatedlyAttack();
    public static final Item gadgetLongArm0 = new GadgetLongArm(0, TierPrefix.precision, 3.0F);
    public static final Item gadgetLongArm1 = new GadgetLongArm(1, TierPrefix.clayium, 7.0F);
    public static final Item gadgetLongArm2 = new GadgetLongArm(2, TierPrefix.OEC, 20.0F);
    public static final Item gadgetBlank = new ItemTiered("gadget_blank", TierPrefix.precision);
    /* ...Gadget */
    /* ...Elements */

    private static final List<Item> items = new ArrayList<>();

    public static boolean isWorkTableTool(ItemStack itemstack) {
        for (Item item : new Item[] {
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
