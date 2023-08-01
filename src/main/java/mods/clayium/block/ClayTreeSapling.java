package mods.clayium.block;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TierPrefix;
import mods.clayium.worldgen.WorldGenGenericTree;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class ClayTreeSapling extends BlockBush implements IGrowable, ITieredBlock {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.1d, 0.0d, 0.1d, 0.9d, 0.8d, 0.9d);
    protected static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    public ClayTreeSapling() {
        super();
        setUnlocalizedName("clay_tree_sapling");
        setRegistryName(ClayiumCore.ModId, "clay_tree_sapling");
        setCreativeTab(ClayiumCore.tabClayium);

        setDefaultState(this.getDefaultState().withProperty(STAGE, 0));

        setSoundType(SoundType.PLANT);
        setHarvestLevel("pickaxe", 1);
        setHardness(0.0F);
        setResistance(2F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override // copied form BlockSapling
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return;
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.growUp(worldIn, pos, state, rand);
            }
        }
    }

    public void growUp(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(STAGE) == 0) {
            worldIn.setBlockState(pos, state.withProperty(STAGE, 1), 4);
        } else {
            this.growTree(worldIn, pos, state, rand);
        }
    }

    public void growTree(World world, BlockPos pos, IBlockState state, Random random) {
        if (!TerrainGen.saplingGrowTree(world, random, pos)) return;

        WorldGenerator generator = new WorldGenGenericTree(true, 5, ClayiumBlocks.clayTreeLog.getDefaultState(), ClayiumBlocks.clayTreeLeaf.getDefaultState(), false);

        // There was some line which I can't understand why.
        // Might be for 2x2 tree

        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        if (!generator.generate(world, random, pos)) {
            world.setBlockState(pos, state, 4);
        }
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return (double) worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.growUp(worldIn, pos, state, rand);
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
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, meta);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STAGE);
    }
}
