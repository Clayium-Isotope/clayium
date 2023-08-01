package mods.clayium.util;

public class IllegalTierException extends IllegalArgumentException {
    public IllegalTierException() {
        super("Unknown Tier");
    }
}
