package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.CompressedClay;
import mods.clayium.block.common.MaterialBlock;
import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.*;
import mods.clayium.util.ODHelper;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    @SideOnly(Side.CLIENT)
    public static void requestTint(ItemColors event) {
        for (ItemStack stack : getMaterials()) {
            if (stack.getItem() instanceof ClayiumShapedMaterial && ((ClayiumShapedMaterial) stack.getItem()).useGeneralIcon())
                ((ClayiumShapedMaterial) stack.getItem()).registerMaterialColor(event);
        }
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

        materialShapeMap.get(ClayiumMaterial.clay).put(ClayiumShape.block, I(((CompressedClay) ClayiumBlocks.compressedClay.get(0)).getItemBlock()));
        materialShapeMap.get(ClayiumMaterial.clay).put(ClayiumShape.ball, I(Items.CLAY_BALL));
        for (ClayiumShape shape : ClayiumShape.clayPartShapes) {
            add(ClayiumMaterial.clay, shape, 1, true);
        }
        clayParts = materialShapeMap.get(ClayiumMaterial.clay);

        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.block, I(((CompressedClay) ClayiumBlocks.compressedClay.get(1)).getItemBlock()));
        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.ball, I(Items.CLAY_BALL));
        materialShapeMap.get(ClayiumMaterial.denseClay).put(ClayiumShape.largeBall, ItemStack.EMPTY);
        for (ClayiumShape shape : ClayiumShape.clayPartShapes) {
            add(ClayiumMaterial.denseClay, shape, 2, true);
        }
        denseClayParts = materialShapeMap.get(ClayiumMaterial.denseClay);

        materialShapeMap.get(ClayiumMaterial.indClay).put(ClayiumShape.block, I(((CompressedClay) ClayiumBlocks.compressedClay.get(3)).getItemBlock()));
        add(ClayiumMaterial.indClay, ClayiumShape.plate, 3, true);
        add(ClayiumMaterial.indClay, ClayiumShape.largePlate, 3, true);

        materialShapeMap.get(ClayiumMaterial.advClay).put(ClayiumShape.block, I(((CompressedClay) ClayiumBlocks.compressedClay.get(4)).getItemBlock()));
        add(ClayiumMaterial.advClay, ClayiumShape.plate, 4, true);
        add(ClayiumMaterial.advClay, ClayiumShape.largePlate, 4, true);

