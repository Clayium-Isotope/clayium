package mods.clayium.item.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;

public class ItemFilterOreDict
        extends ItemFilterString {
    public boolean filterStringMatch(String filterString, ItemStack itemstack) {
        String filter = filterString;
        String[] orenames = UtilItemStack.getOreNames(itemstack);
        for (String orename : orenames) {

            try {
                Pattern pattern = Pattern.compile(filter);
                Matcher matcher = pattern.matcher(orename);
                if (matcher.find())
                    return true;
            } catch (PatternSyntaxException e) {
                ClayiumCore.logger.error("Illegal Pattern! \n" + e.getMessage());
                return false;
            }
        }
        return false;
    }
}
