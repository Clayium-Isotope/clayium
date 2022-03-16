package mods.clayium.machine.common;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ClayMachineTempTiered extends BlockContainer implements ITieredBlock {
    private final Class<? extends TileEntity> teClass;
    private final int guiID;
    protected final int tier;

    private Item itemBlock = new ItemBlockTiered(this);

    public ClayMachineTempTiered(Material materialIn, Class<? extends TileEntity> teClass, String modelPath, int guiID, int tier) {
        super(materialIn);

        setRegistryName(ClayiumCore.ModId, modelPath);
        setUnlocalizedName(modelPath);
        setCreativeTab(ClayiumCore.tabClayium);

        this.teClass = teClass;
        this.guiID = guiID;
        this.tier = tier;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (teClass == null) return null;

        try {
            return teClass.newInstance();
        } catch (Exception e) {
            ClayiumCore.logger.catching(e);
            return null;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntitySidedClayContainer tileEntity = (TileEntitySidedClayContainer) world.getTileEntity(pos);
        if (tileEntity != null)
            InventoryHelper.dropInventoryItems(world, pos, tileEntity);
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing direction,
                                    float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !player.isSneaking()) {
            player.openGui(ClayiumCore.instance(), guiID, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public int getTier(ItemStack stackIn) {
        return tier;
    }

    @Override
    public int getTier(IBlockAccess access, BlockPos posIn) {
        return tier;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, getUnlocalizedName());
    }

    public Item getItemBlock() {
        return this.itemBlock;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(new ItemBlockTiered(this));
    }

    public Class<? extends TileEntity> getTileEntity() {
        return teClass;
    }
}
