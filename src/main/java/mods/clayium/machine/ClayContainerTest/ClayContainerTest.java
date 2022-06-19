package mods.clayium.machine.ClayContainerTest;

import mods.clayium.machine.ClayContainer.ClayContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ClayContainerTest extends ClayContainer {
    public ClayContainerTest() {
        super(Material.CLAY, TEClayContainerTest.class, "cc_test", -1, 4);

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }
}
