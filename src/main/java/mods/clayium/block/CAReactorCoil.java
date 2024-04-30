package mods.clayium.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.itemblock.ItemBlockTierNamed;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;

public class CAReactorCoil extends BlockTiered {
    protected final int meta;

    public CAReactorCoil(int meta, TierPrefix tier) {
        super(Material.IRON, "ca_reactor_coil_", meta, tier);
        setHardness(8.0F);
        setResistance(5.0F);
        setSoundType(SoundType.METAL);
        this.meta = meta;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.ca_reactor_coil");
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTierNamed(this, "util.block.energy_storage_upgrade", argMark(this.meta));
    }

    protected static String argMark(int meta) {
        switch (meta) {
            case 0: return "Mk I";
            case 1: return "Mk II";
            case 2: return "Mk III";
            case 3: return "Mk IV";
            default: return "Mk -";
        }
    }
}
