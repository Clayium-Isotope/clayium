package mods.clayium.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GuiButtonPacket implements IMessage {
    private int buttonId;
    private int coordX;
    private int coordY;
    private int coordZ;

    public GuiButtonPacket() {}

    public GuiButtonPacket(int _coordX, int _coordY, int _coordZ, int _buttonId) {
        this.coordX = _coordX;
        this.coordY = _coordY;
        this.coordZ = _coordZ;
        this.buttonId = _buttonId;
    }

    public void fromBytes(ByteBuf buffer) {
        this.coordX = buffer.getInt(0);
        this.coordY = buffer.getInt(4);
        this.coordZ = buffer.getInt(8);
        this.buttonId = buffer.getInt(12);
    }

    public void toBytes(ByteBuf buffer) {
        buffer.readerIndex(0);
        buffer.writeInt(this.coordX);
        buffer.writeInt(this.coordY);
        buffer.writeInt(this.coordZ);
        buffer.writeInt(this.buttonId);
    }

    public int getButtonId() {
        return this.buttonId;
    }
}
