package mods.clayium.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClaySteelPickaxePacket implements IMessage {

    public EnumFacing side;
    public int dimid;
    public Vec3i pos;

    public ClaySteelPickaxePacket() {}

    public ClaySteelPickaxePacket(EnumFacing side) {
        this.side = side;
    }

    public ClaySteelPickaxePacket(World world, Vec3i pos) {
        this.side = null;
        this.dimid = world.provider.getDimension();
        this.pos = pos;
    }

    public void fromBytes(ByteBuf buf) {
        int _side = buf.readInt();
        this.side = _side == -1 ? null : EnumFacing.byIndex(_side);
        this.dimid = buf.readInt();
        this.pos = new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.side == null ? -1 : this.side.getIndex());
        buf.writeInt(this.dimid);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }
}
