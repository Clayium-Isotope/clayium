package mods.clayium.component;

import mods.clayium.component.teField.FieldComponent;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.SIFormat;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.text.NumberFormat;

/**
 * 運用上は Long.MAX_VALUE^2 ≒ 8.5e+37 までの自然数を扱えれば十分だと思う。
 * このDocを書いている時点では直接的な get メソッドを用意していない。
 *
 * @see java.math.BigInteger
 */
public class Stockholder extends Number implements Comparable<Long>, INBTSerializable<NBTTagIntArray>, FieldComponent {
    protected static final NumberFormat stringify = new SIFormat(true, 0);
    public static final Stockholder ZERO = new Stockholder(0, 0);

    protected long magnitude;
    protected long mutable;

    protected Stockholder(long magnitude, long value) {
        this.magnitude = magnitude;
        this.mutable = value;
    }

    public Stockholder() {
        this(0, 0);
    }

    @Override
    public int intValue() {
        return (int) this.mutable;
    }

    @Override
    public long longValue() {
        return this.mutable;
    }

    @Override
    public float floatValue() {
        return (float) this.magnitude * Long.MAX_VALUE + this.mutable;
    }

    @Override
    public double doubleValue() {
        return (double) this.magnitude * Long.MAX_VALUE + this.mutable;
    }

    public void clear() {
        this.magnitude = 0;
        this.mutable = 0;
    }

    public Stockholder copy() {
        return new Stockholder(this.magnitude, this.mutable);
    }

    /**
     * @throws ArithmeticException 繰り上げできないとき
     */
    public Stockholder add(long value) throws ArithmeticException {
        if (value == 0) return this;
        if (value < 0) return this.sub(-value);

        long newMutable = this.mutable + value;
        if (newMutable >= 0) {
            this.mutable = newMutable;
            return this;
        }

        // 繰り上がるとき
        this.magnitude += 1;
        if (this.magnitude < 0) {
            // 繰り上げられないとき
            throw new ArithmeticException("Cannot append with max magnitude");
        }
        this.mutable = Long.MAX_VALUE + newMutable;
        return this;
    }

    public Stockholder add(final Stockholder other) throws ArithmeticException {
        long newMagnitude = this.magnitude + other.magnitude;
        if (newMagnitude < 0) {
            // 加算によるオーバーフロー
            throw new ArithmeticException("Too much magnitude to append!");
        }

        this.magnitude = newMagnitude;
        return this.add(other.mutable);
    }

    /**
     * @throws ArithmeticException 繰り下げできないとき
     */
    public Stockholder sub(long value) throws ArithmeticException {
        if (value == 0) return this;

        if (value <= this.mutable) {
            // 繰り下がりが不要な場合
            this.mutable -= value;
            return this;
        }

        // 繰り下がりが必要な場合
        if (this.magnitude <= 0) {
            // 繰り下げられないとき
//            throw new ArithmeticException("Too less magnitude to subtract!");
            ClayiumCore.logger.error("Too less magnitude to subtract!");
            this.magnitude = 0;
            this.mutable = 0;
            return this;
        }

        this.magnitude -= 1;
        this.mutable += Long.MAX_VALUE - value;
        return this;
    }

    /**
     * @throws ArithmeticException 繰り下げできないとき
     */
    public Stockholder sub(final Stockholder value) throws ArithmeticException {
        long newMagnitude = this.magnitude - value.magnitude;
        if (newMagnitude < 0) {
            // 繰り下げられないとき
            throw new ArithmeticException("Too less magnitude to subtract!");
        }

        this.magnitude = newMagnitude;
        return this.sub(value.mutable);
    }

    @Override
    public String toString() {
        return stringify.format(this.doubleValue());
    }

    @Override
    public int compareTo(@Nonnull Long o) {
        if (this.magnitude > 0) return 1;

        return Long.compare(this.mutable, o);
    }

    @Override
    public NBTTagIntArray serializeNBT() {
        return new NBTTagIntArray(new int[] {
                this.getField(0),
                this.getField(1),
                this.getField(2),
                this.getField(3)
        });
    }

    @Override
    public void deserializeNBT(NBTTagIntArray nbt) {
        int[] value = nbt.getIntArray();

        this.clear();
        if (value.length == 4) {
            this.setField(0, value[0]);
            this.setField(1, value[1]);
            this.setField(2, value[2]);
            this.setField(3, value[3]);
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public int getField(int id) {
        return switch (id) {
            case 0 -> (int) this.mutable;
            case 1 -> (int) (this.mutable >> Integer.SIZE);
            case 2 -> (int) this.magnitude;
            case 3 -> (int) (this.magnitude >> Integer.SIZE);
            default -> 0;
        };
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0 -> this.mutable = (this.mutable & 0xffffffff00000000L) + Integer.toUnsignedLong(value);
            case 1 -> this.mutable = (this.mutable & 0x00000000ffffffffL) + ((long) value << Integer.SIZE);
            case 2 -> this.magnitude = (this.magnitude & 0xffffffff00000000L) + Integer.toUnsignedLong(value);
            case 3 -> this.magnitude = (this.magnitude & 0x00000000ffffffffL) + ((long) value << Integer.SIZE);
        }
    }
}
