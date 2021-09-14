
package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class RawClayMachineHull extends BlockTiered {
	public RawClayMachineHull() {
		super(Material.CLAY, "raw_clay_machine_hull", 1);
		setSoundType(SoundType.GROUND);
		setHarvestLevel("shovel", 0);
		setHardness(1F);
		setResistance(1F);
	}
}
