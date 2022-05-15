package mods.clayium.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockFluidTab extends BlockContainer {
    public BlockFluidTab() {
        super(Material.wood);
        setStepSound(Block.soundTypeWood);
        setHardness(1.0F);
    }


    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();

        TileFluidTab tile = (TileFluidTab) UtilBuilder.safeGetTileEntity((IBlockAccess) par1World, par2, par3, par4);


        if (tile != null) {


            FluidStack fluid = tile.productTank.getFluid();

            if (itemstack == null) {


                String s = "";

                if (fluid != null && fluid.getFluid() != null) {

                    s = "Fluid current in the tab : " + fluid.getFluid().getLocalizedName(fluid);
                } else {

                    s = "No fluid in the tab";
                }


                if (!par1World.isRemote) par5EntityPlayer.addChatMessage((IChatComponent) new ChatComponentText(s));

                return true;
            }


            FluidStack fluid2 = FluidContainerRegistry.getFluidForFilledItem(itemstack);


            if (fluid2 != null && fluid2.getFluid() != null) {


                int put = tile.fill(ForgeDirection.UNKNOWN, fluid2, false);


                if (put == fluid2.amount) {

                    tile.fill(ForgeDirection.UNKNOWN, fluid2, true);


                    ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(itemstack);
                    if (emptyContainer != null) {
                        if (!par5EntityPlayer.inventory.addItemStackToInventory(emptyContainer.copy())) {
                            par5EntityPlayer.entityDropItem(emptyContainer.copy(), 1.0F);
                        }
                    }


                    if (!par5EntityPlayer.capabilities.isCreativeMode && itemstack.stackSize-- <= 0) {
                        par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack) null);
                    }


                    tile.markDirty();
                    par5EntityPlayer.inventory.markDirty();
                    par1World.markBlockForUpdate(par2, par3, par4);


                    par1World.playSoundAtEntity((Entity) par5EntityPlayer, "random.pop", 0.4F, 1.8F);

                    return true;
                }

            } else {

                if (fluid != null && fluid.getFluid() != null) {

                    if (fluid.amount < 1000) return true;


                    ItemStack get = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid.getFluid(), 1000), itemstack);

                    if (get != null) {


                        tile.drain(ForgeDirection.UNKNOWN, 1000, true);


                        if (!par5EntityPlayer.inventory.addItemStackToInventory(get.copy())) {
                            par5EntityPlayer.entityDropItem(get.copy(), 1.0F);
                        }


                        if (!par5EntityPlayer.capabilities.isCreativeMode && itemstack.stackSize-- <= 0) {
                            par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack) null);
                        }


                        tile.markDirty();
                        par5EntityPlayer.inventory.markDirty();
                        par1World.markBlockForUpdate(par2, par3, par4);


                        par1World.playSoundAtEntity((Entity) par5EntityPlayer, "random.pop", 0.4F, 1.8F);
                    }

                    return true;
                }


                return true;
            }
        }


        return true;
    }


    public TileEntity createNewTileEntity(World world, int a) {
        return new TileFluidTab();
    }


    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        float f = 0.0675F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setBlockBoundsForItemRender();
    }


    public boolean isOpaqueCube() {
        return false;
    }


    public int getRenderType() {
        return ClayiumCore.fluidTabRenderId;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = Blocks.planks.getIcon(0, 0);
    }
}
