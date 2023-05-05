package mods.clayium.item.filter;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class FilterString extends FilterTemp {
    protected FilterString(String modelPath, int tier) {
        super(modelPath, tier);
    }

    @Override
    public boolean test(NBTTagCompound filterTag, ItemStack input) {
        return filterTag != null && this.filterStringMatch(filterTag.getString("FilterString"), input);
    }

    protected abstract boolean filterStringMatch(String filterString, ItemStack itemstack);

    protected static boolean checkMatch(String filterString, String input) {
        try {
            Pattern pattern = Pattern.compile(filterString);
            Matcher matcher = pattern.matcher(input);
            return matcher.find();
        } catch (PatternSyntaxException var7) {
            ClayiumCore.logger.error("Illegal Pattern! \n" + var7.getMessage());
            return false;
        }
    }

    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {
        player.openGui(ClayiumCore.instance(), GuiHandler.GuiIdItemFilterString, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public void addTooltip(NBTTagCompound filterTag, List<String> list, int indent) {
        if (list.size() > 100) return;

        if (filterTag == null) return;

        StringBuilder in = new StringBuilder();

        for(int i = 0; i < indent; ++i) {
            in.append(" ");
        }

        String filter = filterTag.getString("FilterString");
        if (!filter.equals("")) {
            list.add(in + filter);
        }
    }
}
