package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import mods.clayium.gui.client.GuiPANCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class PANCoreListPacketHandler
        implements IMessageHandler<PANCoreListPacket, IMessage> {
    public IMessage onMessage(PANCoreListPacket message, MessageContext ctx) {
        GuiScreen gui = (Minecraft.getMinecraft()).currentScreen;
        if (gui == null || !(gui instanceof GuiPANCore))
            return null;
        GuiPANCore guiPanCore = (GuiPANCore) gui;
        if (message.windowId == guiPanCore.inventorySlots.windowId)
            guiPanCore.setItemList(message.ingreds, message.prohibiteds);
        return null;
    }
}
