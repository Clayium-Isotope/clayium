package mods.clayium.machine.ClayiumMachine;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClaySidedContainer;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
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

public class ClayHorizontalNoRecipeMachine extends ClaySidedContainer {
    private final EnumMachineKind machineKind;

    public ClayHorizontalNoRecipeMachine(Class<? extends TileEntityClayContainer> teClass, EnumMachineKind kind, String suffix, int guiId, int tier) {
        super(Material.IRON, teClass, (suffix.isEmpty() ? (TierPrefix.get(tier).getPrefix() + "_") : "") + kind.getRegisterName() + (suffix.isEmpty() ? "" : ("_" + suffix)), guiId, tier);
        this.machineKind = kind;

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

        JsonHelper.genItemJson(TierPrefix.get(tier), kind, this.getRegistryName().getResourcePath());
    }

    public ClayHorizontalNoRecipeMachine(Class<? extends TileEntityClayContainer> teClass, EnumMachineKind kind, int tier) {
        this(teClass, kind, GuiHandler.GuiIdNormalInventory, tier);
    }

    public ClayHorizontalNoRecipeMachine(Class<? extends TileEntityClayContainer> teClass, EnumMachineKind kind, int guiId, int tier) {
        this(teClass, kind, "", guiId, tier);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        UtilLocale.localizeTooltip(tooltip, "tooltip." + this.machineKind.getRegisterName());
    }

    @Override
    public TileEntityGeneric createNewTileEntity(World worldIn, int meta) {
        TileEntityGeneric te = super.createNewTileEntity(worldIn, meta);
        if (te instanceof TileEntityClayiumMachine) {
            ((TileEntityClayiumMachine) te).setKind(this.machineKind);
        }
        return te;
    }
}
