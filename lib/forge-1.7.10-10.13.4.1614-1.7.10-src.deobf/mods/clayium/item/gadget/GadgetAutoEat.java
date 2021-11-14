package mods.clayium.item.gadget;

import java.util.Arrays;
import java.util.List;

import mods.clayium.item.CItems;
import mods.clayium.item.IItemGadget;
import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GadgetAutoEat
        implements IItemGadget {
    public List<String> itemNames;

    public GadgetAutoEat(String... itemNames) {
        this.itemNames = Arrays.asList(itemNames);
    }

    public GadgetAutoEat() {
        this(new String[] {"AutoEat0", "AutoEat1"});
    }


    public void update(List<ItemStack> list, Entity entity, boolean isRemote) {
        for (ItemStack item : list) {
            if (item != null && item.getItem() == CItems.itemGadget) {
                update(this.itemNames.indexOf(CItems.itemGadget.getItemName(item)), item, entity, isRemote);
            }
        }
    }


    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack) {
        int i = 0;
        if (filterTag == null)
            return true;
        ItemStack[] filters = UtilItemStack.tagList2Items(filterTag.getTagList("Items", 10));
        if (filters == null)
            return true;
        boolean ret = true;
        for (ItemStack filter : filters) {
            i = ret & (filter == null) ? 1 : 0;
            if ((ItemFilterTemp.isFilter(filter) && ItemFilterTemp.match(filter, itemstack)) ||
                    ItemFilterTemp.matchBetweenItemstacks(filter, itemstack, false)) {
                return true;
            }
        }
        return i == 1;
    }

    public void update(int itemIndex, ItemStack item, Entity entity, boolean isRemote) {
        if (itemIndex >= 0 && entity instanceof EntityPlayer && !isRemote && ((EntityPlayer) entity).getFoodStats().needFood()) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack[] inv = player.inventory.mainInventory;
            int pHeal = player.getFoodStats().getFoodLevel();
            float pSat = player.getFoodStats().getSaturationLevel();
            NBTTagCompound filterTag = item.getTagCompound();

            boolean bestTiming = true;
            float bestRate = 0.0F;
            int bestIndex = -1;
            for (int i = 0; i < inv.length; i++) {
                if (inv[i] != null && filterMatch(filterTag, inv[i]) && inv[i].getItem() instanceof ItemFood) {
                    int tHeal = pHeal;
                    float tSat = pSat;
                    int heal = ((ItemFood) inv[i].getItem()).func_150905_g(inv[i]);
                    float sat = ((ItemFood) inv[i].getItem()).func_150906_h(inv[i]);
                    float val0 = Math.min(heal, 20 - tHeal) + Math.min(sat, Math.min(tHeal + heal, 20) - tSat);
                    boolean best0 = true;
                    if (itemIndex == 0) {
                        while (tSat > 0.0F || tHeal > 0) {
                            if (tSat > 0.0F)
                                tSat--;
                            if (tSat <= 0.0F) {
                                tSat = 0.0F;
                                tHeal--;
                            }
                            float val1 = Math.min(heal, 20 - tHeal) + Math.min(sat, Math.min(tHeal + heal, 20) - tSat);
                            if (val1 > val0) {
                                val0 = val1;
                                best0 = false;
                            }
                        }
                    }
                    if (bestRate < val0 / (heal + sat)) {
                        bestRate = val0 / (heal + sat);
                        bestIndex = i;
                        bestTiming = best0;
                    }
                }
            }
            if (bestIndex >= 0 && bestTiming) {
                inv[bestIndex].onFoodEaten(entity.worldObj, player);
                if (inv[bestIndex] != null && (inv[bestIndex]).stackSize == 0)
                    inv[bestIndex] = null;
                player.inventory.markDirty();
            }
        }
    }


    public boolean match(ItemStack itemStack, World world, Entity entity, int slot, boolean isCurrentItem) {
        if (itemStack != null && itemStack.getItem() == CItems.itemGadget) {
            return this.itemNames.contains(CItems.itemGadget.getItemName(itemStack));
        }
        return false;
    }
}
