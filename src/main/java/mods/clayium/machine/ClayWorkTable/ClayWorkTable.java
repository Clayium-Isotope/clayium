package mods.clayium.machine.ClayWorkTable;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@HasOriginalState
public class ClayWorkTable extends ClayContainer {
    public ClayWorkTable() {
        super(Material.ROCK, TileEntityClayWorkTable::new, "clay_work_table", GuiHandler.GuiIdClayWorkTable, TierPrefix.clay);
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(4F);
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
