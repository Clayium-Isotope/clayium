package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import mods.clayium.gui.client.GuiTemp;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;


public class GuiTextFieldPacketHandler
        implements IMessageHandler<GuiTextFieldPacket, IMessage> {
    public IMessage onMessage(GuiTextFieldPacket message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            EntityPlayerMP entityPlayerMP = (ctx.getServerHandler()).playerEntity;
            ContainerTemp container = (ContainerTemp) ((EntityPlayer) entityPlayerMP).openContainer;

            if (message.string != null && message.string.length() >= 1) {


                String s = message.string;
                container.setTextFieldString((EntityPlayer) entityPlayerMP, s, message.id);
            } else {

                container.setTextFieldString((EntityPlayer) entityPlayerMP, "", message.id);
            }
        } else {

            GuiScreen guiScreen = (Minecraft.getMinecraft()).currentScreen;
            if (guiScreen instanceof GuiTemp) {
                GuiTemp gui = (GuiTemp) guiScreen;
                gui.setTextFieldString(message.string, message.id, true);
            }
        }

        return null;
    }
}
