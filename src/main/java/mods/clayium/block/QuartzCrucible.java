package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileQuartzCrucible;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class QuartzCrucible
        extends BlockContainer
        implements ITieredBlock, ISpecialToolTip {
    public QuartzCrucible() {
        super(Material.glass);
        setStepSound(Block.soundTypeGlass);
        setHardness(0.2F);
        setResistance(0.2F);
    }


    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0675F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        float f = 0.0675F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 0.75F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, f);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 0.75F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        setBlockBoundsForItemRender();
    }


    public boolean isOpaqueCube() {
        return false;
    }


    public int getRenderType() {
        return ClayiumCore.quartzCrucibleRenderId;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = Blocks.quartz_block.getIcon(0, 0);
    }


    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityItem) {
            ItemStack itemStack = ((EntityItem) entity).getEntityItem();
            ItemStack ingot = CMaterials.get(CMaterials.IMPURE_SILICON, CMaterials.INGOT);
            ItemStack string = new ItemStack(Items.string);
            TileQuartzCrucible tile = (TileQuartzCrucible) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);

            if (entity.posY - y < 0.20000000298023224D) {
                if (UtilItemStack.areTypeEqual(itemStack, ingot)) {


                    if (tile.putIngot() &&
                            --itemStack.stackSize <= 0) entity.setDead();

                }


                if (UtilItemStack.areTypeEqual(itemStack, string)) {


                    if (tile.consumeString() &&
                            --itemStack.stackSize <= 0) entity.setDead();

                }
            }
        }
    }


    public int getTier(ItemStack itemstack) {
        return 5;
    }


    public TileEntity createNewTileEntity(World world, int par2) {
        return (TileEntity) new TileQuartzCrucible();
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 5;
    }


    public List getTooltip(ItemStack itemStack) {
        return UtilLocale.localizeTooltip(getUnlocalizedName() + ".tooltip");
    }
}
