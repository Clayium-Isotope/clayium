package mods.clayium.machine.Interface.ClayInterface;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.TileEntityGeneric;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

@HasOriginalState
public class ClayInterface extends ClayContainer {
    protected final EnumMachineKind kind;

    public ClayInterface(TierPrefix tier) {
        this(EnumMachineKind.clayInterface, TileEntityClayInterface::new, tier);

//        JsonHelper.genBlockJsonParent(TierPrefix.get(tier), kind, TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName(), "interface_temp");
//        JsonHelper.genItemJsonSimple(TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName());
//        JsonHelper.genStateJsonSimple(TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName());
    }

    public ClayInterface(EnumMachineKind kind, Supplier<? extends TileEntityGeneric> teSupplier, TierPrefix tier) {
        super(Material.IRON, teSupplier, tier.getName() + "_" + kind.getRegisterName(), GuiHandler.GuiIdClayInterface, tier);
        this.setSoundType(SoundType.METAL);
        this.setHardness(2.0f);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 0);

        this.kind = kind;
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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() instanceof IModifyCC) return false;

        if (worldIn.isRemote) return true;

        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        TileEntityClayInterface tile = (TileEntityClayInterface) worldIn.getTileEntity(pos);
        if (tile == null || !tile.isSynced() || !(tile.core instanceof TileEntityClayContainer)) return false;

        TileEntityClayContainer coreTile = (TileEntityClayContainer) tile.core;

        coreTile.getWorld().markBlockRangeForRenderUpdate(coreTile.getPos(), coreTile.getPos());
        openGui(coreTile.getWorld(), coreTile.getPos(), playerIn);
        return true;
    }
}
