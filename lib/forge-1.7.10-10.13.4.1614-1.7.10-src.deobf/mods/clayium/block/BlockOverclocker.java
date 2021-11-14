package mods.clayium.block;

import java.util.List;

import mods.clayium.util.UtilLocale;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class BlockOverclocker
        extends BlockDamaged implements IOverclocker {
    public BlockOverclocker(Material material) {
        super(material);
    }

    public BlockOverclocker() {
        this(Material.rock);
    }

    public BlockDamaged addOverclockFactor(double resonance) {
        return putInfo("OverclockFactor", new Double(resonance));
    }

    public double getOverclockFactor(String blockname) {
        Object obj = getInfo(blockname, "OverclockFactor");
        return (obj instanceof Double) ? ((Double) obj).doubleValue() : 1.0D;
    }


    public double getOverclockFactor(IBlockAccess world, int x, int y, int z) {
        return getOverclockFactor(getBlockName(world, x, y, z));
    }


    public List getTooltip(ItemStack itemStack) {
        List<String> ret = UtilLocale.localizeTooltip("tooltip.Overclocker");
        ret.addAll(super.getTooltip(itemStack));
        if (UtilLocale.canLocalize("tooltip.Overclocker.overclockFactor")) {
            ret.add(UtilLocale.localizeAndFormat("tooltip.Overclocker.overclockFactor", new Object[] {Double.valueOf(getOverclockFactor(getBlockName(itemStack.getItemDamage())))}));
        }
        return ret;
    }
}
