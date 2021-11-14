package mods.clayium.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import mods.clayium.block.CBlocks;
import mods.clayium.block.tile.TileAreaActivator;
import mods.clayium.block.tile.TileAreaCollector;
import mods.clayium.block.tile.TileAreaMiner;
import mods.clayium.block.tile.TileCAReactor;
import mods.clayium.block.tile.TileClayContainerInterface;
import mods.clayium.block.tile.TileClayEnergyLaser;
import mods.clayium.block.tile.TileClayLaserInterface;
import mods.clayium.block.tile.TileClayMarker;
import mods.clayium.block.tile.TileClayOpenPitMarker;
import mods.clayium.block.tile.TileLaserReflector;
import mods.clayium.block.tile.TileMetalChest;
import mods.clayium.block.tile.TileRedstoneInterface;
import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.block.tile.TileVacuumContainer;
import mods.clayium.entity.EntityClayBall;
import mods.clayium.entity.EntityTeleportBall;
import mods.clayium.item.CItems;
import mods.clayium.item.CMaterial;
import mods.clayium.item.CMaterials;
import mods.clayium.item.CShape;
import mods.clayium.item.ClaySteelPickaxe;
import mods.clayium.item.ClaySteelPickaxeClient;
import mods.clayium.item.ItemCapsule;
import mods.clayium.item.ItemDamaged;
import mods.clayium.item.gadget.GadgetLongArm;
import mods.clayium.misc.RenderFluidTab;
import mods.clayium.misc.TileEntityFluidTabRenderer;
import mods.clayium.misc.TileFluidTab;
import mods.clayium.network.ClaySteelPickaxePacket;
import mods.clayium.plugin.nei.LoadNEIConfig;
import mods.clayium.render.block.RenderClayContainer;
import mods.clayium.render.block.RenderLaserReflector;
import mods.clayium.render.block.RenderMetalChest;
import mods.clayium.render.block.RenderPANCable;
import mods.clayium.render.block.RenderQuartzCrucible;
import mods.clayium.render.item.ItemDamagedRenderer;
import mods.clayium.render.tile.AreaMachineRenderer;
import mods.clayium.render.tile.CAReactorRenderer;
import mods.clayium.render.tile.ClayLaserRenderer;
import mods.clayium.render.tile.ClayMarkerRenderer;
import mods.clayium.render.tile.InterfaceRenderer;
import mods.clayium.render.tile.MetalChestRenderer;
import mods.clayium.render.tile.StorageContainerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClayiumClientProxy extends ClayiumCommonProxy {
    public void registerTileEntity() {
        if (!ClayiumCore.cfgUtilityMode) {
            RenderingRegistry.registerBlockHandler(new RenderFluidTab());
            RenderingRegistry.registerBlockHandler(new RenderClayContainer());
            RenderingRegistry.registerBlockHandler(new RenderQuartzCrucible());
            RenderingRegistry.registerBlockHandler(new RenderLaserReflector());
            RenderingRegistry.registerBlockHandler(new RenderPANCable());
            RenderingRegistry.registerBlockHandler(new RenderMetalChest());

            ClientRegistry.bindTileEntitySpecialRenderer(TileFluidTab.class, new TileEntityFluidTabRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileClayEnergyLaser.class, new ClayLaserRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileLaserReflector.class, new ClayLaserRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileStorageContainer.class, new StorageContainerRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileVacuumContainer.class, new StorageContainerRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileClayMarker.class, new ClayMarkerRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileClayOpenPitMarker.class, new ClayMarkerRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileAreaMiner.class, new AreaMachineRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileAreaCollector.class, new AreaMachineRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileAreaActivator.class, new AreaMachineRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileCAReactor.class, new CAReactorRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileMetalChest.class, MetalChestRenderer.INSTANCE);
            ClientRegistry.bindTileEntitySpecialRenderer(TileClayContainerInterface.class, new InterfaceRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileClayLaserInterface.class, new InterfaceRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileRedstoneInterface.class, new InterfaceRenderer());
        } else {
            RenderingRegistry.registerBlockHandler(new RenderClayContainer());
        }
    }


    public void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(EntityClayBall.class, new RenderSnowball(Items.clay_ball));
        RenderingRegistry.registerEntityRenderingHandler(EntityTeleportBall.class, new RenderSnowball(Items.ender_pearl));
        ItemDamagedRenderer itemDamagedRenderer = new ItemDamagedRenderer();
        for (Iterator<Map.Entry<CMaterial, ItemDamaged>> iterator1 = CMaterials.materialMap.entrySet().iterator(); iterator1.hasNext(); ) {
            Map.Entry<CMaterial, ItemDamaged> entry = iterator1.next();
            MinecraftForgeClient.registerItemRenderer(entry.getValue(), itemDamagedRenderer);
        }
        for (Iterator<Map.Entry<CShape, ItemDamaged>> iterator = CMaterials.shapeMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<CShape, ItemDamaged> entry = iterator.next();
            MinecraftForgeClient.registerItemRenderer(entry.getValue(), itemDamagedRenderer);
        }
        if (ClayiumCore.cfgEnableFluidCapsule) {
            for (ItemCapsule item : CItems.itemsCapsule) {
                MinecraftForgeClient.registerItemRenderer(item, itemDamagedRenderer);
            }
        }
    }

    public int getRenderID() {
        return RenderingRegistry.getNextAvailableRenderId();
    }


    public World getClientWorld() {
        return (FMLClientHandler.instance().getClient()).theWorld;
    }


    public void LoadNEI() {
        if (ClayiumCore.IntegrationID.NEI.loaded()) {

            try {

                LoadNEIConfig.load();
            } catch (Exception e) {

                e.printStackTrace(System.err);
            }
        }
    }


    public ClaySteelPickaxe newClaySteelPickaxe() {
        return new ClaySteelPickaxeClient();
    }


    public int getHittingSide(EntityPlayer player) {
        if (!(player.getEntityWorld()).isRemote || player != (Minecraft.getMinecraft()).thePlayer) {
            return super.getHittingSide(player);
        }

        return sideHit();
    }


    public void updateHittingSide(EntityPlayer player) {
        if (player.worldObj.isRemote && getClientPlayer() == player)
            ClayiumCore.packetDispatcher.sendToServer(new ClaySteelPickaxePacket(sideHit()));
    }

    private int sideHit() {
        return ((Minecraft.getMinecraft()).renderViewEntity.rayTrace(9999.0D, 0.0F)).sideHit;
    }


    public EntityPlayer getClientPlayer() {
        return (Minecraft.getMinecraft()).thePlayer;
    }

    public boolean renderAsPipingMode() {
        EntityPlayer player = getClientPlayer();
        ItemStack item;
        return (player != null && (item = player.getCurrentEquippedItem()) != null && item.getItem() == CItems.itemClayPipingTools);
    }

    Field blockHitDelay = null;
    Field rightClickDelayTimer = null;

    public void clientPlayerTick(EntityPlayer player) {
        int j = 0;
        boolean flag = false;
        int s = 5;
        for (int i = 0; i < 9; i++) {
            ItemStack item = player.inventory.mainInventory[i];
            if (item != null && item.getItem() instanceof ItemBlock && ((ItemBlock) item
                    .getItem()).field_150939_a == CBlocks.blockOverclocker) {
                j = flag ? 1 : 0;
                switch (item.getItemDamage()) {
                    case 10:
                        s = Math.min(s, 3);
                        break;
                    case 11:
                        s = Math.min(s, 2);
                        break;
                    case 12:
                        s = Math.min(s, 1);
                        break;
                    case 13:
                        s = Math.min(s, 0);
                        break;
                }

            }
        }
        if (j != 0) {
            overclockPlayer(s);
        }
    }


    public void overclockPlayer(int delay) {
        if (this.blockHitDelay == null) {
            Class<?> classPlayerController = null;
            try {
                classPlayerController = Class.forName("net.minecraft.client.multiplayer.PlayerControllerMP");
            } catch (ClassNotFoundException e2) {
                try {
                    classPlayerController = Class.forName("net.minecraft.bje");
                } catch (ClassNotFoundException e) {
                    ClayiumCore.logger.catching(e);
                }
            }
            if (classPlayerController != null) {
                try {
                    this.blockHitDelay = classPlayerController.getDeclaredField("field_78781_i");
                } catch (NoSuchFieldException e) {
                    ClayiumCore.logger.info(e.getMessage());
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
                if (this.blockHitDelay == null) {
                    try {
                        this.blockHitDelay = classPlayerController.getDeclaredField("blockHitDelay");
                    } catch (NoSuchFieldException e) {
                        ClayiumCore.logger.info(e.getMessage());
                    } catch (SecurityException e) {
                        ClayiumCore.logger.catching(e);
                    }
                }
                if (this.blockHitDelay == null) {
                    try {
                        this.blockHitDelay = classPlayerController.getDeclaredField("i");
                    } catch (NoSuchFieldException e) {
                        ClayiumCore.logger.info(e.getMessage());
                    } catch (SecurityException e) {
                        ClayiumCore.logger.catching(e);
                    }
                }
                if (this.blockHitDelay != null) {
                    this.blockHitDelay.setAccessible(true);
                }
            }
        }

        if (this.rightClickDelayTimer == null) {
            Class<?> classMinecraft = null;
            try {
                classMinecraft = Class.forName("net.minecraft.client.Minecraft");
            } catch (ClassNotFoundException e2) {
                try {
                    classMinecraft = Class.forName("net.minecraft.bao");
                } catch (ClassNotFoundException e) {
                    ClayiumCore.logger.catching(e);
                }
            }
            if (classMinecraft != null) {
                try {
                    this.rightClickDelayTimer = classMinecraft.getDeclaredField("field_71467_ac");
                } catch (NoSuchFieldException e) {
                    ClayiumCore.logger.info(e.getMessage());
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
                if (this.rightClickDelayTimer == null) {
                    try {
                        this.rightClickDelayTimer = classMinecraft.getDeclaredField("rightClickDelayTimer");
                    } catch (NoSuchFieldException e) {
                        ClayiumCore.logger.info(e.getMessage());
                    } catch (SecurityException e) {
                        ClayiumCore.logger.catching(e);
                    }
                }
                if (this.rightClickDelayTimer == null) {
                    try {
                        this.rightClickDelayTimer = classMinecraft.getDeclaredField("ad");
                    } catch (NoSuchFieldException e) {
                        ClayiumCore.logger.info(e.getMessage());
                    } catch (SecurityException e) {
                        ClayiumCore.logger.catching(e);
                    }
                }
                if (this.rightClickDelayTimer != null) {
                    this.rightClickDelayTimer.setAccessible(true);
                }
            }
        }

        if (this.blockHitDelay != null) {
            try {
                int i = this.blockHitDelay.getInt((Minecraft.getMinecraft()).playerController);
                if (i >= delay)
                    this.blockHitDelay.setInt((Minecraft.getMinecraft()).playerController, delay);
            } catch (IllegalArgumentException e) {
                ClayiumCore.logger.catching(e);
            } catch (IllegalAccessException e) {
                ClayiumCore.logger.catching(e);
            }
        }
        if (this.rightClickDelayTimer != null) {
            try {
                int i = this.rightClickDelayTimer.getInt(Minecraft.getMinecraft());
                if (i >= delay + 1)
                    this.rightClickDelayTimer.setInt(Minecraft.getMinecraft(), delay + 1);
            } catch (IllegalArgumentException e) {
                ClayiumCore.logger.catching(e);
            } catch (IllegalAccessException e) {
                ClayiumCore.logger.catching(e);
            }
        }
    }

    float mode1velocity = 0.7F;
    float mode2acceleration = 0.9F;
    float mode2division = 1.1F;

    public void updateFlightStatus(int mode) {
        EntityClientPlayerMP player = (EntityClientPlayerMP) getClientPlayer();
        if (mode != -1 &&
                mode >= 1 && player.capabilities.isFlying) {
            MovementInput mi = player.movementInput;
            GameSettings settings = (Minecraft.getMinecraft()).gameSettings;

            float sin = MathHelper.sin(player.rotationYaw * 3.1415927F / 180.0F);
            float cos = MathHelper.cos(player.rotationYaw * 3.1415927F / 180.0F);

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

            if (settings.keyBindForward.getIsKeyPressed()) {
                if (mode >= 2) {
                    mi.moveForward += this.mode2acceleration;
                    mi.moveForward /= this.mode2division;
                } else {
                    mi.moveForward = this.mode1velocity;
                }
            }

            if (settings.keyBindBack.getIsKeyPressed()) {
                if (mode >= 2) {
                    mi.moveForward -= this.mode2acceleration;
                    mi.moveForward /= this.mode2division;
                } else {
                    mi.moveForward = -this.mode1velocity;
                }
            }

            if (!settings.keyBindForward.getIsKeyPressed() && !settings.keyBindBack.getIsKeyPressed()) {
                mi.moveForward = 0.0F;
            }

            if (settings.keyBindLeft.getIsKeyPressed()) {
                if (mode >= 2) {
                    mi.moveStrafe += this.mode2acceleration;
                    mi.moveStrafe /= this.mode2division;
                } else {
                    mi.moveStrafe = this.mode1velocity;
                }
            }

            if (settings.keyBindRight.getIsKeyPressed()) {
                if (mode >= 2) {
                    mi.moveStrafe -= this.mode2acceleration;
                    mi.moveStrafe /= this.mode2division;
                } else {
                    mi.moveStrafe = -this.mode1velocity;
                }
            }

            if (!settings.keyBindLeft.getIsKeyPressed() && !settings.keyBindRight.getIsKeyPressed()) {
                mi.moveStrafe = 0.0F;
            }

            player.motionX = (mi.moveStrafe * cos - mi.moveForward * sin);
            player.motionZ = (mi.moveForward * cos + mi.moveStrafe * sin);
        }
    }


    public float hookBlockReachDistance(float distance) {
        return GadgetLongArm.hookBlockReachDistance(distance, getClientPlayer());
    }


    public boolean isCreative(EntityPlayer player) {
        if (player == getClientPlayer()) {
            return (Minecraft.getMinecraft()).playerController.isInCreativeMode();
        }
        return super.isCreative(player);
    }
}
