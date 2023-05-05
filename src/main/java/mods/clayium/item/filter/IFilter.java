package mods.clayium.item.filter;

import mods.clayium.util.UsedFor;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@UsedFor(UsedFor.Type.Item)
public interface IFilter extends BiPredicate<NBTTagCompound, ItemStack> {
    boolean test(NBTTagCompound filterTag, ItemStack input);

    static boolean match(ItemStack filter, ItemStack input) {
        return isFilter(filter) && ((IFilter) filter.getItem()).test(filter.getTagCompound(), input);
    }

    static boolean isFilter(ItemStack filter) {
        return !filter.isEmpty() && filter.getItem() instanceof IFilter;
    }

    void addTooltip(NBTTagCompound filterTag, List<String> list, int indent);

    default boolean isCopy(ItemStack filter) {
        return filter.getItemDamage() == 1;
    }

    default ItemStack setCopyFlag(ItemStack filter) {
        filter.setItemDamage(1);
        return filter;
    }

    default void addFilterInformation(ItemStack itemstack, EntityPlayer player, List<String> tooltip, boolean flag) {
        addTooltip(itemstack.getTagCompound(), tooltip, 0);
    }

    static boolean matchBetweenItemstacks(ItemStack filter, ItemStack itemstack, boolean fuzzy) {
        if (filter.isEmpty()) {
            return itemstack.isEmpty();
        }
        if (!fuzzy) {
            return !itemstack.isEmpty() && UtilItemStack.areTypeEqual(filter, itemstack);
        }

        if (itemstack.isEmpty()) {
            return false;
        }

        return UtilItemStack.areItemDamageEqualOrDamageable(filter, itemstack) || UtilItemStack.haveSameOD(filter, itemstack);
    }

    static Predicate<ItemStack> getFilterPredicate(ItemStack filter) {
        if (filter.isEmpty()) {
            return ItemStack::isEmpty;
        }

        if (IFilter.isFilter(filter)) {
            return stack -> IFilter.match(filter, stack);
        }

        return stack -> UtilItemStack.areTypeEqual(filter, stack);
    }
}
