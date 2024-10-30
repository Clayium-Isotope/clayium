package mods.clayium.component.teField;

import net.minecraft.nbt.NBTTagLong;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class FieldLong implements FieldComponent, INBTSerializable<NBTTagLong>, Comparable<Long> {
    protected long value = 0;

    public long get() {
        return this.value;
    }

    public void set(long newValue) {
        this.value = newValue;
    }

    public void add(long other) {
        this.value += other;
    }

    @Override
    public int compareTo(@Nonnull Long o) {
        return Long.compare(this.value, o);
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public int getField(int id) {
        return switch (id) {
            case 0 -> (int) this.value;
            case 1 -> (int) (this.value >> Integer.SIZE);
            default -> 0;
        };
    }

    @Override
    public void setField(int id, int value) {
        this.value = switch (id) {
            case 0 -> (this.value & 0xffffffff00000000L) + Integer.toUnsignedLong(value);
            case 1 -> (this.value & 0x00000000ffffffffL) + ((long) value << Integer.SIZE);
            default -> this.value;
        };
    }

    @Override
    public NBTTagLong serializeNBT() {
        return new NBTTagLong(this.value);
    }

    @Override
    public void deserializeNBT(NBTTagLong nbt) {
        this.value = nbt.getLong();
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
