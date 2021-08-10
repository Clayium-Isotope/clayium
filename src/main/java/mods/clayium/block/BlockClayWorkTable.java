
package mods.clayium.block;

import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClayWorkTable extends Block implements ITileEntityProvider {
	public BlockClayWorkTable() {
		super(Material.CLAY);
		setUnlocalizedName("clay_work_table");
		setSoundType(SoundType.GROUND);
		setHarvestLevel("shovel", 0);
		setHardness(1F);
		setResistance(4F);
		setCreativeTab(ClayiumCore.tabClayium);
		setRegistryName(ClayiumCore.ModId, "clay_work_table");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileClayWorkTable();
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileClayWorkTable)
			InventoryHelper.dropInventoryItems(world, pos, (TileClayWorkTable) tileentity);
		world.removeTileEntity(pos);
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing direction,
									float hitX, float hitY, float hitZ) {
		super.onBlockActivated(world, pos, state, entity, hand, direction, hitX, hitY, hitZ);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if (entity != null) {
			entity.openGui(ClayiumCore.instance(), GuiHandler.GuiClayWorkTableId, world, x, y, z);
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
}
