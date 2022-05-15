package mods.clayium.item.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.ItemStack;


public class ItemFilterItemDamage
        extends ItemFilterString {
    public boolean filterStringMatch(String filterString, ItemStack itemstack) {
        String filter = "^" + filterString + "$";
        if (itemstack == null || itemstack.getItem() == null) return false;
        String name = String.valueOf(itemstack.getItemDamage());

        try {
            Pattern pattern = Pattern.compile(filter);
            Matcher matcher = pattern.matcher(name);
            return matcher.find();
        } catch (PatternSyntaxException e) {
            ClayiumCore.logger.error("Illegal Pattern! \n" + e.getMessage());
            return false;
        }
    }
}
