package mods.clayium.block;

import mods.clayium.block.tile.TileEntityClayContainer;
import mods.clayium.block.tile.TileGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class ClayContainer extends BlockContainer /* implements IOverridableBlock */ {
    private final Class<? extends TileEntity> teClass;
    private final int guiId;

    public ClayContainer(Material material, Class<? extends TileEntity> teClass, String modelPath, int guiId) {
        super(material);
        this.teClass = teClass;
        this.guiId = guiId;

        setUnlocalizedName(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);

        GameRegistry.registerTileEntity(teClass, new ResourceLocation(ClayiumCore.ModId, modelPath));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityClayContainer te = (TileEntityClayContainer) worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn, pos, te);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (teClass == null) {
            return null;
        }

        try {
            return teClass.newInstance();
        } catch (Exception var4) {
            ClayiumCore.logger.catching(var4);
            return null;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            if (!playerIn.isSneaking()) {
                TileEntity te = worldIn.getTileEntity(pos);

                if (te != null) {
                    if (te instanceof TileGeneric) {
                        ItemStack stack = playerIn.getActiveItemStack();
                        if (((TileGeneric) te).isUsable(stack, playerIn, facing, hitX, hitY, hitZ)) {
                            ((TileGeneric) te).useItem(stack, playerIn, facing, hitX, hitY, hitZ);
                            return true;
                        }
                    }

                    playerIn.openGui(ClayiumCore.instance(), guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
        return true;
    }

    public static void updateBlockState(World worldIn, BlockPos posIn) {
        TileEntity tileentity = worldIn.getTileEntity(posIn);

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(posIn, tileentity);
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, this.getUnlocalizedName());
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (world.getTileEntity(pos) != null
                && ((TileGeneric) world.getTileEntity(pos)).hasSpecialDrops()) {
            ((TileGeneric)world.getTileEntity(pos)).getDrops(drops, world, pos, state, fortune);
        } else {
            super.getDrops(drops, world, pos, state, fortune);
        }

        if (world instanceof World) {
            ((World) world).removeTileEntity(pos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return new ItemBlock(this);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(new ItemBlock(this));
    }
}
