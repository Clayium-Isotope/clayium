package mods.clayium.machine.ClayiumMachine;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClaySidedContainer;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.JsonHelper;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import mods.clayium.util.UtilTier;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClayiumMachine extends ClaySidedContainer {
    private final EnumMachineKind machineKind;

    public ClayiumMachine(EnumMachineKind kind, String suffix, int tier, Class<? extends TileEntityClayContainer> teClass, int guiID) {
        super(Material.IRON, teClass,
                suffix == null || suffix.equals("")
                        ? TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName()
                        : kind.getRegisterName() + "_" + suffix,
                guiID, tier);
        this.machineKind = kind;

        JsonHelper.genItemJson(TierPrefix.get(tier), kind, this.getRegistryName().getResourcePath());
    }

    public ClayiumMachine(EnumMachineKind kind, String suffix, int tier) {
        this(kind, suffix, tier, TileEntityClayiumMachine.class, GuiHandler.GuiIdClayMachines);
    }

    public ClayiumMachine(EnumMachineKind kind, int tier) {
        this(kind, null, tier);
    }

    public ClayiumMachine(EnumMachineKind kind, int tier, Class<? extends TileEntityClayContainer> teClass) {
        this(kind, null, tier, teClass, GuiHandler.GuiIdClayMachines);
    }

    @Override
    public TileGeneric createNewTileEntity(World worldIn, int meta) {
        TileGeneric tecc = super.createNewTileEntity(worldIn, meta);
        if (tecc instanceof TileEntityClayiumMachine) {
            ((TileEntityClayiumMachine) tecc).setKind(this.machineKind);
        }
        return tecc;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayiumMachine) {
            TileEntityClayiumMachine tecm = (TileEntityClayiumMachine) worldIn.getTileEntity(pos);
            tecm.importRoutes.replace(EnumFacing.UP, 0);
            if (!UtilTier.canManufactureCraft(this.tier))
                tecm.importRoutes.replace(placer.getHorizontalFacing(), -2);
            tecm.exportRoutes.replace(EnumFacing.DOWN, 0);
        }
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
