package mods.clayium.machine.ClayDistributor;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;

public class ClayDistributor extends ClayDirectionalNoRecipeMachine {

    public ClayDistributor(TierPrefix tier) {
        super(TileEntityClayDistributor::new, EnumMachineKind.distributor, GuiHandler.GuiIdClayDistributor, tier);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip." + EnumMachineKind.distributor.getRegisterName());
        super.addInformation(stack, player, tooltip, advanced);
    }
}
