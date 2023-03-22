package mods.clayium.core;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.common.IItemBlockHolder;
import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.entity.EntityClayBall;
import mods.clayium.entity.EntityTeleportBall;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumShapedMaterial;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.LaserReflector.TEISRLaserReflector;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.worldgen.ClayOreGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ClayiumCore.ModId, version = ClayiumCore.Version, name = ClayiumCore.ModName)
public class ClayiumCore {
    public static final String ModId = "clayium";
    public static final String ModName = "Clayium";
    public static final String Version = "0.0.0";

    @Instance(ClayiumCore.ModId)
    private static ClayiumCore instance;

    public static ClayiumCore instance() {
        if (instance == null)
            instance = new ClayiumCore();
        return instance;
    }

    @SidedProxy(clientSide = "mods.clayium.core.ClayiumClientProxy", serverSide = "mods.clayium.core.ClayiumCommonProxy")
    public static IClayiumProxy proxy;

    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("clayium:channel");

    public static Logger logger = LogManager.getLogger("clayium");

    public static final CreativeTabs tabClayium = new CreativeTabs("clayium") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.CLAY_BALL);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> tab) {
            for (Item item : ClayiumItems.getItems()) {
                tab.add(new ItemStack(item));
            }

            for (Block block : ClayiumBlocks.getBlocks()) {
                tab.add(new ItemStack(block));
            }

//            for (Item item : ClayiumMaterials.getItems()) {
//                tab.add(new ItemStack(item));
//            }
            tab.addAll(ClayiumMaterials.getMaterials());

            for (Block block : ClayiumMachines.getBlocks()) {
                tab.add(new ItemStack(block));
            }
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        ClayiumBlocks.registerBlocks();
        ClayiumItems.initItems();

        ClayiumMaterials.registerMaterials();
        ClayiumMachines.registerMachines();

        EntityRegistry.registerModEntity(new ResourceLocation(ClayiumCore.ModId, "clay_bullet"), EntityClayBall.class, "clay_bullet", ClayiumConfiguration.EntityIdClayBall, ClayiumCore.instance(), 128, 1, true);
        RenderingRegistry.registerEntityRenderingHandler(EntityClayBall.class, manager -> new RenderSnowball<>(manager, Items.CLAY_BALL, Minecraft.getMinecraft().getRenderItem()));
        EntityRegistry.registerModEntity(new ResourceLocation(ClayiumCore.ModId, "teleport_bullet"), EntityTeleportBall.class, "teleport_bullet", ClayiumConfiguration.EntityIdTeleportBall, ClayiumCore.instance(), 128, 1, true);
        RenderingRegistry.registerEntityRenderingHandler(EntityTeleportBall.class, manager -> new RenderSnowball<>(manager, Items.ENDER_PEARL, Minecraft.getMinecraft().getRenderItem()));

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        ClayiumMaterials.supportRegisterMaterials();
        ClayiumRecipes.registerRecipes();

        proxy.registerTileEntity();

//        OreDictionary.registerOre("circuitBasic", ClayiumItems.advancedCircuit);
//        OreDictionary.registerOre("circuitAdvanced", ClayiumItems.precisionCircuit);
//        OreDictionary.registerOre("circuitElite", ClayiumItems.integratedCircuit);
//        OreDictionary.registerOre("circuitUltimate", ClayiumItems.clayCore);
//
//        ClayiumMaterials.registerOres();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance(), new GuiHandler());
        GameRegistry.registerWorldGenerator(new ClayOreGenerator(), 0);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        proxy.serverLoad(event);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ClayiumBlocks.getBlocks().toArray(new Block[0]));
        event.getRegistry().registerAll(ClayiumMachines.getBlocks().toArray(new Block[0]));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        for (Block block : ClayiumBlocks.getBlocks()) {
            assert block.getRegistryName() != null;
            Item item = block instanceof IItemBlockHolder ? ((IItemBlockHolder) block).getItemBlock() : new ItemBlock(block);
            event.getRegistry().register(item.setRegistryName(block.getRegistryName()));
        }

        for (Block block : ClayiumMachines.getBlocks()) {
            assert block.getRegistryName() != null;

            // TODO not completed
            Item value = new ItemBlockTiered(block).setRegistryName(block.getRegistryName());
            if (block == ClayiumMachines.laserReflector)
                value.setTileEntityItemStackRenderer(new TEISRLaserReflector());

            event.getRegistry().register(value);
        }

        event.getRegistry().registerAll(ClayiumItems.getItems().toArray(new Item[0]));
        event.getRegistry().registerAll(ClayiumMaterials.getItems().toArray(new Item[0]));
    }

    // フルパスで書かなきゃいけないものかしら。
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        for (Block block : ClayiumBlocks.getBlocks()) {
            assert block.getRegistryName() != null;
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(block.getRegistryName(), "inventory"));
        }

        for (Block block : ClayiumMachines.getBlocks()) {
            assert block.getRegistryName() != null;
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(ClayiumCore.ModId + ":machine/" + block.getRegistryName().getResourcePath(), "inventory"));
            // Custom blockstate mapping
            if (block != ClayiumMachines.clayWorkTable && block != ClayiumMachines.clayCraftingTable && block != ClayiumMachines.quartzCrucible) {
                net.minecraftforge.client.model.ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                    @Override
                    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                        return new net.minecraft.client.renderer.block.model.ModelResourceLocation(ClayiumCore.ModId + ":empty");
                    }
                });
            }
        }

        for (Item item : ClayiumItems.getItems()) {
            assert item.getRegistryName() != null;
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

        for (ItemStack stack : ClayiumMaterials.getMaterials()) {
            assert stack.getItem().getRegistryName() != null;

            if (stack.getItem() instanceof ClayiumShapedMaterial) {
                if (((ClayiumShapedMaterial) stack.getItem()).useGeneralIcon())
                    net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                            new net.minecraft.client.renderer.block.model.ModelResourceLocation(ClayiumCore.ModId + ":material/" + ((ClayiumShapedMaterial) stack.getItem()).getTempFile(), "inventory"));
                else
                    net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                            new net.minecraft.client.renderer.block.model.ModelResourceLocation(ClayiumCore.ModId + ":material/" + stack.getItem().getRegistryName().getResourcePath(), "inventory"));
            }
        }

        for (ItemStack stack : ClayiumMaterials.clayParts.values()) {
            assert stack.getItem().getRegistryName() != null;
            if (stack.getItem() instanceof ClayiumShapedMaterial)
                net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                        new net.minecraft.client.renderer.block.model.ModelResourceLocation(ClayiumCore.ModId + ":part/" + stack.getItem().getRegistryName().getResourcePath(), "inventory"));
        }

        for (ItemStack stack : ClayiumMaterials.denseClayParts.values()) {
            assert stack.getItem().getRegistryName() != null;
            if (stack.getItem() instanceof ClayiumShapedMaterial)
                net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                        new net.minecraft.client.renderer.block.model.ModelResourceLocation(ClayiumCore.ModId + ":part/" + stack.getItem().getRegistryName().getResourcePath(), "inventory"));
        }
    }

    @SuppressWarnings("unchecked")
    public static <N extends Number> N multiplyProgressionRate(N number) {
        if (number instanceof Double) {
            return (N) (Double) (number.doubleValue() * ClayiumConfiguration.cfgProgressionRate);
        }
        if (number instanceof Float) {
            return (N) (Float) ((float) (number.floatValue() * ClayiumConfiguration.cfgProgressionRate));
        }
        if (number instanceof Integer) {
            Integer r = (Integer) ((int) ((double) number.intValue() * ClayiumConfiguration.cfgProgressionRate));
            return r != 0 ? (N) r : (N) (Integer) Integer.compare(number.intValue(), 0);
        }
        if (number instanceof Long) {
            Long r = (Long) ((long) ((double) number.longValue() * ClayiumConfiguration.cfgProgressionRate));
            return r != 0 ? (N) r : (N) (Integer) Long.compare(number.longValue(), 0);
        }

        throw new UnsupportedOperationException("unknown class: " + number.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <N extends Number> N divideByProgressionRate(N number) {
        if (number instanceof Double) {
            return (N) (Double) (number.doubleValue() / ClayiumConfiguration.cfgProgressionRate);
        }
        if (number instanceof Float) {
            return (N) (Float) ((float) (number.floatValue() / ClayiumConfiguration.cfgProgressionRate));
        }
        if (number instanceof Integer) {
            Integer r = (Integer) ((int) ((double) number.intValue() / ClayiumConfiguration.cfgProgressionRate));
            return r != 0 ? (N) r : (N) (Integer) Integer.compare(number.intValue(), 0);
        }
        if (number instanceof Long) {
            Long r = (Long) ((long) ((double) number.longValue() / ClayiumConfiguration.cfgProgressionRate));
            return r != 0 ? (N) r : (N) (Integer) Long.compare(number.longValue(), 0);
        }

        throw new UnsupportedOperationException("unknown class: " + number.getClass());
    }

    public static int multiplyProgressionRateStackSize(int a) {
        return Math.min(multiplyProgressionRate(a), 64);
    }

    public static int divideByProgressionRateStackSize(int a) {
        return Math.min(divideByProgressionRate(a), 64);
    }
}
