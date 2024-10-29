package mods.clayium.util.exception;

public class IllegalTierException extends IllegalArgumentException {

    public IllegalTierException() {
        super("Unknown Tier");
    }
}
