package mods.clayium.core;

import mods.clayium.client.color.ShapedMaterial;
import mods.clayium.client.color.SiliconeColor;
import mods.clayium.client.render.*;
import mods.clayium.entity.EntityClayBall;
import mods.clayium.entity.EntityTeleportBall;
import mods.clayium.machine.AreaCollector.TileEntityAreaCollector;
import mods.clayium.machine.AreaMiner.TileEntityAreaWorker;
import mods.clayium.machine.AutoClayCondenser.TileEntityAutoClayCondenser;
import mods.clayium.machine.AutoCrafter.TileEntityAutoCrafter;
import mods.clayium.machine.AutoTrader.TileEntityAutoTrader;
import mods.clayium.machine.CACollector.TileEntityCACollector;
import mods.clayium.machine.CACondenser.TileEntityCACondenser;
import mods.clayium.machine.CAInjector.TileEntityCAInjector;
import mods.clayium.machine.CAReactor.TileEntityCAReactor;
import mods.clayium.machine.ChemicalMetalSeparator.TileEntityChemicalMetalSeparator;
import mods.clayium.machine.ClayAssembler.TileEntityClayAssembler;
import mods.clayium.machine.ClayBlastFurnace.TileEntityClayBlastFurnace;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.machine.ClayCentrifuge.TileEntityClayCentrifuge;
import mods.clayium.machine.ClayChemicalReactor.TileEntityClayChemicalReactor;
import mods.clayium.machine.ClayCraftingTable.TileEntityClayCraftingTable;
import mods.clayium.machine.ClayDistributor.TileEntityClayDistributor;
import mods.clayium.machine.ClayEnergyLaser.TileEntityClayEnergyLaser;
import mods.clayium.machine.ClayFabricator.TileEntityClayFabricator;
import mods.clayium.machine.ClayMarker.TileEntityClayMarker;
import mods.clayium.machine.ClayReactor.TileEntityClayReactor;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.CobblestoneGenerator.TileEntityCobblestoneGenerator;
import mods.clayium.machine.CreativeEnergySource.TileEntityCreativeEnergySource;
import mods.clayium.machine.Interface.ClayInterface.TileEntityClayInterface;
import mods.clayium.machine.Interface.ClayLaserInterface.TileEntityClayLaserInterface;
import mods.clayium.machine.Interface.RedstoneInterface.TileEntityRedstoneInterface;
import mods.clayium.machine.LaserReflector.TileEntityLaserReflector;
import mods.clayium.machine.MultitrackBuffer.TileEntityMultitrackBuffer;
import mods.clayium.machine.QuartzCrucible.TileEntityQuartzCrucible;
import mods.clayium.machine.SaltExtractor.TileEntitySaltExtractor;
import mods.clayium.machine.SolarClayFabricator.TileEntitySolarClayFabricator;
import mods.clayium.machine.StorageContainer.TileEntityStorageContainer;
import mods.clayium.machine.VacuumContainer.TileEntityVacuumContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClayiumClientProxy implements IClayiumProxy {
    float mode1velocity = 0.7F;
    float mode2acceleration = 0.9F;
    float mode2division = 1.1F;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        OBJLoader.INSTANCE.addDomain("clayium");

        RenderingRegistry.registerEntityRenderingHandler(EntityClayBall.class, manager -> new RenderSnowball<>(manager, Items.CLAY_BALL, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTeleportBall.class, manager -> new RenderSnowball<>(manager, Items.ENDER_PEARL, Minecraft.getMinecraft().getRenderItem()));
    }

    @Override
    public void init(FMLInitializationEvent event) {
        registerColors();
        MinecraftForge.EVENT_BUS.register(SiliconeColor.class);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {}

    @Override
    public void serverLoad(FMLServerStartingEvent event) {}

    public EnumFacing getHittingSide(EntityPlayer player) {
//        return Minecraft.getMinecraft().getRenderViewEntity().rayTrace(9999.0D, 0.0F).sideHit;
        RayTraceResult rtr = net.minecraftforge.common.ForgeHooks.rayTraceEyes(player, 9999.0D);
        return rtr == null ? EnumFacing.NORTH : rtr.sideHit;
    }

    public EntityPlayer getClientPlayer() {
        return (Minecraft.getMinecraft()).player;
    }

    @SideOnly(Side.CLIENT)
    public void registerColors() {
        ShapedMaterial.requestTint(Minecraft.getMinecraft().getItemColors());

        SiliconeColor.registerColorHandler(Minecraft.getMinecraft().getItemColors(), Minecraft.getMinecraft().getBlockColors());
    }

    public void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityClayWorkTable.class, new ResourceLocation(ClayiumCore.ModId, "clay_work_table"));
        GameRegistry.registerTileEntity(TileEntityClayCraftingTable.class, new ResourceLocation(ClayiumCore.ModId, "clay_crafting_table"));

        ClientRegistry.registerTileEntity(TileEntityClayBuffer.class, "clayium:buffer", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityClayiumMachine.class, "clayium:machine", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityCobblestoneGenerator.class, "clayium:cobblestone_generator", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityClayAssembler.class, "clayium:assembler", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityMultitrackBuffer.class, "clayium:multitrack_buffer", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntitySaltExtractor.class, "clayium:salt_extractor", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityClayEnergyLaser.class, "clayium:laser", new TileEntitySpecialRenderer<TileEntityClayEnergyLaser>() {
            @Override
            public void render(TileEntityClayEnergyLaser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
                TESRClayContainer.render(te, x, y, z, partialTicks, destroyStage, alpha, this.rendererDispatcher);
                RenderClayLaser.render(te, x, y, z);
            }

            @Override
            public boolean isGlobalRenderer(TileEntityClayEnergyLaser te) {
                return true;
            }
        });
        ClientRegistry.registerTileEntity(TileEntityLaserReflector.class, "clayium:laser_reflector", new TESRLaserReflector());
//        Item.getItemFromBlock(ClayiumMachines.laserReflector).setTileEntityItemStackRenderer(new TEISRLaserReflector());
        ClientRegistry.registerTileEntity(TileEntityClayChemicalReactor.class, "clayium:chemical_reactor", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityAutoClayCondenser.class, "clayium:auto_clay_condenser", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntitySolarClayFabricator.class, "clayium:solar_clay_fabricator", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityClayFabricator.class, "clayium:clay_fabricator", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityChemicalMetalSeparator.class, "clayium:chemical_metal_separator", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityClayInterface.class, "clayium:clay_interface",
                new TileEntitySpecialRenderer<TileEntityClayInterface>() {
                    @Override
                    public void render(TileEntityClayInterface te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
                        TESRClayContainer.render(te, x, y, z, partialTicks, destroyStage, alpha, this.rendererDispatcher);
                        TESRInterface.render(te, x, y, z, partialTicks, destroyStage, alpha, this.rendererDispatcher);
                }
        }
        );
        ClientRegistry.registerTileEntity(TileEntityClayBlastFurnace.class, "clayium:blast_furnace", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityRedstoneInterface.class, "clayium:redstone_interface", TESRInterface.instance);
        ClientRegistry.registerTileEntity(TileEntityClayLaserInterface.class, "clayium:laser_interface", TESRInterface.instance);

        ClientRegistry.registerTileEntity(TileEntityCreativeEnergySource.class, "clayium:creative_energy", TESRClayContainer.instance);
        GameRegistry.registerTileEntity(TileEntityQuartzCrucible.class, new ResourceLocation(ClayiumCore.ModId, "quartz_crucible"));
        ClientRegistry.registerTileEntity(TileEntityClayDistributor.class, "clayium:distributor", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityAutoCrafter.class, "clayium:auto_crafter", TESRClayContainer.instance);
//        GameRegistry.registerTileEntity(TileEntityAutoCrafter.class, new ResourceLocation(ClayiumCore.ModId, "auto_crafter"));
        ClientRegistry.registerTileEntity(TileEntityClayReactor.class, "clayium:reactor", TESRClayContainer.instance);

        ClientRegistry.registerTileEntity(TileEntityCACondenser.class, "clayium:ca_condenser", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityCAInjector.class, "clayium:ca_injector", TESRClayContainer.instance);

        ClientRegistry.registerTileEntity(TileEntityClayCentrifuge.class, "clayium:centrifuge", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityStorageContainer.class, "clayium:storage_container", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityVacuumContainer.class, "clayium:vacuum_container", TESRClayContainer.instance);

        ClientRegistry.registerTileEntity(TileEntityClayMarker.class, "clayium:clay_marker", TESRClayMarker.instance);
//        GameRegistry.registerTileEntity(TileEntityClayMarker.class, new ResourceLocation(ClayiumCore.ModId, "clay_marker"));
        ClientRegistry.registerTileEntity(TileEntityAreaWorker.class, "clayium:area_miner",
                new TileEntitySpecialRenderer<>() {
                    @Override
                    public void render(TileEntityAreaWorker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
                        TESRClayContainer.render(te, x, y, z, partialTicks, destroyStage, alpha, this.rendererDispatcher);
                        TESRClayMarker.render(te, x, y, z, partialTicks, destroyStage, alpha, this.rendererDispatcher);
                    }
                });
        ClientRegistry.registerTileEntity(TileEntityAreaCollector.class, "clayium:area_collector", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityAutoTrader.class, "clayium:auto_trader", TESRClayContainer.instance);

        ClientRegistry.registerTileEntity(TileEntityCACollector.class, "clayium:ca_collector", TESRClayContainer.instance);
        ClientRegistry.registerTileEntity(TileEntityCAReactor.class, "clayium:ca_reactor", TESRClayContainer.instance);
    }

    /**
     * Copied from the original clayium to do the same performance
     */
    public void updateFlightStatus(int mode) {
        EntityPlayerSP player = (EntityPlayerSP) this.getClientPlayer();
        if (mode == 0 || !player.capabilities.isFlying) return;

        MovementInput mi = player.movementInput;
        GameSettings settings = Minecraft.getMinecraft().gameSettings;

        double sin = Math.sin(Math.toRadians(player.rotationYaw));
        double cos = Math.cos(Math.toRadians(player.rotationYaw));
        mi.moveForward = (float) (player.motionZ * cos - player.motionX * sin);
        mi.moveStrafe = (float) (player.motionZ * sin + player.motionX * cos);

        if (mi.sneak) {
            if (mode >= 2) {
                player.motionY -= this.mode2acceleration;
                player.motionY /= this.mode2division;
            } else {
                player.motionY = -this.mode1velocity;
            }
        }

        if (mi.jump) {
            if (mode >= 2) {
                player.motionY += this.mode2acceleration;
                player.motionY /= this.mode2division;
            } else {
                player.motionY = this.mode1velocity;
            }
        }

        if (mi.jump == mi.sneak) {
            player.motionY = 0.0D;
        }

        if (settings.keyBindForward.isKeyDown()) {
            if (mode >= 2) {
                mi.moveForward += this.mode2acceleration;
                mi.moveForward /= this.mode2division;
            } else {
                mi.moveForward = this.mode1velocity;
            }
        }

        if (settings.keyBindBack.isKeyDown()) {
            if (mode >= 2) {
                mi.moveForward -= this.mode2acceleration;
                mi.moveForward /= this.mode2division;
            } else {
                mi.moveForward = -this.mode1velocity;
            }
        }

        if (!settings.keyBindForward.isKeyDown() && !settings.keyBindBack.isKeyDown()) {
            mi.moveForward = 0.0F;
        }

        if (settings.keyBindLeft.isKeyDown()) {
            if (mode >= 2) {
                mi.moveStrafe += this.mode2acceleration;
                mi.moveStrafe /= this.mode2division;
            } else {
                mi.moveStrafe = this.mode1velocity;
            }
        }

        if (settings.keyBindRight.isKeyDown()) {
            if (mode >= 2) {
                mi.moveStrafe -= this.mode2acceleration;
                mi.moveStrafe /= this.mode2division;
            } else {
                mi.moveStrafe = -this.mode1velocity;
            }
        }

        if (!settings.keyBindLeft.isKeyDown() && !settings.keyBindRight.isKeyDown()) {
            mi.moveStrafe = 0.0F;
        }

        player.motionX = mi.moveStrafe * cos - mi.moveForward * sin;
        player.motionZ = mi.moveForward * cos + mi.moveStrafe * sin;
    }

    /**
     * Copied from the original clayium to do the same performance
     */
    public void overclockPlayer(int delay) {
        int i;
        try {
            i = ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, Minecraft.getMinecraft().playerController, "field_78781_i");
//            i = this.blockHitDelay.getInt(Minecraft.getMinecraft().playerController);
            if (i >= delay) {
                ObfuscationReflectionHelper.setPrivateValue(PlayerControllerMP.class, Minecraft.getMinecraft().playerController,  delay, "field_78781_i");
//                this.blockHitDelay.setInt(Minecraft.getMinecraft().playerController, delay);
            }
        } catch (IllegalArgumentException var7) {
            ClayiumCore.logger.catching(var7);
        }

        try {
            i = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_71467_ac");
//            i = this.rightClickDelayTimer.getInt(Minecraft.getMinecraft());
            if (i >= delay + 1) {
                ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(),  delay + 1, "field_71467_ac");
//                this.rightClickDelayTimer.setInt(Minecraft.getMinecraft(), delay + 1);
            }
        } catch (IllegalArgumentException var5) {
            ClayiumCore.logger.catching(var5);
        }
    }
}
