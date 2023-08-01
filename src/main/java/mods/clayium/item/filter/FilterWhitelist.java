package mods.clayium.item.filter;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiHandler;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class FilterWhitelist extends FilterTemp {
    protected final boolean fuzzy;

    public FilterWhitelist() {
        this("filter_whitelist", TierPrefix.advanced, false);
    }

    public FilterWhitelist(String modelPath, TierPrefix tier) {
        this(modelPath, tier, false);
    }

    public FilterWhitelist(String modelPath, TierPrefix tier, boolean fuzzy) {
        super(modelPath, tier);
        this.fuzzy = fuzzy;
    }

    @Override
    public boolean test(NBTTagCompound filterTag, ItemStack input) {
        for (ItemStack stack : UtilItemStack.getItemsFromTag(filterTag)) {
            if (IFilter.isFilter(stack) && IFilter.match(stack, input)
                    || IFilter.matchBetweenItemstacks(stack, input, this.fuzzy))
                return true;
        }

        return false;
    }

    @Override
    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {
        player.openGui(ClayiumCore.instance(), GuiHandler.GuiIdItemFilterWhitelist, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public void addTooltip(NBTTagCompound filterTag, List<String> list, int indent) {
        if (list.size() > 100) return;

        if (filterTag == null) return;

        StringBuilder in = new StringBuilder(indent);

        for (int i = 0; i < indent; ++i) {
            in.append(" ");
        }

        for (ItemStack filter : UtilItemStack.getItemsFromTag(filterTag)) {
            if (filter.isEmpty()) continue;

            list.add(in + filter.getDisplayName());
            if (IFilter.isFilter(filter)) {
                ((IFilter) filter.getItem()).addTooltip(filter.getTagCompound(), list, indent + 1);
            }
        }
    }
}
