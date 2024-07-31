package mods.clayium.component;

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
public class Stockholder implements Comparable<Long>, INBTSerializable<NBTTagIntArray> {
    protected static final NumberFormat stringify = new SIFormat(true, 0);
    public static final Stockholder ZERO = new Stockholder(0, 0);

    protected long magnitude;
    protected long mutable;

    protected Stockholder(long magnitude, long value) {
        this.magnitude = magnitude;
        this.mutable = value;
    }

    public static Stockholder init() {
        return new Stockholder(0, 0);
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
            throw new ArithmeticException("Too less magnitude to subtract!");
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
        return stringify.format(this.magnitude * Long.MAX_VALUE + this.mutable);
    }

    @Override
    public int compareTo(@Nonnull Long o) {
        if (this.magnitude > 0) return 1;

        return Long.compare(this.mutable, o);
    }

    @Override
    public NBTTagIntArray serializeNBT() {
        return new NBTTagIntArray(new int[] {
                (int) this.mutable,
                (int) (this.mutable >> Integer.SIZE),
                (int) this.magnitude,
                (int) (this.magnitude >> Integer.SIZE)
        });
    }

    @Override
    public void deserializeNBT(NBTTagIntArray nbt) {
        int[] value = nbt.getIntArray();

        this.clear();
        if (value.length == 4) {
            this.add(new Stockholder(
                    (long) value[3] << Integer.SIZE + value[2],
                    (long) value[1] << Integer.SIZE + value[0]
            ));
        }
    }
}
