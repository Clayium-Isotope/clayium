package mods.clayium.machine.ClayBuffer;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClayBuffer extends ClayContainer {
    public ClayBuffer(TierPrefix tier) {
        super(Material.CLAY, TileEntityClayBuffer::new, tier.getName() + "_" + EnumMachineKind.buffer.getRegisterName(), GuiHandler.GuiIdNormalInventory, tier);

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
}
