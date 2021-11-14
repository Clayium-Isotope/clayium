package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;

import mods.clayium.util.UtilAdvancedTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;


public class ClaySteelPickaxePacketHandler
        implements IMessageHandler<ClaySteelPickaxePacket, IMessage> {
    public IMessage onMessage(ClaySteelPickaxePacket message, MessageContext ctx) {
        EntityPlayerMP entityPlayerMP = (ctx.getServerHandler()).playerEntity;
        if (message.side != -2) {
            UtilAdvancedTools.sideList.put(entityPlayerMP, Integer.valueOf(message.side));
        } else {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("x", Integer.valueOf(message.x));
            map.put("y", Integer.valueOf(message.y));
            map.put("z", Integer.valueOf(message.z));
            map.put("d", Integer.valueOf(message.dimid));
            DimensionManager.getWorld(message.dimid).markBlockForUpdate(message.x, message.y, message.z);
            UtilAdvancedTools.forceList.put(entityPlayerMP, map);
        }

        return null;
    }
}
