package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ClayWorkTable
        extends BlockContainer
        implements ITieredBlock, ISpecialToolTip {
    @SideOnly(Side.CLIENT)
    private IIcon TopIcon;
    @SideOnly(Side.CLIENT)
    private IIcon SideIcon;
    private final Random random = new Random();

    public ClayWorkTable() {
        super(Material.rock);
    }


    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        world.markBlockForUpdate(x, y, z);
        player.openGui(ClayiumCore.INSTANCE, 0, world, x, y, z);
        return true;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.TopIcon = par1IconRegister.registerIcon("clayium:clayworktable");
        this.SideIcon = par1IconRegister.registerIcon("clayium:clayworktable_side");
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if (par1 == 0 || par1 == 1) {
            return this.TopIcon;
        }


        return this.SideIcon;
    }


    public static void updateBlockState(World world, int x, int y, int z) {
        TileEntity tileentity = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }


    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileClayWorkTable tileentityclayworktable = (TileClayWorkTable) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (tileentityclayworktable != null) {
            for (int i = 0; i < tileentityclayworktable.getSizeInventory(); i++) {
                ItemStack itemstack = tileentityclayworktable.getStackInSlot(i);
                if (itemstack != null) {

                    float f = this.random.nextFloat() * 0.6F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.6F + 0.1F;
                    float f2 = this.random.nextFloat() * 0.6F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j = this.random.nextInt(21) + 10;
                        if (j > itemstack.stackSize) {
                            j = itemstack.stackSize;
                        }
                        itemstack.stackSize -= j;
                        EntityItem entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                        float f3 = 0.025F;
                        entityitem.motionX = ((float) this.random.nextGaussian() * f3);
                        entityitem.motionY = ((float) this.random.nextGaussian() * f3 + 0.1F);
                        entityitem.motionZ = ((float) this.random.nextGaussian() * f3);
                        world.spawnEntityInWorld((Entity) entityitem);
                    }
                }
            }

            world.func_147453_f(x, y, z, block);


            tileentityclayworktable.releaseTicket();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }


    public TileEntity createNewTileEntity(World world, int par2) {
        return (TileEntity) new TileClayWorkTable();
    }


    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        onEntityWalking(world, x, y, z, entity);
    }


    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        TileClayWorkTable te = (TileClayWorkTable) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (entity instanceof IMerchant) {
            ClayiumCore.logger.info("entity instanceof IMerchant");
            te.merchant = (IMerchant) entity;
            te.setCurrentRecipeIndex(te.merchant.getRecipes(null).size() - 1);
        }
    }

    public int getTier(ItemStack itemstack) {
        return 0;
    }

    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 0;
    }


    public List getTooltip(ItemStack itemStack) {
        return UtilLocale.localizeTooltip(getUnlocalizedName() + ".tooltip");
    }
}
