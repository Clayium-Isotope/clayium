package mods.clayium.machine.ClayiumMachine;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClaySidedContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.TileEntityGeneric;
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
import java.util.function.Supplier;

public class ClayHorizontalNoRecipeMachine extends ClaySidedContainer {
    private final EnumMachineKind machineKind;

    public ClayHorizontalNoRecipeMachine(Supplier<? extends TileEntityGeneric> teSupplier, EnumMachineKind kind, TierPrefix tier, String modelPath, int guiId) {
        super(Material.IRON, teSupplier, modelPath, guiId, tier);
        this.machineKind = kind;

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

        JsonHelper.genItemJson(tier, kind, this.getRegistryName().getPath());
    }

    public ClayHorizontalNoRecipeMachine(Supplier<? extends TileEntityGeneric> teSupplier, EnumMachineKind kind, String suffix, int guiId, TierPrefix tier) {
        this(teSupplier, kind, tier, (suffix.isEmpty() ? (tier.getName() + "_") : "") + kind.getRegisterName() + (suffix.isEmpty() ? "" : ("_" + suffix)), guiId);
    }

    public ClayHorizontalNoRecipeMachine(Supplier<? extends TileEntityGeneric> teSupplier, EnumMachineKind kind, TierPrefix tier) {
        this(teSupplier, kind, GuiHandler.GuiIdNormalInventory, tier);
    }

    public ClayHorizontalNoRecipeMachine(Supplier<? extends TileEntityGeneric> teSupplier, EnumMachineKind kind, int guiId, TierPrefix tier) {
        this(teSupplier, kind, "", guiId, tier);
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
