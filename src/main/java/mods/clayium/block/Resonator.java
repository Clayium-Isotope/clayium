package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.common.IItemBlockHolder;
import mods.clayium.block.itemblock.ItemBlockTierNamed;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.CAMachine.ResonanceHandler;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Resonator extends BlockTiered implements CAResonator, IItemBlockHolder {
    static final int RESONATE_RANGE = ResonanceHandler.RESONATE_RANGE;
    private final double resonance;

    public Resonator(int meta, TierPrefix tier, double resonance) {
        super(Material.IRON, "resonator_", meta, tier);
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.resonance = resonance;
    }

    @Override
    public double getResonance(IBlockAccess world, BlockPos pos) {
        return this.getResonance();
    }

    public double getResonance() {
        return this.resonance;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.resonator");
        super.addInformation(stack, player, tooltip, advanced);
        if (UtilLocale.canLocalize("tooltip.Resonator.resonance")) {
            tooltip.add(UtilLocale.localizeAndFormat("tooltip.Resonator.resonance", this.getResonance()));
        }
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTierNamed(this, "util.block.resonator", UtilLocale.localizeAndFormat(TierPrefix.getLocalizeKey(this.tier)));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        noticeRecalcResonance(worldIn, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        noticeRecalcResonance(worldIn, pos);
    }

    public static void noticeRecalcResonance(World worldIn, BlockPos pos) {
        for (BlockPos offset : BlockPos.getAllInBox(-RESONATE_RANGE, -RESONATE_RANGE, -RESONATE_RANGE, RESONATE_RANGE, RESONATE_RANGE, RESONATE_RANGE)) {
            TileEntity te = worldIn.getTileEntity(pos.add(offset));
            if (te != null && te.hasCapability(ClayiumCore.RESONANCE_CAPABILITY, null)) {
                te.getCapability(ClayiumCore.RESONANCE_CAPABILITY, null).markRecalcResonance();
            }
        }
    }
}
