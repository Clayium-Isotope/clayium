package mods.clayium.machine.LaserReflector;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LaserReflector extends BlockContainer implements ITieredBlock {
    public static PropertyDirection FACING = BlockDirectional.FACING;

    public LaserReflector() {
        super(Material.IRON);

        setUnlocalizedName("laser_reflector");
        setRegistryName(ClayiumCore.ModId, "laser_reflector");
        setCreativeTab(ClayiumCore.tabClayium);
        setSoundType(SoundType.GLASS);
        setHardness(1.0F);
        setResistance(1.0F);

        setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityLaserReflector telr = new TileEntityLaserReflector();
        telr.initParamsByTier(TierPrefix.claySteel);
        return telr;
    }

    @Override
    public TierPrefix getTier(ItemStack stackIn) {
        return TierPrefix.claySteel;
    }

    @Override
    public TierPrefix getTier(IBlockAccess access, BlockPos posIn) {
        return TierPrefix.claySteel;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean getUseNeighborBrightness(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        float f = 0.125F;
        switch (state.getValue(FACING).getAxis()) {
            case Y:
                return new AxisAlignedBB(0.0F + f * 2.0F, 0.0F + f, 0.0F + f * 2.0F, 1.0F - f * 2.0F, 1.0F - f, 1.0F - f * 2.0F);
            case Z:
                return new AxisAlignedBB(0.0F + f * 2.0F, 0.0F + f * 2.0F, 0.0F + f, 1.0F - f * 2.0F, 1.0F - f * 2.0F, 1.0F - f);
            case X:
                return new AxisAlignedBB(0.0F + f, 0.0F + f * 2.0F, 0.0F + f * 2.0F, 1.0F - f, 1.0F - f * 2.0F, 1.0F - f * 2.0F);
        }
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() == ClayiumItems.wrench || playerIn.getHeldItem(hand).getItem() == ClayiumItems.spatula)
            return false;

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }
}
