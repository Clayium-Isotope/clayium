package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CMaterials;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClayTreeLeaf
        extends BlockLeaves implements ITieredBlock {
    public static final String[][] iconNames = new String[][] {{"clayium:leaves_claytree", "leaves_big_oak"}, {"clayium:leaves_claytree_opaque", "leaves_big_oak_opaque"}};
    public static final String[] nameSuffix = new String[] {"claytree", "big_oak"};


    public ClayTreeLeaf() {
        setCreativeTab(ClayiumCore.creativeTabClayium);
    }


    protected void func_150124_c(World world, int x, int y, int z, int meta, int chance) {
        if ((meta & 0x3) == 0 && world.rand.nextInt(chance) == 0) {
            dropBlockAsItem(world, x, y, z, CMaterials.get(CMaterials.DENSE_CLAY, CMaterials.DUST));
        }
    }


    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 16777215;
    }


    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return 16777215;
    }


    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
        return 16777215;
    }


    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) & 0x3;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        setGraphicsLevel((Minecraft.getMinecraft()).gameSettings.fancyGraphics);

        return ((p_149691_2_ & 0x3) == 1) ? this.field_150129_M[this.field_150127_b][1] : this.field_150129_M[this.field_150127_b][0];
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        for (int i = 0; i < iconNames.length; i++) {

            this.field_150129_M[i] = new IIcon[(iconNames[i]).length];

            for (int j = 0; j < (iconNames[i]).length; j++) {
                this.field_150129_M[i][j] = register.registerIcon(iconNames[i][j]);
            }
        }
    }


    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return CMaterials.get(CMaterials.CLAY, CMaterials.DUST).getItem();
    }


    public int damageDropped(int p_149692_1_) {
        return CMaterials.get(CMaterials.CLAY, CMaterials.DUST).getItemDamage();
    }


    public String[] func_150125_e() {
        return nameSuffix;
    }

    public int getTier(ItemStack itemstack) {
        return 7;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 7;
    }
}
