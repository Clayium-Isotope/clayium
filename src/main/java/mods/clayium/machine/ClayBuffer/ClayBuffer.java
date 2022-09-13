package mods.clayium.machine.ClayBuffer;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClayBuffer extends ClayContainer {
    public ClayBuffer(int tier) {
        super(Material.CLAY, TileEntityClayBuffer.class, TierPrefix.get(tier).getPrefix() + "_" + EnumMachineKind.buffer.getRegisterName(), GuiHandler.clayBufferGuiID, tier);

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.buffer");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayBuffer) {
            ((TileEntityClayBuffer) worldIn.getTileEntity(pos)).importRoutes.replace(EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite(), 0);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
