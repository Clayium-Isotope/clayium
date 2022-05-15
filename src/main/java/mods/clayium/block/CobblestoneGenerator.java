package mods.clayium.block;

import mods.clayium.block.tile.TileCobblestoneGenerator;

public class CobblestoneGenerator
        extends ClayNoRecipeMachines {
    public CobblestoneGenerator(int tier) {
        super((String) null, "clayium:cobblestonegenerator", tier, (Class) TileCobblestoneGenerator.class, 2);
        this.guiId = 11;
    }
}
