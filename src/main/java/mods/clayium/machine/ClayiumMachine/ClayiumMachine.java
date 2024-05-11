package mods.clayium.machine.ClayiumMachine;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClaySidedContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.JsonHelper;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ClayiumMachine extends ClaySidedContainer {
    private final EnumMachineKind machineKind;

    public ClayiumMachine(EnumMachineKind kind, String suffix, TierPrefix tier, Supplier<? extends TileEntityGeneric> teSupplier, int guiID) {
        super(Material.IRON, teSupplier,
                suffix == null || suffix.equals("")
                        ? tier.getPrefix() + "_" + kind.getRegisterName()
                        : kind.getRegisterName() + "_" + suffix,
                guiID, tier);
        this.machineKind = kind;

        JsonHelper.genItemJson(tier, kind, this.getRegistryName().getPath());
    }

    public ClayiumMachine(EnumMachineKind kind, String suffix, TierPrefix tier) {
        this(kind, suffix, tier, TileEntityClayiumMachine::new, GuiHandler.GuiIdClayMachines);
    }

    public ClayiumMachine(EnumMachineKind kind, TierPrefix tier) {
        this(kind, null, tier);
    }

    public ClayiumMachine(EnumMachineKind kind, TierPrefix tier, Supplier<? extends TileEntityGeneric> teSupplier) {
        this(kind, null, tier, teSupplier, GuiHandler.GuiIdClayMachines);
    }

    @Override
    public TileEntityGeneric createNewTileEntity(World worldIn, int meta) {
        TileEntityGeneric tecc = super.createNewTileEntity(worldIn, meta);
        if (tecc instanceof TileEntityClayiumMachine) {
            ((TileEntityClayiumMachine) tecc).setKind(this.machineKind);
        }
        return tecc;
    }

    public static void updateBlockState(World world, BlockPos pos) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(pos, tileentity);
        }
    }

    public EnumMachineKind getMachineKind() {
        return machineKind;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        UtilLocale.localizeTooltip(tooltip, "tooltip." + machineKind.getRegisterName());
    }
}
