package mods.clayium.core;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.entity.EntityClayBall;
import mods.clayium.entity.EntityTeleportBall;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.machine.CAMachine.ResonanceHandler;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.worldgen.ClayOreGenerator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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

    @SidedProxy(clientSide = "mods.clayium.core.ClayiumClientProxy",
                serverSide = "mods.clayium.core.ClayiumCommonProxy")
    public static IClayiumProxy proxy;

    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("clayium:channel");

    public static Logger logger = LogManager.getLogger("clayium");

    public static final CreativeTabs tabClayium = new CreativeTabs("clayium") {
        @Override
        public ItemStack createIcon() {
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

    @CapabilityInject(ResonanceHandler.class)
    public static Capability<ResonanceHandler> RESONANCE_CAPABILITY = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        ClayiumBlocks.registerBlocks();
        ClayiumItems.initItems();

        ClayiumMaterials.registerMaterials();
        ClayiumMachines.registerMachines();

        EntityRegistry.registerModEntity(new ResourceLocation(ClayiumCore.ModId, "clay_bullet"), EntityClayBall.class, "clay_bullet", ClayiumConfiguration.EntityIdClayBall, ClayiumCore.instance(), 128, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(ClayiumCore.ModId, "teleport_bullet"), EntityTeleportBall.class, "teleport_bullet", ClayiumConfiguration.EntityIdTeleportBall, ClayiumCore.instance(), 128, 1, true);

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
}
