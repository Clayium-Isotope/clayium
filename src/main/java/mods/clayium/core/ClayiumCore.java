package mods.clayium.core;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayCraftingTable.TileEntityClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable;
import mods.clayium.worldgen.ClayOreGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
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
        public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
            for (Item item : ClayiumItems.getItems()) {
                p_78018_1_.add(new ItemStack(item));
            }

            for (Item block : ClayiumBlocks.getItems()) {
                p_78018_1_.add(new ItemStack(block));
            }
        }
    };

    public static Item.ToolMaterial CLAY_STEEL = EnumHelper.addToolMaterial("CLAY_STEEL", 2, 10000, 3f, 3f, 10);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        ClayiumBlocks.initBlocks();
        ClayiumItems.initItems();

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        GameRegistry.addSmelting(ClayiumItems.rawClayRollingPin, new ItemStack(ClayiumItems.clayRollingPin), 1F);
        GameRegistry.addSmelting(ClayiumItems.rawClaySlicer, new ItemStack(ClayiumItems.claySlicer), 1F);
        GameRegistry.addSmelting(ClayiumItems.rawClaySpatula, new ItemStack(ClayiumItems.claySpatula), 1F);

        GameRegistry.registerTileEntity(TileEntityClayWorkTable.class, ClayiumBlocks.clayWorkTable.getRegistryName());
        GameRegistry.registerTileEntity(TileEntityClayCraftingTable.class, ClayiumBlocks.clayCraftingTable.getRegistryName());

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
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ClayiumBlocks.getItems().toArray(new Item[0]));
        event.getRegistry().registerAll(ClayiumItems.getItems().toArray(new Item[0]));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        for (Item block : ClayiumBlocks.getItems()) {
            ModelLoader.setCustomModelResourceLocation(block, 0,
                    new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }

        for (Item item : ClayiumItems.getItems()) {
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}
