package mods.clayium.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;

import mods.clayium.core.ClayiumCore;
import mods.clayium.network.KeyInputEventPacket;
import mods.clayium.network.MouseClickEventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;


public class UtilKeyInput {
    public static UtilKeyInput INSTANCE = new UtilKeyInput();

    @SideOnly(Side.CLIENT)
    public static volatile HashMap<KeyBinding, PlayerKey.KeyType> keyBindingMap;


    public void playerTick(EntityPlayer player) {
        if (!keyMapContainsKey(player)) {
            keyMapPut(player, new PlayerKey());
        }

        keyMapGet(player).update();
    }

    @Deprecated
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void MouseEventSubscriber(MouseEvent event) {
        if (event.button != -1) {
            setMouseInput(UtilPlayer.getClientPlayer(), event.button, event.buttonstate);
            ClayiumCore.packetDispatcher.sendToServer((IMessage) new MouseClickEventPacket(event.button, event.buttonstate));
        }
    }


    @Deprecated
    @SideOnly(Side.CLIENT)
    public void sendMouseEventToServer(int button, boolean buttonstate) {
        EntityPlayer player = UtilPlayer.getClientPlayer();
        if (button != -1 && player != null) {
            boolean oldstate = (getMouseLength(player, button) >= 0);
            if (oldstate != buttonstate) {
                setMouseInput(player, button, buttonstate);
                ClayiumCore.packetDispatcher.sendToServer((IMessage) new MouseClickEventPacket(button, buttonstate));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void ClientTickEventSubscriber(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if ((Minecraft.getMinecraft()).currentScreen != null) {
                sendMouseEventToServer(0, false);
                sendMouseEventToServer(1, false);
            }
            sendAllKeyEventToServer();
        }
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void KeyEventSubscriber(InputEvent.KeyInputEvent event) {}


    @SideOnly(Side.CLIENT)
    public synchronized void sendAllKeyEventToServer() {
        if (keyBindingMap == null) {
            keyBindingMap = new HashMap<KeyBinding, PlayerKey.KeyType>();
            keyBindingMap.put((Minecraft.getMinecraft()).gameSettings.keyBindSprint, PlayerKey.KeyType.SPRINT);
            keyBindingMap.put((Minecraft.getMinecraft()).gameSettings.keyBindUseItem, PlayerKey.KeyType.USE_ITEM);
        }
        for (KeyBinding key : keyBindingMap.keySet()) {


            sendKeyEventToServer(key);
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendKeyEventToServer(PlayerKey.KeyType key, boolean buttonstate) {
        if (key == null)
            return;
        EntityPlayer player = UtilPlayer.getClientPlayer();
        if (player != null) {
            boolean oldstate = (getKeyLength(player, key) >= 0);
            if (oldstate != buttonstate) {
                setKeyInput(player, key, buttonstate);
                ClayiumCore.packetDispatcher.sendToServer((IMessage) new KeyInputEventPacket(key, buttonstate));
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public void sendKeyEventToServer(KeyBinding key) {
        sendKeyEventToServer(keyBindingMap.get(key), key.getIsKeyPressed());
    }

    public static HashMap<EntityPlayer, PlayerKey> playerKeyMapClient = new HashMap<EntityPlayer, PlayerKey>();
    public static HashMap<EntityPlayer, PlayerKey> playerKeyMap = new HashMap<EntityPlayer, PlayerKey>();

    public static PlayerKey keyMapGet(EntityPlayer player) {
        return UtilPlayer.isRemote(player) ? playerKeyMapClient.get(player) : playerKeyMap.get(player);
    }

    public static PlayerKey keyMapPut(EntityPlayer player, PlayerKey value) {
        return UtilPlayer.isRemote(player) ? playerKeyMapClient.put(player, value) : playerKeyMap.put(player, value);
    }

    public static boolean keyMapContainsKey(EntityPlayer player) {
        return UtilPlayer.isRemote(player) ? playerKeyMapClient.containsKey(player) : playerKeyMap.containsKey(player);
    }

    public static int getKeyLength(EntityPlayer player, PlayerKey.KeyType key) {
        if (!keyMapContainsKey(player))
            keyMapPut(player, new PlayerKey());
        return keyMapGet(player).getKeyLength(key);
    }


    public static void setKeyInput(EntityPlayer player, PlayerKey.KeyType key, boolean buttonstate) {
        if (!keyMapContainsKey(player))
            keyMapPut(player, new PlayerKey());
        keyMapGet(player).setKeyInput(key, buttonstate);
    }


    @Deprecated
    public static int getMouseLength(EntityPlayer player, int button) {
        if (!keyMapContainsKey(player))
            keyMapPut(player, new PlayerKey());
        return keyMapGet(player).getMouseLength(button);
    }


    @Deprecated
    public static void setMouseInput(EntityPlayer player, int button, boolean buttonstate) {
        if (!keyMapContainsKey(player))
            keyMapPut(player, new PlayerKey());
        keyMapGet(player).setMouseInput(button, buttonstate);
    }
}


