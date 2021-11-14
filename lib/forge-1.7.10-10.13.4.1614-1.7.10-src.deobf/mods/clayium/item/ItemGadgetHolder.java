package mods.clayium.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGadgetHolder
        extends ItemTiered {
    protected static List<IItemGadget> gadgetList = new ArrayList<IItemGadget>();
    protected static Map<Entity, Map<IItemGadget, List<ItemStack>>> gadgetMapRemote = new HashMap<Entity, Map<IItemGadget, List<ItemStack>>>();
    protected static Map<Entity, Map<IItemGadget, List<ItemStack>>> gadgetMapServer = new HashMap<Entity, Map<IItemGadget, List<ItemStack>>>();
    protected static final String tagName = "Items";

    public static boolean addGadget(IItemGadget gadget) {
        return gadgetList.add(gadget);
    }

    public static List<IItemGadget> getGadgetList() {
        return gadgetList;
    }

    protected static Map<Entity, Map<IItemGadget, List<ItemStack>>> getGadgetMap(boolean theWorldIsRemote) {
        return theWorldIsRemote ? gadgetMapRemote : gadgetMapServer;
    }

    protected static Map<IItemGadget, List<ItemStack>> getGadgetMap(Entity entity, boolean isRemote) {
        Map<Entity, Map<IItemGadget, List<ItemStack>>> gadgetMap = getGadgetMap(isRemote);
        if (!gadgetMap.containsKey(entity)) {
            gadgetMap.put(entity, initGadgetList(null));
        }
        return gadgetMap.get(entity);
    }


    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        p_77659_3_.openGui(ClayiumCore.INSTANCE, 23, p_77659_2_, (int) p_77659_3_.posX, (int) p_77659_3_.posY, (int) p_77659_3_.posZ);
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isCurrentItem) {
        if (world == null)
            return;
        ItemStack[] items = UtilItemStack.getItemsFromTag("Items", itemStack);
        if (items == null)
            return;
        Map<IItemGadget, List<ItemStack>> gadgetMap = getGadgetMap(entity, world.isRemote);
        if (gadgetMap == null) {
            return;
        }

        for (IItemGadget gadget : getGadgetList()) {
            List<ItemStack> list = gadgetMap.get(gadget);
            for (ItemStack item : items) {
                if (gadget.match(item, world, entity, slot, isCurrentItem)) {
                    list.add(item);
                }
            }
        }
        for (ItemStack item : items) {
            if (item != null && item.getItem() != null && !(item.getItem() instanceof ItemGadgetHolder)) {
                item.getItem().onUpdate(item, world, entity, slot, isCurrentItem);
            }
        }
    }

    public static void clearGadgetList(Entity entity, boolean isRemote) {
        Map<IItemGadget, List<ItemStack>> gadgetMap = getGadgetMap(entity, isRemote);
        if (gadgetMap == null) {
            return;
        }
        for (List<ItemStack> list : gadgetMap.values())
            list.clear();
    }

    public static void updateGadget(Entity entity, boolean isRemote) {
        Map<IItemGadget, List<ItemStack>> gadgetMap = getGadgetMap(entity, isRemote);
        if (gadgetMap == null) {
            return;
        }
        for (IItemGadget gadget : getGadgetList()) {
            List<ItemStack> list = gadgetMap.get(gadget);
            gadget.update(list, entity, isRemote);
        }
    }

    public static Map<IItemGadget, List<ItemStack>> initGadgetList(Map<IItemGadget, List<ItemStack>> gadgetMap) {
        if (gadgetMap == null) {
            gadgetMap = new HashMap<IItemGadget, List<ItemStack>>();
        }
        for (IItemGadget gadget : gadgetList) {
            gadgetMap.put(gadget, new ArrayList<ItemStack>());
        }
        return gadgetMap;
    }
}
