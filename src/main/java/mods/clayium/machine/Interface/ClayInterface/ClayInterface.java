package mods.clayium.machine.Interface.ClayInterface;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.JsonHelper;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClayInterface extends ClayContainer {
    private final EnumMachineKind kind;

    public ClayInterface(int tier) {
        this(EnumMachineKind.clayInterface, TileEntityClayInterface.class, tier);
    }

    public ClayInterface(EnumMachineKind kind, Class<? extends TileEntityClayInterface> teClass, int tier) {
        super(Material.IRON, teClass, TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName(), GuiHandler.GuiIdClayInterface, tier);
        this.setSoundType(SoundType.METAL);
        this.setHardness(2.0f);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 0);

        this.kind = kind;

        JsonHelper.genBlockJsonParent(TierPrefix.get(tier), kind, TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName(), "interface_temp");
        JsonHelper.genItemJsonSimple(TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName());
        JsonHelper.genStateJsonSimple(TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName());
    }

    @Override
    public boolean canBePipe() {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        UtilLocale.localizeTooltip(tooltip, "tooltip." + this.kind.getRegisterName());
    }
}
