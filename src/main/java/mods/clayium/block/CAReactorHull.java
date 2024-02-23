package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.itemblock.ItemBlockTierNamed;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CAReactorHull extends BlockTiered {
    protected final int meta;

    public CAReactorHull(int meta, TierPrefix tier) {
        super(Material.IRON, "ca_reactor_hull_", meta, tier);

        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setHardness(4.0F);
        setResistance(25.0F);

        this.meta = meta;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, "tooltip.ca_reactor_hull", argMark(this.meta));
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTierNamed(this, "util.block.ca_reactor_hull", this.meta);
    }

    protected static String argMark(int meta) {
        switch (meta) {
            case 0:
            case 1: return "Mk I";
            case 2:
            case 3:
            case 4:
            case 5: return "Mk II";
            case 6:
            case 7:
            case 8:
            case 9: return "Mk III";
            default: return "Mk -";
        }
    }
}
