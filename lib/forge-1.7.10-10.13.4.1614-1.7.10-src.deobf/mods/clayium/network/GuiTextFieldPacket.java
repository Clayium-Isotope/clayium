package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

import mods.clayium.core.ClayiumCore;
import net.minecraft.network.PacketBuffer;

public class GuiTextFieldPacket
        implements IMessage {
    public int id;
    public String string;

    public GuiTextFieldPacket() {}

    public GuiTextFieldPacket(String string, int id) {
        this.id = id;
        this.string = string;
    }


    public void fromBytes(ByteBuf buf) {
        PacketBuffer b = new PacketBuffer(buf);
        this.id = b.readInt();
        try {
            this.string = b.readStringFromBuffer(128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void toBytes(ByteBuf buf) {
        PacketBuffer b = new PacketBuffer(buf);
        b.writeInt(this.id);
        try {
            b.writeStringToBuffer(this.string);
        } catch (IOException e) {
            ClayiumCore.logger.catching(e);
        }
    }
}
