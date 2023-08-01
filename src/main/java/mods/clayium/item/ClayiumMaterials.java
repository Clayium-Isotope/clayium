package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.common.MaterialBlock;
import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.*;
import mods.clayium.util.ODHelper;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class ClayiumMaterials {
    private static final Map<ClayiumMaterial, Map<ClayiumShape, ItemStack>> materialShapeMap = new EnumMap<>(ClayiumMaterial.class);
    public static List<Item> getItems() {
        List<Item> res = new ArrayList<>();
        for (Map<ClayiumShape, ItemStack> entry : ClayiumMaterials.materialShapeMap.values()) {
            for (ItemStack entry_ : entry.values()) {
                if (entry_.getItem() instanceof ClayiumShapedMaterial)
                    res.add(entry_.getItem());
            }
        }
        return res;
    }
    public static List<ItemStack> getMaterials() {
        List<ItemStack> res = new ArrayList<>();
        for (Map<ClayiumMaterial, ItemStack> map : Arrays.asList(plates, largePlates, dusts, ingots, gems)) {
            res.addAll(map.values());
        }
        return res;
    }

    private static final Map<String, ItemStack> ODReserve = new HashMap<>();
    public static void registerOres() {
        for (Map.Entry<String, ItemStack> ore : ODReserve.entrySet()) {
            OreDictionary.registerOre(ore.getKey(), ore.getValue());
        }
    }

    public static Map<ClayiumShape, ItemStack> clayParts;
    public static Map<ClayiumShape, ItemStack> denseClayParts;
    public static final Map<ClayiumMaterial, ItemStack> plates = new EnumMap<>(ClayiumMaterial.class);
    public static final Map<ClayiumMaterial, ItemStack> largePlates = new EnumMap<>(ClayiumMaterial.class);
    public static final Map<ClayiumMaterial, ItemStack> dusts = new EnumMap<>(ClayiumMaterial.class);
    public static final Map<ClayiumMaterial, ItemStack> ingots = new EnumMap<>(ClayiumMaterial.class);
    public static final Map<ClayiumMaterial, ItemStack> gems = new EnumMap<>(ClayiumMaterial.class);
    public static final Map<ClayiumMaterial, ItemStack> blocks = new EnumMap<>(ClayiumMaterial.class);

    public static void registerMaterials() {
        for (ClayiumMaterial material : ClayiumMaterial.values()) {
            materialShapeMap.put(material, new EnumMap<>(ClayiumShape.class));
        }

        materialShapeMap.get(ClayiumMaterial.clay).put(ClayiumShape.ball, new ItemStack(Items.CLAY_BALL));
        materialShapeMap.get(ClayiumMaterial.clay).put(ClayiumShape.block, new ItemStack(Blocks.CLAY));
        for (ClayiumShape shape : ClayiumShape.clayPartShapes) {
            add(ClayiumMaterial.clay, shape, TierPrefix.clay, true);
        }
        clayParts = materialShapeMap.get(ClayiumMaterial.clay);

        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.ball, ItemStack.EMPTY);
        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.largeBall, ItemStack.EMPTY);
        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.block, ItemStack.EMPTY);
        for (ClayiumShape shape : ClayiumShape.clayPartShapes) {
            add(ClayiumMaterial.denseClay, shape, TierPrefix.denseClay, true);
        }
        denseClayParts = materialShapeMap.get(ClayiumMaterial.denseClay);

        add(ClayiumMaterial.indClay, ClayiumShape.plate, TierPrefix.simple, true);
        add(ClayiumMaterial.indClay, ClayiumShape.largePlate, TierPrefix.simple, true);

        add(ClayiumMaterial.advClay, ClayiumShape.plate, TierPrefix.basic, true);
        add(ClayiumMaterial.advClay, ClayiumShape.largePlate, TierPrefix.basic, true);

