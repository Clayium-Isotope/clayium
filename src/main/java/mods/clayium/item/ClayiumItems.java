package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.CompressedClay;
import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.*;
import mods.clayium.machine.TierPrefix;
import mods.clayium.util.ODHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
                    }
                }
                if (field.get(instance) instanceof ItemDamaged) {
                    items.addAll((ItemDamaged) field.get(instance));
                }
                if (field.get(instance) == materialMap) {
                    for (Map.Entry<ClayiumMaterial, Map<ClayiumShape, ItemStack>> entry : materialMap.entrySet()) {
                        for (Map.Entry<ClayiumShape, ItemStack> entry_ : entry.getValue().entrySet()) {
                            if (entry_.getValue().getItem() instanceof ClayiumItem) {
                                items.add(entry_.getValue().getItem());
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            ClayiumCore.logger.catching(e);
        }
    }

    public static List<Item> getItems() {
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
    public static final List<Item> circuits = Arrays.asList(clayCircuit, clayCircuit, clayCircuit, simpleCircuit, basicCircuit, advancedCircuit, precisionCircuit, integratedCircuit, clayCore, clayBrain, claySpirit, claySoul, clayAnima, clayPsyche);
    /* ...Misc */

    public static final Map<Integer, ClayiumMaterial> materials = new HashMap<>();
    /* Materials... */
    public static final Map<ClayiumMaterial, Map<ClayiumShape, ItemStack>> materialMap = new HashMap<ClayiumMaterial, Map<ClayiumShape, ItemStack>>() {{
        // ===== PARTS PART =====
        for (ClayiumMaterial material : materials.values()) {
            materialMap.put(material, new HashMap<>());
        }

        materialMap.get(ClayiumMaterial.clay).put(ClayiumShape.block, I(new ItemBlockCompressedClay((CompressedClay) ClayiumBlocks.compressedClays.get(0))));
        materialMap.get(ClayiumMaterial.clay).put(ClayiumShape.ball, I(Items.CLAY_BALL));
        for (ClayiumShape shape : ClayiumShape.values()) {
            add(ClayiumMaterial.clay, shape, 1);
        }

        materialMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.block, I(new ItemBlockCompressedClay((CompressedClay) ClayiumBlocks.compressedClays.get(1))));
        materialMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.ball, I(Items.CLAY_BALL));
        materialMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.largeBall, ItemStack.EMPTY);
        for (ClayiumShape shape : ClayiumShape.values()) {
            add(ClayiumMaterial.denseClay, shape, 2);
        }

        materialMap.get(ClayiumMaterial.indClay).put(ClayiumShape.block, I(new ItemBlockCompressedClay((CompressedClay) ClayiumBlocks.compressedClays.get(3))));
        add(ClayiumMaterial.indClay, ClayiumShape.plate, 3);
        add(ClayiumMaterial.indClay, ClayiumShape.largePlate, 3);

        materialMap.get(ClayiumMaterial.advClay).put(ClayiumShape.block, I(new ItemBlockCompressedClay((CompressedClay) ClayiumBlocks.compressedClays.get(4))));
        add(ClayiumMaterial.advClay, ClayiumShape.plate, 4);
        add(ClayiumMaterial.advClay, ClayiumShape.largePlate, 4);

        add(ClayiumMaterial.engClay, ClayiumShape.dust, 3);

        // ===== METAL PART =====
        //        for (Material material : GenericMaterial.values()) {
        //            materialMap.put(material, new HashMap<>());
        //        }
        //
        //        materialMap.get(GenericMaterial.aluminium).put(GenericShape.ingot, add(GenericMaterial.aluminium, GenericShape.ingot));

        for (ClayiumMaterial material : new ClayiumMaterial[] {ClayiumMaterial.rubidium, ClayiumMaterial.cerium, ClayiumMaterial.tantalum, ClayiumMaterial.praseodymium, ClayiumMaterial.protactinium,
                ClayiumMaterial.neptunium, ClayiumMaterial.promethium, ClayiumMaterial.samarium, ClayiumMaterial.curium, ClayiumMaterial.europium}) {
            materialMap.get(material).put(ClayiumShape.ingot, I(new ClayiumShapedMaterial(material, ClayiumShape.ingot, 0)));
        }

        addOD(ClayiumMaterial.antimatter, ClayiumShape.dust, 10);
        addOD(ClayiumMaterial.antimatter, ClayiumShape.gem, 10);
        addOD(ClayiumMaterial.antimatter, ClayiumShape.plate, 10);
        add(ClayiumMaterial.antimatter, ClayiumShape.largePlate, 10);

        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.dust, 11);
        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.gem, 11);
        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.plate, 11);
        add(ClayiumMaterial.pureAntimatter, ClayiumShape.largePlate, 11);

        for (int i = 1; i <= 7; i++) {
            addOD(ClayiumMaterial.compressedPureAntimatter.get(i), ClayiumShape.gem, i < 4 ? 11 : 12);
        }

        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.dust, 12);
        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.plate, 12);
        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.largePlate, 12);
        materialMap.get(ClayiumMaterial.octupleEnergeticClay).putIfAbsent(ClayiumShape.block, I(new ItemBlockCompressedClay((CompressedClay) ClayiumBlocks.compressedClays.get(13))));

        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.dust, 13);
        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.gem, 13);
        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.plate, 13);
        add(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.largePlate, 13);
    }};

    private static void add(ClayiumMaterial material, ClayiumShape shape) {
        materialMap.get(material).putIfAbsent(shape, I(new ClayiumItem(material, shape)));
    }
    private static void add(ClayiumMaterial material, ClayiumShape shape, int tier) {
        materialMap.get(material).putIfAbsent(shape, I(new ItemTiered(material, shape, tier)));
    }
    private static void addOD(ClayiumMaterial material, ClayiumShape shape, int tier) {
        List<ItemStack> list = OreDictionary.getOres(shape.getName() + material.getODName());
        if (list.isEmpty()) {
            add(material, shape);
            OreDictionary.registerOre(shape.getName() + material.getODName(), get(material, shape));
        }

        ItemStack stack = list.get(0).copy();
        ((ItemTiered) stack.getItem()).setTier(tier);
        materialMap.get(material).putIfAbsent(shape, stack);
    }
    public static ItemStack get(ClayiumMaterial material, ClayiumShape shape) {
        return materialMap.get(material).get(shape).copy();
    }
    public static ItemStack get(ClayiumMaterial material, ClayiumShape shape, int amount) {
        ItemStack stack = get(material, shape);
        stack.setCount(amount);
        return stack;
    }
    public static ItemStack get(TierPrefix material, ClayiumShape shape) {
        return get(material.getMaterial(), shape);
    }
    public static ItemStack get(int tier, ClayiumShape shape) {
        return get(TierPrefix.get(tier), shape);
    }
    public static ItemStack get(int tier, ClayiumShape shape, int amount) {
        return get(TierPrefix.get(tier).getMaterial(), shape, amount);
    }
    public static ItemStack getOD(ClayiumMaterial material, ClayiumShape shape) {
        return ODHelper.getODStack(shape.getName() + material.getODName());
    }
    public static ItemStack getOD(ClayiumMaterial material, ClayiumShape shape, int amount) {
        return ODHelper.getODStack(shape.getName() + material.getODName(), amount);
    }
    private static ItemStack I(Item item) {
        return new ItemStack(item);
    }
    /* ...Materials */

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
