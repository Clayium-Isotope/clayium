package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@HasOriginalState
public class ClayCraftingTable extends ClayContainer {
    public ClayCraftingTable() {
        super(Material.CLAY, TileEntityClayCraftingTable.class, "clay_crafting_table", GuiHandler.GuiIdClayCraftingTable, TierPrefix.none);
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

    @Override
    public boolean canBePipe() {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        openGui(worldIn, pos, playerIn);
        return true;
    }
}