//        add(ClayiumMaterial.clay, ClayiumShape.dust, 1, true);
//        add(ClayiumMaterial.denseClay, ClayiumShape.dust, 2, true);
        addOD(ClayiumMaterial.indClay, ClayiumShape.dust, TierPrefix.simple, true);
        addOD(ClayiumMaterial.advClay, ClayiumShape.dust, TierPrefix.basic, true);
        addOD(ClayiumMaterial.engClay, ClayiumShape.dust, TierPrefix.simple, true);
        addOD(ClayiumMaterial.excClay, ClayiumShape.dust, TierPrefix.claySteel, true);

        addOD(ClayiumMaterial.salt, ClayiumShape.dust, TierPrefix.basic, true);
        add(ClayiumMaterial.calClay, ClayiumShape.dust, TierPrefix.basic, true);
        add(ClayiumMaterial.calciumChloride, ClayiumShape.dust, TierPrefix.basic, true);
        add(ClayiumMaterial.sodiumCarbonate, ClayiumShape.dust, TierPrefix.basic, true);
        addOD(ClayiumMaterial.quartz, ClayiumShape.dust, TierPrefix.basic, true);

        addOD(ClayiumMaterial.impureSilicon, ClayiumShape.dust, TierPrefix.advanced, false);
        addOD(ClayiumMaterial.impureSilicon, ClayiumShape.ingot, TierPrefix.advanced, true);
        addOD(ClayiumMaterial.impureSilicon, ClayiumShape.plate, TierPrefix.advanced, true);
        add(ClayiumMaterial.impureSilicon, ClayiumShape.largePlate, TierPrefix.advanced, true);

        addOD(ClayiumMaterial.silicone, ClayiumShape.dust, TierPrefix.advanced, false);
        addOD(ClayiumMaterial.silicone, ClayiumShape.ingot, TierPrefix.advanced, false);
        addOD(ClayiumMaterial.silicone, ClayiumShape.plate, TierPrefix.advanced, false);
        add(ClayiumMaterial.silicone, ClayiumShape.largePlate, TierPrefix.advanced, false);

        addOD(ClayiumMaterial.silicon, ClayiumShape.dust, TierPrefix.advanced, false);
        addOD(ClayiumMaterial.silicon, ClayiumShape.ingot, TierPrefix.advanced, false);
        addOD(ClayiumMaterial.silicon, ClayiumShape.plate, TierPrefix.advanced, false);
        add(ClayiumMaterial.silicon, ClayiumShape.largePlate, TierPrefix.advanced, false);

        addOD(ClayiumMaterial.aluminium, ClayiumShape.dust, TierPrefix.precision, false);
        addOD(ClayiumMaterial.aluminium, ClayiumShape.ingot, TierPrefix.precision, false);
        addOD(ClayiumMaterial.aluminium, ClayiumShape.plate, TierPrefix.precision, false);
        add(ClayiumMaterial.aluminium, ClayiumShape.largePlate, TierPrefix.precision, false);

        if (ClayiumConfiguration.cfgHardcoreAluminium) {
            add(ClayiumMaterial.impureAluminium, ClayiumShape.dust, TierPrefix.precision, false);
            add(ClayiumMaterial.impureAluminium, ClayiumShape.ingot, TierPrefix.precision, false);
            add(ClayiumMaterial.impureAluminium, ClayiumShape.plate, TierPrefix.precision, false);
            add(ClayiumMaterial.impureAluminium, ClayiumShape.largePlate, TierPrefix.precision, false);
        } else {
            add(ClayiumMaterial.impureAluminium, ClayiumShape.dust, TierPrefix.unknown, false);
        }

        addOD(ClayiumMaterial.claySteel, ClayiumShape.dust, TierPrefix.claySteel, false);
        addOD(ClayiumMaterial.claySteel, ClayiumShape.ingot, TierPrefix.claySteel, false);
        addOD(ClayiumMaterial.claySteel, ClayiumShape.plate, TierPrefix.claySteel, false);
        add(ClayiumMaterial.claySteel, ClayiumShape.largePlate, TierPrefix.claySteel, false);

        addOD(ClayiumMaterial.clayium, ClayiumShape.dust, TierPrefix.clayium, false);
        addOD(ClayiumMaterial.clayium, ClayiumShape.ingot, TierPrefix.clayium, false);
        addOD(ClayiumMaterial.clayium, ClayiumShape.plate, TierPrefix.clayium, false);
        add(ClayiumMaterial.clayium, ClayiumShape.largePlate, TierPrefix.clayium, false);

        addOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.dust, TierPrefix.ultimate, false);
        addOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.ingot, TierPrefix.ultimate, false);
        addOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.plate, TierPrefix.ultimate, false);
        add(ClayiumMaterial.ultimateAlloy, ClayiumShape.largePlate, TierPrefix.ultimate, false);

        addOD(ClayiumMaterial.antimatter, ClayiumShape.dust, TierPrefix.antimatter, false);
        addOD(ClayiumMaterial.antimatter, ClayiumShape.gem, TierPrefix.antimatter, false);
        addOD(ClayiumMaterial.antimatter, ClayiumShape.plate, TierPrefix.antimatter, false);
        add(ClayiumMaterial.antimatter, ClayiumShape.largePlate, TierPrefix.antimatter, false);

        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.dust, TierPrefix.pureAntimatter, false);
        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.gem, TierPrefix.pureAntimatter, false);
        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.plate, TierPrefix.pureAntimatter, false);
        add(ClayiumMaterial.pureAntimatter, ClayiumShape.largePlate, TierPrefix.pureAntimatter, false);

        addOD(ClayiumMaterial.compressedPureAntimatter_1, ClayiumShape.gem, TierPrefix.pureAntimatter, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_2, ClayiumShape.gem, TierPrefix.pureAntimatter, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_3, ClayiumShape.gem, TierPrefix.pureAntimatter, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_4, ClayiumShape.gem, TierPrefix.OEC, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_5, ClayiumShape.gem, TierPrefix.OEC, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_6, ClayiumShape.gem, TierPrefix.OEC, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_7, ClayiumShape.gem, TierPrefix.OEC, false);

        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.dust, TierPrefix.OEC, false);
        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.plate, TierPrefix.OEC, false);
        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.largePlate, TierPrefix.OEC, false);

        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.dust, TierPrefix.OPA, false);
        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.gem, TierPrefix.OPA, false);
        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.plate, TierPrefix.OPA, false);
        add(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.largePlate, TierPrefix.OPA, false);

        for (Block block : ClayiumBlocks.materialBlock) {
            if (block instanceof MaterialBlock) {
                materialShapeMap.get(((MaterialBlock) block).getMaterial()).put(ClayiumShape.block, I(block));
                blocks.put(((MaterialBlock) block).getMaterial(), I(block));
                registerOre(((MaterialBlock) block).getMaterial(), ClayiumShape.block);
            }
        }

        for (EnumDyeColor dye : EnumDyeColor.values()) {
            registerOre(getOreName(ClayiumMaterial.silicone, ClayiumShape.block), I(ClayiumBlocks.siliconeColored.get(dye.ordinal())));
            registerOre(getOreName(ClayiumMaterial.silicone, ClayiumShape.block) + dye.getName(), I(ClayiumBlocks.siliconeColored.get(dye.ordinal())));
        }

        addOD(ClayiumMaterial.orgClay, ClayiumShape.dust, false);

        add(ClayiumMaterial.impureRedstone, ClayiumShape.dust, TierPrefix.unknown, false);
        add(ClayiumMaterial.impureGlowstone, ClayiumShape.dust, TierPrefix.unknown, false);

        add(ClayiumMaterial.impureUltimateAlloy, ClayiumShape.ingot, TierPrefix.clayium, false);

        for (ClayiumMaterial metal: ClayiumMaterial.metals) {
            addOD(metal, ClayiumShape.dust, false);
            addOD(metal, ClayiumShape.ingot, false);
        }

        for (ClayiumMaterial metal : ClayiumMaterial.impureMetals) {
            add(metal, ClayiumShape.dust, TierPrefix.unknown, false);
        }

        for (ClayiumMaterial metal : Arrays.asList(ClayiumMaterial.zincalminiumAlloy, ClayiumMaterial.zinconiumAlloy)) {
            addOD(metal, ClayiumShape.dust, TierPrefix.precision, false);
            addOD(metal, ClayiumShape.ingot, TierPrefix.precision, false);
        }

        for (ClayiumMaterial metal : Arrays.asList(ClayiumMaterial.AZ91DAlloy, ClayiumMaterial.ZK60AAlloy)) {
            addOD(metal, ClayiumShape.dust, TierPrefix.precision, false);
            addOD(metal, ClayiumShape.ingot, TierPrefix.precision, false);
            addOD(metal, ClayiumShape.plate, TierPrefix.precision, false);
            add(metal, ClayiumShape.largePlate, TierPrefix.precision, false);
        }

        addOD(ClayiumMaterial.carbon, ClayiumShape.dust, false);
        addOD(ClayiumMaterial.phosphorus, ClayiumShape.dust, false);
        addOD(ClayiumMaterial.sulfur, ClayiumShape.dust, false);

        addOD(ClayiumMaterial.lapis, ClayiumShape.dust, false);
        addOD(ClayiumMaterial.coal, ClayiumShape.dust, false);
        addOD(ClayiumMaterial.charcoal, ClayiumShape.dust, false);

        addOD(ClayiumMaterial.saltpeter, ClayiumShape.dust, false);

        for (ClayiumMaterial metal : ClayiumMaterial.ingotMetals) {
            addOD(metal, ClayiumShape.ingot, false);
        }

        if (ClayiumConfiguration.cfgHardcoreOsmium) {
            addOD(ClayiumMaterial.impureOsmium, ClayiumShape.ingot, false);
        }

        registerOre("itemSilicon", get(ClayiumMaterial.silicon, ClayiumShape.plate));
        registerOre("itemSalt", get(ClayiumMaterial.salt, ClayiumShape.dust));
        registerOre("condimentSalt", get(ClayiumMaterial.salt, ClayiumShape.dust));

        registerOre("ingotAluminium", get(ClayiumMaterial.aluminium, ClayiumShape.ingot));
        registerOre("dustAluminium", get(ClayiumMaterial.aluminium, ClayiumShape.dust));
        registerOre("plateAluminium", get(ClayiumMaterial.aluminium, ClayiumShape.plate));
        registerOre("blockAluminium", get(ClayiumMaterial.aluminium, ClayiumShape.block));
    }

    public static void supportRegisterMaterials() {
        materialShapeMap.get(ClayiumMaterial.clay).put(ClayiumShape.block, I(Blocks.CLAY));
        materialShapeMap.get(ClayiumMaterial.clay).put(ClayiumShape.ball, I(Items.CLAY_BALL));

        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.block, ClayiumBlocks.compressedClay.get(0, 1));
        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.ball, I(Items.CLAY_BALL));

        materialShapeMap.get(ClayiumMaterial.indClay).put(ClayiumShape.block, ClayiumBlocks.compressedClay.get(2, 1));

        materialShapeMap.get(ClayiumMaterial.advClay).put(ClayiumShape.block, ClayiumBlocks.compressedClay.get(3, 1));

        materialShapeMap.get(ClayiumMaterial.octupleEnergeticClay).put(ClayiumShape.block, ClayiumBlocks.compressedClay.get(12, 1));
    }

    public static final Map<Integer, ClayiumMaterial> materials = new HashMap<>();

    private static void add(ClayiumMaterial material, ClayiumShape shape, boolean hasUniqueIcon) {
        if (materialShapeMap.containsKey(material) && materialShapeMap.get(material).containsKey(shape)) {
            ClayiumCore.logger.error("The item already exists  [" + material.getName() + "] [" + shape.getName() + "]");
            return;
        }
        if (!materialShapeMap.containsKey(material)) {
            materialShapeMap.put(material, new EnumMap<>(ClayiumShape.class));
        }
        assert materialShapeMap.containsKey(material) && !materialShapeMap.get(material).containsKey(shape);

        materialShapeMap.get(material).putIfAbsent(shape, I(new ClayiumItem(material, shape)));
        putMap(material, shape);
    }

    private static void add(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier) {
        add(material, shape, tier, false);
    }

    private static void add(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier, boolean hasUniqueIcon) {
        if (materialShapeMap.containsKey(material) && materialShapeMap.get(material).containsKey(shape) && !materialShapeMap.get(material).get(shape).isEmpty()) {
            ClayiumCore.logger.error("The item already exists  [" + material.getName() + "] [" + shape.getName() + "]");
            return;
        }
        if (!materialShapeMap.containsKey(material)) {
            materialShapeMap.put(material, new EnumMap<>(ClayiumShape.class));
        }

        materialShapeMap.get(material).putIfAbsent(shape, I(new ClayiumShapedMaterial(material, shape, tier, !hasUniqueIcon)));
        if (material != ClayiumMaterial.clay && material != ClayiumMaterial.denseClay)
            putMap(material, shape);
    }

    private static void addOD(ClayiumMaterial material, ClayiumShape shape, boolean hasUniqueIcon) {
        addOD(material, shape, TierPrefix.unknown, hasUniqueIcon);
    }

    private static void addOD(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier) {
        addOD(material, shape, tier, false);
    }

    private static void addOD(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier, boolean hasUniqueIcon) {
        List<ItemStack> list = OreDictionary.getOres(shape.getName() + material.getODName());
        if (list.isEmpty()) {
            add(material, shape, tier, hasUniqueIcon);
            registerOre(material, shape);
            return;
        }

        ItemStack stack = list.get(0).copy();
        if (stack.getItem() instanceof ItemTiered)
            ((ItemTiered) stack.getItem()).setTier(tier);
        materialShapeMap.get(material).putIfAbsent(shape, stack);
    }

    public static ItemStack get(ClayiumMaterial material, ClayiumShape shape) {
        assert materialShapeMap.containsKey(material);
        assert materialShapeMap.get(material).containsKey(shape);
        return materialShapeMap.get(material).get(shape).copy();
    }

    public static ItemStack get(ClayiumMaterial material, ClayiumShape shape, int amount) {
        ItemStack stack = get(material, shape);
        stack.setCount(amount);
        return stack;
    }

    public static ItemStack get(TierPrefix tier, ClayiumShape shape) {
        return get(tier.getMaterial(), shape);
    }

    public static ItemStack get(TierPrefix tier, ClayiumShape shape, int amount) {
        return get(tier.getMaterial(), shape, amount);
    }

    public static ItemStack getOD(ClayiumMaterial material, ClayiumShape shape) {
        return getOD(material, shape, 1);
    }

    public static ItemStack getOD(ClayiumMaterial material, ClayiumShape shape, int amount) {
        ItemStack stack = ODHelper.getODStack(shape.getName() + material.getODName(), amount);
        if (stack.isEmpty()) return get(material, shape, amount);
        return stack;
    }

    private static void registerOre(ClayiumMaterial material, ClayiumShape shape) {
        registerOre(getOreName(material, shape), get(material, shape));
    }

    private static void registerOre(String key, ItemStack stack) {
//        OreDictionary.registerOre(key, stack);
        ODReserve.put(key, stack);
    }

    public static String getOreName(ClayiumMaterial material, ClayiumShape shape) {
        return shape.getName() + material.getODName();
    }

    public static ItemStack getODExist(String oreName, int stackSize) {
        List<ItemStack> oreList = OreDictionary.getOres(oreName);
        if (oreList != null && oreList.size() > 0) {
            ItemStack res = oreList.get(0).copy();
            res.setCount(stackSize);
            return res;
        } else {
            return null;
        }
    }

    public static ItemStack getODExist(String oreName) {
        return getODExist(oreName, 1);
    }

    public static ItemStack getODExist(ClayiumMaterial material, ClayiumShape shape, int stackSize) {
        return getODExist(getOreName(material, shape), stackSize);
    }

    public static ItemStack getODExist(ClayiumMaterial material, ClayiumShape shape) {
        return getODExist(material, shape, 1);
    }

    public static boolean existOD(String oreName) {
        return getODExist(oreName) != null;
    }

    public static boolean existOD(ClayiumMaterial material, ClayiumShape shape) {
        return getODExist(material, shape) != null;
    }

    private static ItemStack I(Item item) {
        return new ItemStack(item);
    }

    private static ItemStack I(Block block) {
        return new ItemStack(block);
    }

    private static void putMap(ClayiumMaterial material, ClayiumShape shape) {
        switch (shape) {
            case plate:
                plates.put(material, get(material, shape)); break;
            case largePlate:
                largePlates.put(material, get(material, shape)); break;
            case dust:
                dusts.put(material, get(material, shape)); break;
            case ingot:
                ingots.put(material, get(material, shape)); break;
            case gem:
                gems.put(material, get(material, shape)); break;
        }
    }
}
