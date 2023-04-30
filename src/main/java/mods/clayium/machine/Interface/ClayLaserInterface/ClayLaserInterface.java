package mods.clayium.machine.Interface.ClayLaserInterface;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayDirectionalContainer;
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

public class ClayLaserInterface extends ClayDirectionalContainer {
    public ClayLaserInterface(int tier) {
        super(Material.IRON, TileEntityClayLaserInterface.class, TierPrefix.get(tier).getPrefix() + "_" + EnumMachineKind.laserInterface.getRegisterName(), GuiHandler.GuiIdClayInterface, tier);
        this.setSoundType(SoundType.METAL);
        this.setHardness(2.0f);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public boolean canBePipe() {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        UtilLocale.localizeTooltip(tooltip, "tooltip." + EnumMachineKind.laserInterface.getRegisterName());
    }
}
