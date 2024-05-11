package mods.clayium.machine.ClayDistributor;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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
