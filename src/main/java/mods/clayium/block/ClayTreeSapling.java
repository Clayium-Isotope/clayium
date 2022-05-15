package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import mods.clayium.worldgen.WorldGenGenericTrees;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ClayTreeSapling
        extends BlockBush
        implements IGrowable, ITieredBlock {
    private IIcon saplingIcon;

    public ClayTreeSapling() {
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    }


    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {

            super.updateTick(world, x, y, z, random);

            if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
                growUp(world, x, y, z, random);
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return this.saplingIcon;
    }


    public void growUp(World world, int x, int y, int z, Random random) {
        int l = world.getBlockMetadata(x, y, z);

        if ((l & 0x8) == 0) {

            world.setBlockMetadataWithNotify(x, y, z, l | 0x8, 4);
        } else {

            growTree(world, x, y, z, random);
        }
    }


    public void growTree(World world, int x, int y, int z, Random random) {
        if (!TerrainGen.saplingGrowTree(world, random, x, y, z))
            return;
        int l = world.getBlockMetadata(x, y, z) & 0x7;

        Object object = new WorldGenGenericTrees(true, 5, CBlocks.blockClayTreeLog, 0, CBlocks.blockClayTreeLeaf, 0, false);
        int i1 = 0, j1 = 0;
        boolean flag = false;


        switch (l) {


            case 1:
label34:
                for (i1 = 0; i1 >= -1; i1--) {

                    for (j1 = 0; j1 >= -1; j1--) {

                        if (isSameTypeSapling(world, x + i1, y, z + j1, l) &&
                                isSameTypeSapling(world, x + i1 + 1, y, z + j1, l) &&
                                isSameTypeSapling(world, x + i1, y, z + j1 + 1, l) &&
                                isSameTypeSapling(world, x + i1 + 1, y, z + j1 + 1, l)) {

                            object = new WorldGenMegaPineTree(false, random.nextBoolean());
                            flag = true;

                            break label34;
                        }
                    }
                }
                if (!flag) {

                    j1 = 0;
                    i1 = 0;
                    object = new WorldGenTaiga2(true);
                }
                break;
        }


        Block block = Blocks.air;


        if (flag) {

            world.setBlock(x + i1, y, z + j1, block, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1, block, 0, 4);
            world.setBlock(x + i1, y, z + j1 + 1, block, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1 + 1, block, 0, 4);
        } else {

            world.setBlock(x, y, z, block, 0, 4);
        }


        if (!((WorldGenerator) object).generate(world, random, x + i1, y, z + j1)) {
            if (flag) {

                world.setBlock(x + i1, y, z + j1, (Block) this, l, 4);
                world.setBlock(x + i1 + 1, y, z + j1, (Block) this, l, 4);
                world.setBlock(x + i1, y, z + j1 + 1, (Block) this, l, 4);
                world.setBlock(x + i1 + 1, y, z + j1 + 1, (Block) this, l, 4);
            } else {

                world.setBlock(x, y, z, (Block) this, l, 4);
            }
        }
    }


    public boolean isSameTypeSapling(World world, int x, int y, int z, int meta) {
        return (world.getBlock(x, y, z) == this && (world.getBlockMetadata(x, y, z) & 0x7) == meta);
    }


    public int damageDropped(int meta) {
        return MathHelper.clamp_int(meta & 0x7, 0, 5);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.saplingIcon = p_149651_1_.registerIcon(getTextureName());
    }


    public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
        return true;
    }


    public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
        return (p_149852_1_.rand.nextFloat() < 0.45D);
    }


    public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_, int p_149853_5_) {
        growUp(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
    }


    public int getTier(ItemStack itemstack) {
        return 7;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 7;
    }
}
