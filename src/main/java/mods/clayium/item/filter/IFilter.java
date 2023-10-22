package mods.clayium.item.filter;

import mods.clayium.item.ClayiumItems;
import mods.clayium.util.UsedFor;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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

    /**
     * Check {@code filter} is a instance which copied by {@code this}
     */
    boolean isCopy(ItemStack filter);

    ItemStack setCopyFlag(ItemStack filter);

    default void addFilterInformation(ItemStack itemstack, EntityPlayer player, List<String> tooltip, boolean flag) {
        addTooltip(itemstack.getTagCompound(), tooltip, 0);
    }

    static boolean matchBetweenItemstacks(ItemStack filter, ItemStack itemstack, boolean fuzzy) {
        if (filter.isEmpty()) {
            return itemstack.isEmpty();
        }
        if (!fuzzy) {
            return !itemstack.isEmpty() && UtilItemStack.areItemDamageTagEqual(filter, itemstack);
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

        return stack -> UtilItemStack.areItemDamageTagEqual(filter, stack);
    }

    static ItemStack getMockWhitelist(final ItemStack sample) {
        if (sample.isEmpty()) return ItemStack.EMPTY;

        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList items = new NBTTagList();

        NBTTagCompound item = new NBTTagCompound();
        item.setByte("Slot", (byte) 0);
        sample.writeToNBT(item);

        items.appendTag(item);
        tag.setTag("Items", items);

        tag.setBoolean("is_mock", true);

        ItemStack stack = new ItemStack(ClayiumItems.filterWhitelist);
        stack.setTagCompound(tag);
        return stack;
    }
}
