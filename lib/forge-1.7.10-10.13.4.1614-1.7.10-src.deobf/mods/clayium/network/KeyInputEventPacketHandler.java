package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import mods.clayium.util.UtilKeyInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class KeyInputEventPacketHandler implements IMessageHandler<KeyInputEventPacket, IMessage> {
    public IMessage onMessage(KeyInputEventPacket message, MessageContext ctx) {
        EntityPlayerMP entityPlayerMP = (ctx.getServerHandler()).playerEntity;
        UtilKeyInput.setKeyInput((EntityPlayer) (ctx.getServerHandler()).playerEntity, message.key, message.keystate);
        return null;
    }
}
