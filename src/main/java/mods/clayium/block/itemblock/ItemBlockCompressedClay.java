package mods.clayium.block.itemblock;

import mods.clayium.block.CompressedClay;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockCompressedClay extends ItemBlockTiered implements IClayEnergy {
    private final int meta;

    public ItemBlockCompressedClay(CompressedClay block) {
        super(block);

        meta = block.getTier(ItemStack.EMPTY);
    }

    @Override
    public long getClayEnergy() {
        if (meta < 4) return 0L;
        return (long) Math.pow(10.0D, this.getTier());
    }

    @Override
    public int getTier() {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(UtilLocale.getLocalizedText("gui.Common.energy", UtilLocale.ClayEnergyNumeral(getClayEnergy())));
    }
}
