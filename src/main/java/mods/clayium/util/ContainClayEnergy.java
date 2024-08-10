package mods.clayium.util;

import mods.clayium.component.Stockholder;

public class ContainClayEnergy extends Stockholder {
    public ContainClayEnergy() {
        super();
    }

    public void set(long containEnergy) {
        this.clear();
        this.add(containEnergy);
    }

    public boolean hasEnough(long amount) {
        return this.compareTo(amount) >= 0;
    }

    public boolean hasEnough(double amount) {
        return Double.compare(this.doubleValue(), amount) >= 0;
    }

    @Override
    public String toString() {
        return UtilLocale.ClayEnergyNumeral(this.doubleValue());
    }
}