//        add(ClayiumMaterial.clay, ClayiumShape.dust, 1, true);
//        add(ClayiumMaterial.denseClay, ClayiumShape.dust, 2, true);
        addOD(ClayiumMaterial.indClay, ClayiumShape.dust, 3, true);
        addOD(ClayiumMaterial.advClay, ClayiumShape.dust, 4, true);
        addOD(ClayiumMaterial.engClay, ClayiumShape.dust, 3, true);
        addOD(ClayiumMaterial.excClay, ClayiumShape.dust, 7, true);

        addOD(ClayiumMaterial.salt, ClayiumShape.dust, 4, true);
        add(ClayiumMaterial.calClay, ClayiumShape.dust, 4, true);
        add(ClayiumMaterial.calciumChloride, ClayiumShape.dust, 4, true);
        add(ClayiumMaterial.sodiumCarbonate, ClayiumShape.dust, 4, true);
        addOD(ClayiumMaterial.quartz, ClayiumShape.dust, 4, true);

        addOD(ClayiumMaterial.impureSilicon, ClayiumShape.dust, 5, false);
        addOD(ClayiumMaterial.impureSilicon, ClayiumShape.ingot, 5, true);
        addOD(ClayiumMaterial.impureSilicon, ClayiumShape.plate, 5, true);
        add(ClayiumMaterial.impureSilicon, ClayiumShape.largePlate, 5, true);

        addOD(ClayiumMaterial.silicone, ClayiumShape.dust, 5, false);
        addOD(ClayiumMaterial.silicone, ClayiumShape.ingot, 5, false);
        addOD(ClayiumMaterial.silicone, ClayiumShape.plate, 5, false);
        add(ClayiumMaterial.silicone, ClayiumShape.largePlate, 5, false);

        addOD(ClayiumMaterial.silicon, ClayiumShape.dust, 5, false);
        addOD(ClayiumMaterial.silicon, ClayiumShape.ingot, 5, false);
        addOD(ClayiumMaterial.silicon, ClayiumShape.plate, 5, false);
        add(ClayiumMaterial.silicon, ClayiumShape.largePlate, 5, false);

        addOD(ClayiumMaterial.aluminium, ClayiumShape.dust, 6, false);
        addOD(ClayiumMaterial.aluminium, ClayiumShape.ingot, 6, false);
        addOD(ClayiumMaterial.aluminium, ClayiumShape.plate, 6, false);
        add(ClayiumMaterial.aluminium, ClayiumShape.largePlate, 6, false);

        if (ClayiumConfiguration.cfgHardcoreAluminium) {
            add(ClayiumMaterial.impureAluminium, ClayiumShape.dust, 6, false);
            add(ClayiumMaterial.impureAluminium, ClayiumShape.ingot, 6, false);
            add(ClayiumMaterial.impureAluminium, ClayiumShape.plate, 6, false);
            add(ClayiumMaterial.impureAluminium, ClayiumShape.largePlate, 6, false);
        }
        else
            add(ClayiumMaterial.impureAluminium, ClayiumShape.dust, false);

        addOD(ClayiumMaterial.claySteel, ClayiumShape.dust, 7, false);
        addOD(ClayiumMaterial.claySteel, ClayiumShape.ingot, 7, false);
        addOD(ClayiumMaterial.claySteel, ClayiumShape.plate, 7, false);
        add(ClayiumMaterial.claySteel, ClayiumShape.largePlate, 7, false);

        addOD(ClayiumMaterial.clayium, ClayiumShape.dust, 8, false);
        addOD(ClayiumMaterial.clayium, ClayiumShape.ingot, 8, false);
        addOD(ClayiumMaterial.clayium, ClayiumShape.plate, 8, false);
        add(ClayiumMaterial.clayium, ClayiumShape.largePlate, 8, false);

        addOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.dust, 9, false);
        addOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.ingot, 9, false);
        addOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.plate, 9, false);
        add(ClayiumMaterial.ultimateAlloy, ClayiumShape.largePlate, 9, false);

        addOD(ClayiumMaterial.antimatter, ClayiumShape.dust, 10, false);
        addOD(ClayiumMaterial.antimatter, ClayiumShape.gem, 10, false);
        addOD(ClayiumMaterial.antimatter, ClayiumShape.plate, 10, false);
        add(ClayiumMaterial.antimatter, ClayiumShape.largePlate, 10, false);

        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.dust, 11, false);
        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.gem, 11, false);
        addOD(ClayiumMaterial.pureAntimatter, ClayiumShape.plate, 11, false);
        add(ClayiumMaterial.pureAntimatter, ClayiumShape.largePlate, 11, false);

        addOD(ClayiumMaterial.compressedPureAntimatter_1, ClayiumShape.gem, 11, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_2, ClayiumShape.gem, 11, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_3, ClayiumShape.gem, 11, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_4, ClayiumShape.gem, 12, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_5, ClayiumShape.gem, 12, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_6, ClayiumShape.gem, 12, false);
        addOD(ClayiumMaterial.compressedPureAntimatter_7, ClayiumShape.gem, 12, false);

        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.dust, 12, false);
        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.plate, 12, false);
        add(ClayiumMaterial.octupleEnergeticClay, ClayiumShape.largePlate, 12, false);
        materialShapeMap.get(ClayiumMaterial.octupleEnergeticClay).put(ClayiumShape.block, I(new ItemBlockCompressedClay((CompressedClay) ClayiumBlocks.compressedClay.get(12))));

        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.dust, 13, false);
        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.gem, 13, false);
        addOD(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.plate, 13, false);
        add(ClayiumMaterial.octuplePureAntimatter, ClayiumShape.largePlate, 13, false);

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

        add(ClayiumMaterial.impureRedstone, ClayiumShape.dust, false);
        add(ClayiumMaterial.impureGlowstone, ClayiumShape.dust, false);

        add(ClayiumMaterial.impureUltimateAlloy, ClayiumShape.ingot, 8, false);

        for (ClayiumMaterial metal: ClayiumMaterial.metals) {
            addOD(metal, ClayiumShape.dust, false);
            addOD(metal, ClayiumShape.ingot, false);
        }

        for (ClayiumMaterial metal : ClayiumMaterial.impureMetals) {
            add(metal, ClayiumShape.dust, false);
        }

        for (ClayiumMaterial metal : Arrays.asList(ClayiumMaterial.zincalminiumAlloy, ClayiumMaterial.zinconiumAlloy)) {
            addOD(metal, ClayiumShape.dust, 6, false);
            addOD(metal, ClayiumShape.ingot, 6, false);
        }

        for (ClayiumMaterial metal : Arrays.asList(ClayiumMaterial.AZ91DAlloy, ClayiumMaterial.ZK60AAlloy)) {
            addOD(metal, ClayiumShape.dust, 6, false);
            addOD(metal, ClayiumShape.ingot, 6, false);
            addOD(metal, ClayiumShape.plate, 6, false);
            add(metal, ClayiumShape.largePlate, 6, false);
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

    private static void add(ClayiumMaterial material, ClayiumShape shape, int tier) {
        add(material, shape, tier, false);
    }

    private static void add(ClayiumMaterial material, ClayiumShape shape, int tier, boolean hasUniqueIcon) {
        if (materialShapeMap.containsKey(material) && materialShapeMap.get(material).containsKey(shape)) {
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
        addOD(material, shape, -1, hasUniqueIcon);
    }

    private static void addOD(ClayiumMaterial material, ClayiumShape shape, int tier) {
        addOD(material, shape, tier, false);
    }

    private static void addOD(ClayiumMaterial material, ClayiumShape shape, int tier, boolean hasUniqueIcon) {
        List<ItemStack> list = OreDictionary.getOres(shape.getName() + material.getODName());
        if (list.isEmpty()) {
            add(material, shape, tier, hasUniqueIcon);
            registerOre(material, shape);
            return;
        }

        ItemStack stack = list.get(0).copy();
        if (stack.getItem() instanceof ItemTiered && tier >= 0)
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

    private static void registerOre(ClayiumMaterial material, ClayiumShape shape) {
        registerOre(getOreName(material, shape), get(material, shape));
    }

    private static void registerOre(String key, ItemStack stack) {
//        OreDictionary.registerOre(key, stack);
        ODReserve.put(key, stack);
    }

    private static String getOreName(ClayiumMaterial material, ClayiumShape shape) {
        return shape.getName() + material.getODName();
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
