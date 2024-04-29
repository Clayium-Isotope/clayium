package mods.clayium.machine.ClayiumMachine;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClaySidedContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.JsonHelper;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;

public class ClayHorizontalNoRecipeMachine extends ClaySidedContainer {

    private final EnumMachineKind machineKind;

    public ClayHorizontalNoRecipeMachine(Class<? extends TileEntityGeneric> teClass, EnumMachineKind kind,
                                         String suffix, int guiId, TierPrefix tier) {
        super(Material.IRON, teClass, (suffix.isEmpty() ? (tier.getPrefix() + "_") : "") + kind.getRegisterName() +
                (suffix.isEmpty() ? "" : ("_" + suffix)), guiId, tier);
        this.machineKind = kind;

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

        JsonHelper.genItemJson(tier, kind, this.getRegistryName().getPath());
    }

    public ClayHorizontalNoRecipeMachine(Class<? extends TileEntityGeneric> teClass, EnumMachineKind kind,
                                         TierPrefix tier) {
        this(teClass, kind, GuiHandler.GuiIdNormalInventory, tier);
    }

    public ClayHorizontalNoRecipeMachine(Class<? extends TileEntityGeneric> teClass, EnumMachineKind kind, int guiId,
                                         TierPrefix tier) {
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
