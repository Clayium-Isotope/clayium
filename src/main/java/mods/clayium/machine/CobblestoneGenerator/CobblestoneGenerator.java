package mods.clayium.machine.CobblestoneGenerator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.machine.ClayContainer.ClayDirectionalContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CobblestoneGenerator extends ClayDirectionalContainer {
    public CobblestoneGenerator(int tier) {
        super(Material.IRON, TileEntityCobblestoneGenerator.class, TierPrefix.get(tier).getPrefix() + "_" + EnumMachineKind.cobblestoneGenerator.getRegisterName(), GuiHandler.GuiIdNormalInventory, tier);

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayBuffer) {
            ((TileEntityClayBuffer) worldIn.getTileEntity(pos)).exportRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer), 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
