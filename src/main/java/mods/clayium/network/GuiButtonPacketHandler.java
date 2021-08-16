package mods.clayium.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiButtonPacketHandler implements IMessageHandler<GuiButtonPacket, IMessage> {
    public GuiButtonPacketHandler() {
    }

    public IMessage onMessage(GuiButtonPacket packet, MessageContext ctx) {
        return null;
    }
}
