package mods.clayium.util;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.UUID;
import javax.crypto.SecretKey;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayShooter;
import mods.clayium.item.ItemGadgetHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class UtilPlayer {
    public static UtilPlayer INSTANCE = new UtilPlayer();

    @SubscribeEvent
    public void PlayerTickEventSubscriber(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.phase == TickEvent.Phase.START) {
            UtilAdvancedTools.INSTANCE.playerTick(player);

            UtilKeyInput.INSTANCE.playerTick(player);


            clayShooterTick(player);

            if (player == getClientPlayer()) {
                ClayiumCore.proxy.clientPlayerTick(player);
            }
            ItemGadgetHolder.updateGadget(event.player, event.player.worldObj.isRemote);
            ItemGadgetHolder.clearGadgetList(event.player, event.player.worldObj.isRemote);
        }
    }

    public void clayShooterTick(EntityPlayer player) {
        ItemStack itemstack = player.getCurrentEquippedItem();
        if (itemstack != null && itemstack.getItem() instanceof ClayShooter) {
            ClayShooter item = (ClayShooter) itemstack.getItem();
            int cooldownTime = item.getCooldownTime(itemstack, player);
            float shootingRate = item.getShootingRate(itemstack, player);
            float cooldown = ((Float) getPlayerInstantDataWithSafety(player, "ClayShooterCoolDown", new Float(0.0F))).floatValue();
            if (UtilKeyInput.getKeyLength(player, PlayerKey.KeyType.USE_ITEM) >= 0) {
                if (UtilKeyInput.getKeyLength(player, PlayerKey.KeyType.USE_ITEM) == 1 &&
                        cooldown <= -(cooldownTime)) {
                    cooldown = 1.0F;
                }

                if (cooldown >= 0.0F) {
                    while (cooldown >= 1.0F) {
                        cooldown--;
                        if (!item.isCharger(itemstack, player) && !player.worldObj.isRemote)
                            item.shootBullet(itemstack, player);
                    }
                    cooldown += shootingRate;
                }

            } else if (cooldown >= 0.0F) {
                cooldown = -1.0F;
            } else {
                cooldown--;
            }

            setPlayerInstantData(player, "ClayShooterCoolDown", new Float(cooldown));
        }
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void FOVUpdateEventSubscriber(FOVUpdateEvent event) {
        ItemStack stack = event.entity.getItemInUse();
        if (event.entity.isUsingItem() && stack.getItem() instanceof ClayShooter) {

            ClayShooter item = (ClayShooter) stack.getItem();
            if (item.isCharger(stack, event.entity)) {
                int time = item.getChargeTime(stack, event.entity);
                int i = event.entity.getItemInUseDuration();
                if (i > time) i = time;
                float f1 = i / time;

                if (f1 > 1.0F) {

                    f1 = 1.0F;
                } else {

                    f1 *= f1;
                }


                float t = item.getInitialVelocity(stack, event.entity) * item.getLifespan(stack, event.entity);
                float u = i / time, k = t * u - 10.0F;
                if (k < 0.0F) k = 0.0F;
                k = k + 3600.0F / (k + 60.0F) - 60.0F;
                float m = 2.5F / (k + 2.5F);
                event.newfov = event.fov * m + (1.0F - m) * 0.1F;
            }
        }
    }


    public static HashMap<EntityPlayer, HashMap<String, Object>> playerInstantDataMap = new HashMap<EntityPlayer, HashMap<String, Object>>();
    public static HashMap<EntityPlayer, HashMap<String, Object>> playerInstantDataMapClient = new HashMap<EntityPlayer, HashMap<String, Object>>();

    public static HashMap<EntityPlayer, HashMap<String, Object>> getPlayerInstantDataMap(boolean isRemote) {
        return isRemote ? playerInstantDataMapClient : playerInstantDataMap;
    }


    public static Object getPlayerInstantDataWithSafety(EntityPlayer player, String key, Object def) {
        Object res = getPlayerInstantData(player, key);
        if (res == null) {
            setPlayerInstantData(player, key, def);
            res = def;
        }
        return res;
    }

    public static Object getPlayerInstantData(EntityPlayer player, String key) {
        return !dataMapContainsKey(player) ? null : (
                !dataMapGet(player).containsKey(key) ? null :
                        dataMapGet(player).get(key));
    }

    public static void setPlayerInstantData(EntityPlayer player, String key, Object value) {
        if (!dataMapContainsKey(player))
            dataMapPut(player, new HashMap<String, Object>());
        dataMapGet(player).put(key, value);
    }

    public static boolean isRemote(EntityPlayer player) {
        return player.worldObj.isRemote;
    }

    public static EntityPlayer getClientPlayer() {
        return ClayiumCore.proxy.getClientPlayer();
    }

    public static boolean dataMapContainsKey(EntityPlayer player) {
        return getPlayerInstantDataMap(isRemote(player)).containsKey(player);
    }

    public static HashMap<String, Object> dataMapPut(EntityPlayer player, HashMap<String, Object> value) {
        return getPlayerInstantDataMap(isRemote(player)).put(player, value);
    }

    public static HashMap<String, Object> dataMapGet(EntityPlayer player) {
        return getPlayerInstantDataMap(isRemote(player)).get(player);
    }

    public static EntityPlayer getFakePlayer(String id, World world, double x, double y, double z) {
        return (EntityPlayer) setWorldAndPosition(getFakePlayer(id), world, x, y, z);
    }

    public static EntityPlayer getFakePlayer(String id, World world, int x, int y, int z) {
        return getFakePlayer(id, world, x + 0.5D, y + 0.5D, z + 0.5D);
    }

    public static EntityPlayer getFakePlayerWithItem(String id, ItemStack itemstack) {
        EntityPlayer player = getFakePlayer(id);
        player.inventory.setInventorySlotContents(0, itemstack);
        player.inventory.currentItem = 0;
        return player;
    }

    public static Entity setWorldAndPosition(Entity entity, World world, double x, double y, double z) {
        if (entity != null) {
            entity.setWorld(world);
            entity.setPosition(x, y, z);
        }
        return entity;
    }

    private static EntityPlayer defaultPlayer = null;


    public static EntityPlayer getFakePlayer(String id) {
        FakePlayer fakePlayer = null;
        EntityPlayerMP ret = null;
        if (id != null || defaultPlayer == null) {
            fakePlayer = FakePlayerFactory.get(DimensionManager.getWorld(0), getGameProfile(id));
            fakePlayer.playerNetServerHandler = new NetHandlerPlayServerFakePlayer(fakePlayer);
            fakePlayer.eyeHeight = 0.0F;
        }

        if (id == null) {
            if (fakePlayer != null)
                defaultPlayer = fakePlayer;
            return defaultPlayer;
        }
        return fakePlayer;
    }

    public static GameProfile getGameProfile(String id) {
        if (id == null)
            id = "[Clayium]";
        return new GameProfile(UUID.nameUUIDFromBytes(id.getBytes(Charsets.UTF_8)), id);
    }

    public static class NetHandlerPlayServerFakePlayer extends NetHandlerPlayServer {
        public void onNetworkTick() {}

        public void kickPlayerFromServer(String p_147360_1_) {}

        public void processInput(C0CPacketInput p_147358_1_) {}

        public void processPlayer(C03PacketPlayer p_147347_1_) {}

        public void setPlayerLocation(double p_147364_1_, double p_147364_3_, double p_147364_5_, float p_147364_7_, float p_147364_8_) {}

        public void processPlayerDigging(C07PacketPlayerDigging p_147345_1_) {}

        public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement p_147346_1_) {}

        public NetHandlerPlayServerFakePlayer(EntityPlayerMP player) {
            super(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManagerFakePlayer(), player);
        }

        public void onDisconnect(IChatComponent p_147231_1_) {}

        public void sendPacket(Packet p_147359_1_) {}

        public void processHeldItemChange(C09PacketHeldItemChange p_147355_1_) {}

        public void processChatMessage(C01PacketChatMessage p_147354_1_) {}

        public void processAnimation(C0APacketAnimation p_147350_1_) {}

        public void processEntityAction(C0BPacketEntityAction p_147357_1_) {}

        public void processUseEntity(C02PacketUseEntity p_147340_1_) {}

        public void processClientStatus(C16PacketClientStatus p_147342_1_) {}

        public void processCloseWindow(C0DPacketCloseWindow p_147356_1_) {}

        public void processClickWindow(C0EPacketClickWindow p_147351_1_) {}

        public void processEnchantItem(C11PacketEnchantItem p_147338_1_) {}

        public void processCreativeInventoryAction(C10PacketCreativeInventoryAction p_147344_1_) {}

        public void processConfirmTransaction(C0FPacketConfirmTransaction p_147339_1_) {}

        public void processUpdateSign(C12PacketUpdateSign p_147343_1_) {}

        public void processKeepAlive(C00PacketKeepAlive p_147353_1_) {}

        public void processPlayerAbilities(C13PacketPlayerAbilities p_147348_1_) {}

        public void processTabComplete(C14PacketTabComplete p_147341_1_) {}

        public void processClientSettings(C15PacketClientSettings p_147352_1_) {}

        public void processVanilla250Packet(C17PacketCustomPayload p_147349_1_) {}

        public void onConnectionStateTransition(EnumConnectionState p_147232_1_, EnumConnectionState p_147232_2_) {}
    }

    public static class NetworkManagerFakePlayer extends NetworkManager {
        public NetworkManagerFakePlayer() {
            super(false);
        }

        public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {}

        public void setConnectionState(EnumConnectionState p_150723_1_) {}

        public void channelInactive(ChannelHandlerContext p_channelInactive_1_) {}

        public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {}

        protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) {}

        public void setNetHandler(INetHandler p_150719_1_) {}

        public void scheduleOutboundPacket(Packet p_150725_1_, GenericFutureListener... p_150725_2_) {}

        public void processReceivedPackets() {}

        public SocketAddress getSocketAddress() {
            return null;
        }

        public void closeChannel(IChatComponent p_150718_1_) {}

        public boolean isLocalChannel() {
            return false;
        }

        public void enableEncryption(SecretKey p_150727_1_) {}

        public boolean isChannelOpen() { return false; }

        public INetHandler getNetHandler() { return null; }

        public IChatComponent getExitMessage() {
            return null;
        }

        public void disableAutoRead() {}

        protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_) {}

        public Channel channel() {
            return null;
        }
    }

}


