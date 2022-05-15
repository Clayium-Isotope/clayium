package mods.clayium.item.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;


public class ItemFilterUniqueID
        extends ItemFilterString {
    public boolean filterStringMatch(String filterString, ItemStack itemstack) {
        String filter = filterString;

        if (itemstack == null || itemstack.getItem() == null)
            return false;
        String name = UtilItemStack.findUniqueIdentifierFor(itemstack.getItem()).toString();

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
