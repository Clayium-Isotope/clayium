package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class ClaySteelPickaxePacket implements IMessage {
    public int side;
    public int dimid;
    public int x;
    public int y;
    public int z;

    public ClaySteelPickaxePacket() {}

    public ClaySteelPickaxePacket(int side) {
        this.side = side;
    }

    public ClaySteelPickaxePacket(World world, int x, int y, int z) {
        this.side = -2;
        this.dimid = world.provider.dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void fromBytes(ByteBuf buf) {
        this.side = buf.readInt();
        this.dimid = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }


    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.side);
        buf.writeInt(this.dimid);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }
}
