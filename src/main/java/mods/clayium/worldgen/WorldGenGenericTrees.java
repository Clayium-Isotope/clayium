package mods.clayium.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;


public class WorldGenGenericTrees
        extends WorldGenAbstractTree {
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final int metaWood;
    private final int metaLeaves;
    private final Block blockWood;
    private final Block blockLeaves;
    private static final String __OBFID = "CL_00000438";

    public WorldGenGenericTrees(boolean notify, int minTreeHeight, Block blockWood, int metaWood, Block blockLeaves, int metaLeaves, boolean vinesGrow) {
        super(notify);
        this.minTreeHeight = minTreeHeight;
        this.blockWood = blockWood;
        this.metaWood = metaWood;
        this.blockLeaves = blockLeaves;
        this.metaLeaves = metaLeaves;
        this.vinesGrow = vinesGrow;
    }


    public boolean generate(World world, Random random, int x, int y, int z) {
        int l = random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (y >= 1 && y + l + 1 <= 256) {


            for (int i1 = y; i1 <= y + 1 + l; i1++) {

                byte b0 = 1;

                if (i1 == y) {
                    b0 = 0;
                }

                if (i1 >= y + 1 + l - 2) {
                    b0 = 2;
                }

                for (int j1 = x - b0; j1 <= x + b0 && flag; j1++) {

                    for (int k1 = z - b0; k1 <= z + b0 && flag; k1++) {

                        if (i1 >= 0 && i1 < 256) {

                            Block block = world.getBlock(j1, i1, k1);

                            if (!isReplaceable(world, j1, i1, k1)) {
                                flag = false;
                            }
                        } else {

                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            }


            Block block2 = world.getBlock(x, y - 1, z);

            boolean isSoil = block2.canSustainPlant((IBlockAccess) world, x, y - 1, z, ForgeDirection.UP, (IPlantable) Blocks.sapling);
            if (isSoil && y < 256 - l - 1) {

                block2.onPlantGrow(world, x, y - 1, z, x, y, z);
                byte b0 = 3;
                byte b1 = 0;


                int k1;


                for (k1 = y - b0 + l; k1 <= y + l; k1++) {

                    int i3 = k1 - y + l;
                    int l1 = b1 + 1 - i3 / 2;

                    for (int i2 = x - l1; i2 <= x + l1; i2++) {

                        int j2 = i2 - x;

                        for (int k2 = z - l1; k2 <= z + l1; k2++) {

                            int l2 = k2 - z;

                            if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || (random.nextInt(2) != 0 && i3 != 0)) {

                                Block block1 = world.getBlock(i2, k1, k2);

                                if (block1.isAir((IBlockAccess) world, i2, k1, k2) || block1.isLeaves((IBlockAccess) world, i2, k1, k2)) {
                                    setBlockAndNotifyAdequately(world, i2, k1, k2, this.blockLeaves, this.metaLeaves);
                                }
                            }
                        }
                    }
                }

                for (k1 = 0; k1 < l; k1++) {

                    Block block = world.getBlock(x, y + k1, z);

                    if (block.isAir((IBlockAccess) world, x, y + k1, z) || block.isLeaves((IBlockAccess) world, x, y + k1, z)) {

                        setBlockAndNotifyAdequately(world, x, y + k1, z, this.blockWood, this.metaWood);

                        if (this.vinesGrow && k1 > 0) {

                            if (random.nextInt(3) > 0 && world.isAirBlock(x - 1, y + k1, z)) {
                                setBlockAndNotifyAdequately(world, x - 1, y + k1, z, Blocks.vine, 8);
                            }

                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + k1, z)) {
                                setBlockAndNotifyAdequately(world, x + 1, y + k1, z, Blocks.vine, 2);
                            }

                            if (random.nextInt(3) > 0 && world.isAirBlock(x, y + k1, z - 1)) {
                                setBlockAndNotifyAdequately(world, x, y + k1, z - 1, Blocks.vine, 1);
                            }

                            if (random.nextInt(3) > 0 && world.isAirBlock(x, y + k1, z + 1)) {
                                setBlockAndNotifyAdequately(world, x, y + k1, z + 1, Blocks.vine, 4);
                            }
                        }
                    }
                }

                if (this.vinesGrow) {

                    for (k1 = y - 3 + l; k1 <= y + l; k1++) {

                        int i3 = k1 - y + l;
                        int l1 = 2 - i3 / 2;

                        for (int i2 = x - l1; i2 <= x + l1; i2++) {

                            for (int j2 = z - l1; j2 <= z + l1; j2++) {

                                if (world.getBlock(i2, k1, j2).isLeaves((IBlockAccess) world, i2, k1, j2)) {

                                    if (random.nextInt(4) == 0 && world.getBlock(i2 - 1, k1, j2).isAir((IBlockAccess) world, i2 - 1, k1, j2)) {
                                        growVines(world, i2 - 1, k1, j2, 8);
                                    }

                                    if (random.nextInt(4) == 0 && world.getBlock(i2 + 1, k1, j2).isAir((IBlockAccess) world, i2 + 1, k1, j2)) {
                                        growVines(world, i2 + 1, k1, j2, 2);
                                    }

                                    if (random.nextInt(4) == 0 && world.getBlock(i2, k1, j2 - 1).isAir((IBlockAccess) world, i2, k1, j2 - 1)) {
                                        growVines(world, i2, k1, j2 - 1, 1);
                                    }

                                    if (random.nextInt(4) == 0 && world.getBlock(i2, k1, j2 + 1).isAir((IBlockAccess) world, i2, k1, j2 + 1)) {
                                        growVines(world, i2, k1, j2 + 1, 4);
                                    }
                                }
                            }
                        }
                    }

                    if (random.nextInt(5) == 0 && l > 5) {
                        for (k1 = 0; k1 < 2; k1++) {

                            for (int i3 = 0; i3 < 4; i3++) {

                                if (random.nextInt(4 - k1) == 0) {

                                    int l1 = random.nextInt(3);
                                    setBlockAndNotifyAdequately(world, x + Direction.offsetX[Direction.rotateOpposite[i3]], y + l - 5 + k1, z + Direction.offsetZ[Direction.rotateOpposite[i3]], Blocks.cocoa, l1 << 2 | i3);
                                }
                            }
                        }
                    }
                }

                return true;
            }


            return false;
        }


        return false;
    }


    private void growVines(World world, int x, int starty, int z, int vinelength) {
        setBlockAndNotifyAdequately(world, x, starty, z, Blocks.vine, vinelength);
        int i1 = 4;


        while (true) {
            starty--;

            if (!world.getBlock(x, starty, z).isAir((IBlockAccess) world, x, starty, z) || i1 <= 0) {
                return;
            }


            setBlockAndNotifyAdequately(world, x, starty, z, Blocks.vine, vinelength);
            i1--;
        }
    }
}
