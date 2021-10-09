
package mods.clayium.machine.ClayWorkTable;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.common.ClayMachineTempTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayWorkTable extends ClayMachineTempTiered {
	public ClayWorkTable() {
		super(Material.ROCK, TileEntityClayWorkTable.class, "clay_work_table", GuiHandler.clayWorkTableGuiID, 0);
		setSoundType(SoundType.GROUND);
		setHarvestLevel("shovel", 0);
		setHardness(1F);
		setResistance(4F);
	}

	public static void updateBlockState(World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(pos, tileentity);
		}
	}
}
