package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class GuiButtonPacketHandler
        implements IMessageHandler<GuiButtonPacket, IMessage> {
    public IMessage onMessage(GuiButtonPacket packet, MessageContext ctx) {
        return null;
    }
}
