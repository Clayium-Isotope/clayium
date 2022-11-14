package mods.clayium.item.gadget;

import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public interface IGadget extends Comparator<ItemStack> {
    static boolean isGadget(ItemStack stack) {
        return stack.getItem() instanceof IGadget;
    }

    String getGroupName();

    void onApply(Entity entity, ItemStack gadget, boolean isRemote);
    void onTick(Entity entity, ItemStack gadget, boolean isRemote);
    void onReform(Entity entity, ItemStack before, ItemStack after, boolean isRemote);
    void onRemove(Entity entity, ItemStack gadget, boolean isRemote);

    int getMeta();
    @Override
    default int compare(ItemStack o1, ItemStack o2) {
        if (!UtilItemStack.areTypeEqual(o1, o2)) return 0;
        if (!(o1.getItem() instanceof IGadget) || !(o2.getItem() instanceof IGadget)) return 0;

        return Integer.compare(((IGadget) o1.getItem()).getMeta(), ((IGadget) o2.getItem()).getMeta());
    }
}
