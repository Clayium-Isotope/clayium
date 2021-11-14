package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.core.ClayiumCore;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class ClayTreeLog
        extends BlockLog
        implements ITieredBlock {
    public static final String[] iconNames = new String[] {"claytree"};

    public ClayTreeLog() {
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setHardness(1.5F);
        setStepSound(soundTypeGravel);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.field_150167_a = new net.minecraft.util.IIcon[iconNames.length];
        this.field_150166_b = new net.minecraft.util.IIcon[iconNames.length];

        for (int i = 0; i < this.field_150167_a.length; i++) {

            this.field_150167_a[i] = p_149651_1_.registerIcon(getTextureName() + "_" + iconNames[i]);
            this.field_150166_b[i] = p_149651_1_.registerIcon(getTextureName() + "_" + iconNames[i] + "_top");
        }
    }

    public int getTier(ItemStack itemstack) {
        return 7;
    }

    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 7;
    }
}
