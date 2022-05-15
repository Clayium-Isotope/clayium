package mods.clayium.block;

import java.util.List;

import mods.clayium.util.UtilLocale;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class BlockResonator
        extends BlockDamaged
        implements ICAResonator {
    public BlockResonator(Material material) {
        super(material);
    }

    public BlockResonator() {
        this(Material.rock);
    }

    public BlockDamaged addResonance(double resonance) {
        return putInfo("Resonance", new Double(resonance));
    }

    public double getResonance(String blockname) {
        Object obj = getInfo(blockname, "Resonance");
        return (obj instanceof Double) ? ((Double) obj).doubleValue() : 1.0D;
    }


    public double getResonance(IBlockAccess world, int x, int y, int z) {
        return getResonance(getBlockName(world, x, y, z));
    }


    public List getTooltip(ItemStack itemStack) {
        List<String> ret = UtilLocale.localizeTooltip("tooltip.Resonator");
        ret.addAll(super.getTooltip(itemStack));
        if (UtilLocale.canLocalize("tooltip.Resonator.resonance")) {
            ret.add(UtilLocale.localizeAndFormat("tooltip.Resonator.resonance", new Object[] {Double.valueOf(getResonance(getBlockName(itemStack.getItemDamage())))}));
        }
        return ret;
    }
}
