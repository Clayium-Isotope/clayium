package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import mods.clayium.util.PlayerKey;

public class KeyInputEventPacket implements IMessage {
    public PlayerKey.KeyType key;
    public boolean keystate;

    public KeyInputEventPacket() {}

    public KeyInputEventPacket(PlayerKey.KeyType key, boolean keystate) {
        this.key = key;
        this.keystate = keystate;
    }


    public void fromBytes(ByteBuf buf) {
        this.key = PlayerKey.KeyType.getKeyFromId(buf.readInt());
        this.keystate = buf.readBoolean();
    }


    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.key.getId());
        buf.writeBoolean(this.keystate);
    }
}
