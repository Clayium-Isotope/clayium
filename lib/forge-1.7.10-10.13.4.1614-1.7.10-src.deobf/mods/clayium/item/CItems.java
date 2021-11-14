package mods.clayium.item;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.clayium.block.tile.IRayTracer;
import mods.clayium.core.ClayiumCore;
import mods.clayium.entity.RayTraceMemory;
import mods.clayium.gui.CreativeTab;
import mods.clayium.item.filter.ItemFilterBlacklist;
import mods.clayium.item.filter.ItemFilterBlockMetadata;
import mods.clayium.item.filter.ItemFilterDuplicator;
import mods.clayium.item.filter.ItemFilterItemDamage;
import mods.clayium.item.filter.ItemFilterItemName;
import mods.clayium.item.filter.ItemFilterModID;
import mods.clayium.item.filter.ItemFilterOreDict;
import mods.clayium.item.filter.ItemFilterSpecial;
import mods.clayium.item.filter.ItemFilterUniqueID;
import mods.clayium.item.filter.ItemFilterUnlocalizedName;
import mods.clayium.item.filter.ItemFilterWhitelist;
import mods.clayium.item.gadget.GadgetAutoEat;
import mods.clayium.item.gadget.GadgetFlight;
import mods.clayium.item.gadget.GadgetHealth;
import mods.clayium.item.gadget.GadgetLongArm;
import mods.clayium.item.gadget.GadgetOverclocker;
import mods.clayium.item.gadget.GadgetRepeatedlyAttack;
import mods.clayium.util.UtilAdvancedTools;
import mods.clayium.util.UtilBuilder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class CItems {
    public static ItemDamaged itemMisc;
    public static Item itemLargeClayBall;
    public static ItemDamaged itemRawClayCraftingTools;
    public static Item itemClayRollingPin;
    public static Item itemClaySlicer;
    public static Item itemClaySpatula;
    public static Item itemClayWrench;
    public static ItemDamaged itemClayPipingTools;
    public static ItemDamaged itemCompressedClayShard;
    public static Item itemClayShovel;
    public static Item itemClayPickaxe;
    public static Item itemClaySteelPickaxe;
    public static Item itemClaySteelShovel;
    public static Item itemSynchronizer;
    public static Item itemDirectionMemory;
    public static Item itemFilterDuplicator;
    public static Item itemFilterWhitelist;
    public static Item itemFilterBlacklist;
    public static Item itemFilterFuzzy;
    public static Item itemFilterOreDict;
    public static Item itemFilterItemName;
    public static Item itemFilterUnlocalizedName;
    public static Item itemFilterUniqueId;
    public static Item itemFilterModId;
    public static Item itemFilterItemDamage;
    public static Item itemFilterBlockMetadata;
    public static ItemFilterSpecial itemFilterBlockHarvestable;
    public static Item[] itemsClayShooter;
    public static Item itemInstantTeleporter;
    public static ItemCapsule[] itemsCapsule;
    public static Item itemGadgetHolder;
    public static ItemGadget itemGadget;

    public static void registerItems() {
        itemRawClayCraftingTools = (ItemDamaged) (new ItemDamaged()).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemRawClayCraftingTools").setMaxStackSize(64);
        itemRawClayCraftingTools.addItemList("RollingPin", 0, "rawclayrollingpin");
        itemRawClayCraftingTools.addItemList("Slicer", 1, "rawclayslicer");
        itemRawClayCraftingTools.addItemList("Spatula", 2, "rawclayspatula");
        GameRegistry.registerItem(itemRawClayCraftingTools, "itemRawClayCraftingTools");


        itemClayRollingPin = (new ItemCraftingTools(new ItemStack(Items.clay_ball, 4))).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemClayRollingPin").setTextureName("clayium:clayrollingpin").setMaxDamage(ClayiumCore.multiplyProgressionRateI(60));
        GameRegistry.registerItem(itemClayRollingPin, "itemClayRollingPin");

        itemClaySlicer = (new ItemCraftingTools(new ItemStack(Items.clay_ball, 3))).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemClaySlicer").setTextureName("clayium:clayslicer").setMaxDamage(ClayiumCore.multiplyProgressionRateI(60));
        GameRegistry.registerItem(itemClaySlicer, "itemClaySlicer");

        itemClaySpatula = (new ItemCraftingTools(new ItemStack(Items.clay_ball, 2))).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemClaySpatula").setTextureName("clayium:clayspatula").setMaxDamage(ClayiumCore.multiplyProgressionRateI(36));
        GameRegistry.registerItem(itemClaySpatula, "itemClaySpatula");

        itemCompressedClayShard = (ItemDamaged) (new ItemDamaged()).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemCompressedClayShard").setMaxStackSize(64);
        itemCompressedClayShard.addItemList("1", 1, "compressedclay-shard-1");
        itemCompressedClayShard.addItemList("2", 2, "compressedclay-shard-2");
        itemCompressedClayShard.addItemList("3", 3, "compressedclay-shard-3");
        GameRegistry.registerItem(itemCompressedClayShard, "itemCompressedClayShard");


        itemClayPipingTools = ((ItemDamaged) (new ItemDamaged() {
            public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
                return true;
            }

            public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
                return player.isSneaking() && UtilBuilder.rotateBlockByWrench(world, x, y, z, side);
            }
        }).setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemClayPipingTools")
                .setMaxStackSize(1).setFull3D())
                .addItemList("IO", 0, "iotool")
                .addItemList("Piping", 1, "pipingtool")
                .addItemList("Memory", 2, "memorycard");
        GameRegistry.registerItem(itemClayPipingTools, "itemClayPipingTools");

        itemClayShovel = new ClayShovel();
        GameRegistry.registerItem(itemClayShovel, "itemClayShovel");
        itemClayPickaxe = new ClayPickaxe();
        GameRegistry.registerItem(itemClayPickaxe, "itemClayPickaxe");

        itemClayWrench = (new ItemTiered() {
            public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) { return UtilBuilder.rotateBlockByWrench(world, x, y, z, side); }
        }).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemClayWrench").setTextureName("clayium:claywrench").setMaxStackSize(1).setFull3D();
        GameRegistry.registerItem(itemClayWrench, "itemClayWrench");

        itemClaySteelPickaxe = ClayiumCore.proxy.newClaySteelPickaxe();
        GameRegistry.registerItem(itemClaySteelPickaxe, "itemClaySteelPickaxe");
        itemClaySteelShovel = new ClaySteelShovel();
        GameRegistry.registerItem(itemClaySteelShovel, "itemClaySteelShovel,");
        MinecraftForge.EVENT_BUS.register(UtilAdvancedTools.INSTANCE);


        itemFilterDuplicator = (new ItemFilterDuplicator()).setBaseTier(7)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterDuplicator")
                .setTextureName("clayium:filterduplicator");
        GameRegistry.registerItem(itemFilterDuplicator, "itemFilterDuplicator");

        itemFilterWhitelist = (new ItemFilterWhitelist()).setBaseTier(5)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterWhitelist")
                .setTextureName("clayium:filterwhitelist");
        GameRegistry.registerItem(itemFilterWhitelist, "itemFilterWhitelist");

        itemFilterBlacklist = (new ItemFilterBlacklist()).setBaseTier(5)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterBlacklist")
                .setTextureName("clayium:filterblacklist");
        GameRegistry.registerItem(itemFilterBlacklist, "itemFilterBlacklist");

        itemFilterFuzzy = (new ItemFilterWhitelist(true)).setBaseTier(7)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterFuzzy")
                .setTextureName("clayium:filterfuzzy");
        GameRegistry.registerItem(itemFilterFuzzy, "itemFilterFuzzy");

        itemFilterOreDict = (new ItemFilterOreDict()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterOreDict")
                .setTextureName("clayium:filterod");
        GameRegistry.registerItem(itemFilterOreDict, "itemFilterOreDict");

        itemFilterItemName = (new ItemFilterItemName()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterItemName")
                .setTextureName("clayium:filtername");
        GameRegistry.registerItem(itemFilterItemName, "itemFilterItemName");

        itemFilterUnlocalizedName = (new ItemFilterUnlocalizedName()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterUnlocalizedName")
                .setTextureName("clayium:filteruname");
        GameRegistry.registerItem(itemFilterUnlocalizedName, "itemFilterUnlocalizedName");

        itemFilterUniqueId = (new ItemFilterUniqueID()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterUniqueId")
                .setTextureName("clayium:filterid");
        GameRegistry.registerItem(itemFilterUniqueId, "itemFilterUniqueId");

        itemFilterModId = (new ItemFilterModID()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterModId")
                .setTextureName("clayium:filtermodid");
        GameRegistry.registerItem(itemFilterModId, "itemFilterModId");

        itemFilterItemDamage = (new ItemFilterItemDamage()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterItemDamage")
                .setTextureName("clayium:filtermeta");
        GameRegistry.registerItem(itemFilterItemDamage, "itemFilterMetadata");

        itemFilterBlockMetadata = (new ItemFilterBlockMetadata()).setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterBlockMetadata")
                .setTextureName("clayium:filterblockmeta");
        GameRegistry.registerItem(itemFilterBlockMetadata, "itemFilterBlockMetadata");

        itemFilterBlockHarvestable = new ItemFilterSpecial();
        itemFilterBlockHarvestable.setBaseTier(6)
                .setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemFilterBlockHarvestable")
                .setTextureName("clayium:filterblockharvestable");
        itemFilterBlockHarvestable.addSpecialFilter(new FilterBlockHarvestable());
        GameRegistry.registerItem(itemFilterBlockHarvestable, "itemFilterBlockHarvestable");


        itemsClayShooter = new Item[4];
        itemsClayShooter[0] = (new ClayShooter(10000, "itemClayShooter0", "clayshooter0", 2, 1.2F, 25.0F, 4, 6, 0)).setBaseTier(6);
        GameRegistry.registerItem(itemsClayShooter[0], "itemClayShooter0");

        itemsClayShooter[1] = (new ClayShooter(1000, "itemClayShooter1", "clayshooter1", 4, 4.5F, 3.0F, 7, 12, 0)).setBaseTier(6);
        GameRegistry.registerItem(itemsClayShooter[1], "itemClayShooter1");

        itemsClayShooter[2] = (new ClayShooter(8000, "itemClayShooter2", "clayshooter2", 2, 15.0F, 0.0F, 25, 8, 20)).setBaseTier(6);
        GameRegistry.registerItem(itemsClayShooter[2], "itemClayShooter2");

        itemsClayShooter[3] = (new ClayShooter(2500, "itemClayShooter3", "clayshooter3", 3, 30.0F, 0.0F, 100, 8, 40)).setBaseTier(6);
        GameRegistry.registerItem(itemsClayShooter[3], "itemClayShooter3");


        itemInstantTeleporter = (new InstantTeleporter(2500, "itemInstantTeleporter", "instantteleporter", 3, 30.0F, 0.0F, 100, 8, 40)).setBaseTier(11);
        GameRegistry.registerItem(itemInstantTeleporter, "itemInstantTeleporter");

        itemSynchronizer = (new ItemTiered()).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemSynchronizer").setMaxStackSize(1).setTextureName("clayium:synchronizer");
        GameRegistry.registerItem(itemSynchronizer, "itemSynchronizer");

        itemDirectionMemory = (new ItemTiered() {
            public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
                TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
                NBTTagCompound tag = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();

                if (tag.hasKey("RayTraceMemory", 10)) {
                    RayTraceMemory rayTraceMemory = RayTraceMemory.getFromNBT((NBTTagCompound) tag.getTag("RayTraceMemory"));
                    if (te instanceof IRayTracer && ((IRayTracer) te).acceptRayTraceMemory(rayTraceMemory)) {
                        ((IRayTracer) te).setRayTraceMemory(rayTraceMemory);
                        if (!world.isRemote) player.addChatMessage(new ChatComponentText("Applied direction memory."));
                        return !world.isRemote;
                    }
                }

                double eyeHeight = world.isRemote ? (player.getEyeHeight() - player.getDefaultEyeHeight()) : player.getEyeHeight();
                RayTraceMemory memory = new RayTraceMemory(Vec3.createVectorHelper(player.posX, player.posY + eyeHeight, player.posZ), Vec3.createVectorHelper((x + posX), (y + posY), (z + posZ)), side);
                NBTTagCompound memoryTag = new NBTTagCompound();
                memory.writeToNBT(memoryTag);
                tag.setTag("RayTraceMemory", memoryTag);
                itemstack.setTagCompound(tag);

                if (!world.isRemote)
                    player.addChatMessage(new ChatComponentText("Saved your direction to memory."));

                return !world.isRemote;
            }
        }).setCreativeTab(ClayiumCore.creativeTabClayium)
                .setUnlocalizedName("itemDirectionMemory")
                .setMaxStackSize(1)
                .setTextureName("clayium:directionmemory");
        GameRegistry.registerItem(itemDirectionMemory, "itemDirectionMemory");


        itemMisc = (ItemDamaged) (new ItemDamaged()).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("item").setMaxStackSize(64);
        itemMisc.addItemList("ClayCircuit", 0, "claycircuit", 2);

        itemMisc.addItemList("ClayCircuitBoard", 1024, "claycircuitboard", 2);
        itemMisc.addItemList("SimpleCircuit", 1, "simplecircuit", 3);

        itemMisc.addItemList("CEEBoard", 1025, "ceeboard", 3);
        itemMisc.addItemList("CEECircuit", 1026, "ceecircuit", 3);
        itemMisc.addItemList("CEE", 1027, "cee", 3);
        itemMisc.addItemList("LaserParts", 1028, "laserparts", 7);
        itemMisc.addItemList("AntimatterSeed", 1029, "antimatterseed", 9);
        itemMisc.addItemList("SynchronousParts", 1030, "synchronousparts", 9);
        itemMisc.addItemList("TeleportationParts", 1031, "teleportationparts", 11);
        itemMisc.addItemList("Manipulator1", 1032, "manipulator", 6);
        itemMisc.addItemList("Manipulator2", 1033, "manipulator2", 8);
        itemMisc.addItemList("Manipulator3", 1034, "manipulator3", 12);
        itemMisc.addItemList("BasicCircuit", 2, "basiccircuit", 4);
        itemMisc.addItemList("AdvancedCircuit", 3, "advancedcircuit", 5);
        itemMisc.addItemList("PrecisionCircuit", 4, "precisioncircuit", 6);
        itemMisc.addItemList("IntegratedCircuit", 5, "integratedcircuit", 7);
        itemMisc.addItemList("ClayCore", 6, "claycore", 8);
        itemMisc.addItemList("ClayBrain", 7, "claybrain", 9);
        itemMisc.addItemList("ClaySpirit", 8, "clayspirit", 10);
        itemMisc.addItemList("ClaySoul", 9, "claysoul", 11);
        itemMisc.addItemList("ClayAnima", 10, "clayanima", 12);
        itemMisc.addItemList("ClayPsyche", 11, "claypsyche", 13);

        GameRegistry.registerItem(itemMisc, "itemMisc");

        OreDictionary.registerOre("circuitBasic", itemMisc.get("AdvancedCircuit"));
        OreDictionary.registerOre("circuitAdvanced", itemMisc.get("PrecisionCircuit"));
        OreDictionary.registerOre("circuitElite", itemMisc.get("IntegratedCircuit"));
        OreDictionary.registerOre("circuitUltimate", itemMisc.get("ClayCore"));

        itemsCapsule = new ItemCapsule[5];
        if (ClayiumCore.cfgFluidCapsuleCreativeTabMode != 0 && ClayiumCore.creativeTabClayiumCapsule == null) {
            ClayiumCore.creativeTabClayiumCapsule = new CreativeTab("ClayiumCapsule");
        }
        itemsCapsule[0] = registerCapsule(1000, "Capsule", "capsule1000");
        itemsCapsule[1] = registerCapsule(125, "Capsule0125", "capsule0125");
        itemsCapsule[2] = registerCapsule(25, "Capsule0025", "capsule0025");
        itemsCapsule[3] = registerCapsule(5, "Capsule0005", "capsule0005");
        itemsCapsule[4] = registerCapsule(1, "Capsule0001", "capsule0001");
        if (ClayiumCore.creativeTabClayiumCapsule != null) {
            ((CreativeTab) ClayiumCore.creativeTabClayiumCapsule).setTabIconItem(itemsCapsule[0]);
        }

        itemGadgetHolder = (new ItemGadgetHolder()).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemGadgetHolder").setMaxStackSize(1).setTextureName("clayium:gadgetholder");
        GameRegistry.registerItem(itemGadgetHolder, "itemGadgetHolder");

        itemGadget = (ItemGadget) (new ItemGadget()).setCreativeTab(ClayiumCore.creativeTabClayium).setUnlocalizedName("itemGadget").setMaxStackSize(1);

        itemGadget.addItemList("AntimatterOverclock", 0, "gadget_antimatteroverclocker", 10);
        itemGadget.addItemList("PureAntimatterOverclock", 1, "gadget_pureantimatteroverclocker", 11);
        itemGadget.addItemList("OECOverclock", 2, "gadget_oecoverclocker", 12);
        itemGadget.addItemList("OPAOverclock", 3, "gadget_opaoverclocker", 13);
        itemGadget.addItemList("Flight0", 16, "gadget_flight0", 12);
        itemGadget.addItemList("Flight1", 17, "gadget_flight1", 13);
        itemGadget.addItemList("Flight2", 18, "gadget_flight2", 13);
        itemGadget.addItemList("Health0", 32, "gadget_health0", 6);
        itemGadget.addItemList("Health1", 33, "gadget_health1", 10);
        itemGadget.addItemList("Health2", 34, "gadget_health2", 12);
        itemGadget.addItemList("AutoEat0", 48, "gadget_autoeat0", 7);
        itemGadget.addItemList("AutoEat1", 49, "gadget_autoeat1", 7);
        itemGadget.getClass();
        IItemCallback callback = new ItemGadget.ItemCallbackItemFilterGui(itemGadget);
        itemGadget.setCallback("AutoEat0", callback);
        itemGadget.setCallback("AutoEat1", callback);
        itemGadget.addItemList("RepeatedlyAttack", 64, "gadget_repattack", 10);
        itemGadget.addItemList("LongArm0", 80, "gadget_longarm0", 6);
        itemGadget.addItemList("LongArm1", 81, "gadget_longarm1", 8);
        itemGadget.addItemList("LongArm2", 82, "gadget_longarm2", 12);
        itemGadget.addItemList("Blank", 1024, "gadget_blank", 6);
        GameRegistry.registerItem(itemGadget, "itemGadget");

        ItemGadgetHolder.addGadget(new GadgetOverclocker());
        ItemGadgetHolder.addGadget(new GadgetFlight());
        ItemGadgetHolder.addGadget(new GadgetHealth());
        ItemGadgetHolder.addGadget(new GadgetAutoEat());
        ItemGadgetHolder.addGadget(new GadgetRepeatedlyAttack());
        ItemGadgetHolder.addGadget(new GadgetLongArm());
    }

    public static ItemCapsule registerCapsule(int capacity, String nameSuffix, String iconName) {
        ItemCapsule ret = new ItemCapsule(capacity, iconName);
        if (ClayiumCore.cfgFluidCapsuleCreativeTabMode != 0) {
            ret.setCreativeTab(ClayiumCore.creativeTabClayiumCapsule);
        } else {
            ret.setCreativeTab(ClayiumCore.creativeTabClayium);
        }
        ret.setUnlocalizedName("item" + nameSuffix).setMaxStackSize(64);
        if (ClayiumCore.cfgEnableFluidCapsule)
            GameRegistry.registerItem(ret, "item" + nameSuffix);
        return ret;
    }
}
