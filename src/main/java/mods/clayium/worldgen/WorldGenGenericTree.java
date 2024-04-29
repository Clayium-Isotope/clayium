package mods.clayium.worldgen;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

/**
 * it is a copy of {@link net.minecraft.world.gen.feature.WorldGenBirchTree}
 * and was corrected to fit with the ORIGINAL.
 */
public class WorldGenGenericTree extends WorldGenAbstractTree {
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final IBlockState log;
    private final IBlockState leaves;

    public WorldGenGenericTree(boolean notify, int minTreeHeight, IBlockState log, IBlockState leaves, boolean vinesGrow)
    {
        super(notify);

        this.minTreeHeight = minTreeHeight;
        this.vinesGrow = vinesGrow;
        this.log = log;
        this.leaves = leaves;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int i = rand.nextInt(3) + this.minTreeHeight;

        boolean flag = true;

        if (position.getY() < 1 || position.getY() + i + 1 > worldIn.getHeight()) return false;

        for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 1;

            if (j == position.getY())
            {
                k = 0;
            }

            if (j >= position.getY() + 1 + i - 2)
            {
                k = 2;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
            {
                for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                {
                    if (j >= 0 && j < worldIn.getHeight())
                    {
                        if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1)))
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = false;
                    }
                }
            }
        }

        if (!flag)
        {
            return false;
        }
        else
        {
            BlockPos down = position.down();
            IBlockState state = worldIn.getBlockState(down);
            boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (BlockSapling) Blocks.SAPLING);

            if (isSoil && position.getY() < worldIn.getHeight() - i - 1)
            {
                state.getBlock().onPlantGrow(state, worldIn, down, position);

                for (int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2)
                {
                    int k2 = i2 - (position.getY() + i);
                    int l2 = 1 - k2 / 2;

                    for (int i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3)
                    {
                        int j1 = i3 - position.getX();

                        for (int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1)
                        {
                            int l1 = k1 - position.getZ();

                            if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0)
                            {
                                BlockPos blockpos = new BlockPos(i3, i2, k1);
                                IBlockState state2 = worldIn.getBlockState(blockpos);

                                if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(state2, worldIn, blockpos))
                                {
                                    this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leaves);
                                }
                            }
                        }
                    }
                }

                for (int j2 = 0; j2 < i; ++j2)
                {
                    BlockPos upN = position.up(j2);
                    IBlockState state2 = worldIn.getBlockState(upN);

                    if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(state2, worldIn, upN))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, upN, this.log);

                        if (this.vinesGrow && j2 > 0) {
                            if (rand.nextInt(3) > 0 && worldIn.isAirBlock(upN.west())) {
                                this.setBlockAndNotifyAdequately(worldIn, upN.west(), Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, true));
                            }

                            if (rand.nextInt(3) > 0 && worldIn.isAirBlock(upN.east())) {
                                this.setBlockAndNotifyAdequately(worldIn, upN.east(), Blocks.VINE.getDefaultState().withProperty(BlockVine.WEST, true));
                            }

                            if (rand.nextInt(3) > 0 && worldIn.isAirBlock(upN.north())) {
                                this.setBlockAndNotifyAdequately(worldIn, upN.north(), Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, true));
                            }

                            if (rand.nextInt(3) > 0 && worldIn.isAirBlock(upN.south())) {
                                this.setBlockAndNotifyAdequately(worldIn, upN.south(), Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, true));
                            }
                        }
                    }
                }

                if (this.vinesGrow) {
                    for(int y = position.getY() - 3 + i; y <= position.getY() + i; ++y) {
                        int i3 = y - (position.getY() + i);
                        int l1 = 2 - i3 / 2;

                        for(int x = position.getX() - l1; x <= position.getX() + l1; ++x) {
                            for(int z = position.getZ() - l1; z <= position.getZ() + l1; ++z) {
                                BlockPos pos = new BlockPos(x, y, z);

                                if (worldIn.getBlockState(pos).getBlock().isLeaves(worldIn.getBlockState(pos), worldIn, pos)) {
                                    if (rand.nextInt(4) == 0 && worldIn.isAirBlock(pos.west())) {
                                        this.growVines(worldIn, x - 1, y, z, EnumFacing.EAST);
                                    }

                                    if (rand.nextInt(4) == 0 && worldIn.isAirBlock(pos.east())) {
                                        this.growVines(worldIn, x + 1, y, z, EnumFacing.WEST);
                                    }

                                    if (rand.nextInt(4) == 0 && worldIn.isAirBlock(pos.north())) {
                                        this.growVines(worldIn, x, y, z - 1, EnumFacing.SOUTH);
                                    }

                                    if (rand.nextInt(4) == 0 && worldIn.isAirBlock(pos.south())) {
                                        this.growVines(worldIn, x, y, z + 1, EnumFacing.NORTH);
                                    }
                                }
                            }
                        }
                    }

                    if (rand.nextInt(5) == 0 && i > 5) {
                        for(int k1 = 0; k1 < 2; ++k1) {
                            for(EnumFacing i3 : EnumFacing.HORIZONTALS) {
                                if (rand.nextInt(4 - k1) == 0) {
                                    this.setBlockAndNotifyAdequately(worldIn,
                                            new BlockPos(position.getX() + i3.getXOffset(), position.getY() + i - 5 + k1, position.getZ() + i3.getZOffset()),
                                            Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.FACING, i3.getOpposite()).withProperty(BlockCocoa.AGE, rand.nextInt(3) << 2 | i3.getIndex())
                                    );
                                }
                            }
                        }
                    }
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    private void growVines(World world, int x, int startY, int z, EnumFacing direction) {
        int i1 = 4;

        for (BlockPos pos1 : BlockPos.getAllInBox(x, startY, z, x, startY - i1, z)) {
            if (!world.isAirBlock(pos1)) return;

            this.setBlockAndNotifyAdequately(world, pos1, Blocks.VINE.getDefaultState().withProperty(BlockVine.getPropertyFor(direction), true));
        }
    }
}
