package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

import mods.clayium.core.ClayiumCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class DenseClay
        extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon TopIcon;
    @SideOnly(Side.CLIENT)
    private IIcon FrontIcon;
    @SideOnly(Side.CLIENT)
    private IIcon SideIcon;

    public DenseClay() {
        super(Material.clay);
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setBlockName("blockDenseClay");
        setBlockTextureName("clayium:denseclay");

        setHardness(1.0F);
        setResistance(1.0F);
        setStepSound(Block.soundTypeGravel);


        setHarvestLevel("shovel", 0);
    }


    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        int metadata = world.getBlockMetadata(x, y, z);
        metadata = (metadata - 2 + 1) % 4 + 2;
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        return true;
    }


    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {}


    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {}


    public int quantityDropped(int meta, int fortune, Random random) {
        return quantityDroppedWithBonus(fortune, random);
    }


    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(this);
    }


    public int quantityDropped(Random random) {
        return 36;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 1)
            return this.TopIcon;
        if (side == meta) {
            return this.FrontIcon;
        }
        return this.SideIcon;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.TopIcon = par1IconRegister.registerIcon("clayium:machinehull-0");
        this.FrontIcon = par1IconRegister.registerIcon("clayium:machinehull-1");
        this.SideIcon = par1IconRegister.registerIcon("clayium:denseclay");
    }


    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        int direction = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;

        if (direction == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (direction == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (direction == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (direction == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }

        if (itemstack.hasDisplayName()) ;
    }
}
