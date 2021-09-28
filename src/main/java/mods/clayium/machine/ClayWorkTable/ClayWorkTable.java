
package mods.clayium.machine.ClayWorkTable;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiHandler;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ClayWorkTable extends BlockContainer implements ITieredBlock {
	private final int tier;

	public ClayWorkTable() {
		super(Material.ROCK);
		setUnlocalizedName("clay_work_table");
		setSoundType(SoundType.GROUND);
		setHarvestLevel("shovel", 0);
		setHardness(1F);
		setResistance(4F);
		setCreativeTab(ClayiumCore.tabClayium);
		setRegistryName(ClayiumCore.ModId, "clay_work_table");

		this.tier = 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityClayWorkTable();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityClayWorkTable tileEntity = (TileEntityClayWorkTable) world.getTileEntity(pos);
		if (tileEntity != null)
			InventoryHelper.dropInventoryItems(world, pos, tileEntity);
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing direction,
									float hitX, float hitY, float hitZ) {
		if (!world.isRemote && !player.isSneaking()) {
			player.openGui(ClayiumCore.instance(), GuiHandler.clayWorkTableGuiID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	public static void updateBlockState(World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(pos, tileentity);
		}
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
		UtilLocale.localizeTooltip(tooltip, this.getUnlocalizedName());
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return new ItemBlockTiered(this);
	}
}
