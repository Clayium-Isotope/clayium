package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ClayCraftingTable extends ClayContainer {
    public ClayCraftingTable() {
        super(Material.CLAY, TileEntityClayCraftingTable.class, "clay_crafting_table", GuiHandler.GuiIdClayCraftingTable, 0);
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(4F);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0D, 0D, 0D, 1D, 0.25D, 1D);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
