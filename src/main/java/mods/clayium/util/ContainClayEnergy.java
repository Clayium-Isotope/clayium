package mods.clayium.util;

public class ContainClayEnergy {
    private long containEnergy = 0;

    public long get() {
        return this.containEnergy;
    }

    public void set(long containEnergy) {
        this.containEnergy = containEnergy;
    }

    public void increase(long amount) {
        this.set(this.get() + amount);
    }

    public void decrease(long amount) {
        this.increase(-amount);
    }

    public boolean hasEnough(long amount) {
        return this.containEnergy >= amount;
    }

    @Override
    public String toString() {
        return UtilLocale.ClayEnergyNumeral(this.containEnergy);
    }

    public static final ContainClayEnergy NIL = new HasNoEnergy();
    private static class HasNoEnergy extends ContainClayEnergy {
        @Override
        public long get() {
            return 0;
        }

        @Override
        public void set(long containEnergy) {
            throw new UnsupportedOperationException();
        }
    }
}
