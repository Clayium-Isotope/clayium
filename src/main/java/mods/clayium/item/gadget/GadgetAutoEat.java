package mods.clayium.item.gadget;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.filter.IFilter;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class GadgetAutoEat extends GadgetTemp /* implements IFilter */ {
    private final boolean isEconomical;

    public GadgetAutoEat(int meta, int tier) {
        super("gadget_auto_eat_" + meta, meta, tier);
        this.isEconomical = meta == 0;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.openGui(ClayiumCore.instance(), GuiHandler.GuiIdGadgetAutoEat, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public String getGroupName() {
        return "GadgetAutoEat";
    }

    @Override
    public void onApply(Entity entity, ItemStack gadget, boolean isRemote) {}

    @Override
    public void onTick(Entity entity, ItemStack gadget, boolean isRemote) {
        if (entity instanceof EntityPlayer && !isRemote && ((EntityPlayer) entity).getFoodStats().needFood()) {
            EntityPlayer player = (EntityPlayer) entity;
            NonNullList<ItemStack> inventory = player.inventory.mainInventory;
            int currentFood = player.getFoodStats().getFoodLevel();
            float currentSat = player.getFoodStats().getSaturationLevel();

            boolean bestTiming = true;
            float bestRate = 0.0f; // ratio of replenish (food + saturation) point, actual / inherent
            int bestIndex = -1; // index of the food considered best

            for (int i = 0; i < inventory.size(); i++) {
                if (!inventory.get(i).isEmpty() && this.filterMatch(gadget, inventory.get(i)) && inventory.get(i).getItem() instanceof ItemFood) {
                    int tempFood = currentFood;
                    float tempSat = currentSat;
                    int foodPoint = ((ItemFood) inventory.get(i).getItem()).getHealAmount(inventory.get(i));
                    float saturation = ((ItemFood) inventory.get(i).getItem()).getSaturationModifier(inventory.get(i));
                    float immediateFood = (float)Math.min(foodPoint, 20 - currentFood) + Math.min(saturation, (float)Math.min(currentFood + foodPoint, 20) - currentSat);
                    boolean mayBest = true;

                    if (this.isEconomical) {
                        while (tempSat > 0.0f || tempFood > 0) {
                            if (tempSat > 0.0f) {
                                --tempSat;
                            }
                            if (tempSat <= 0.0f) {
                                tempSat = 0.0f;
                                --tempFood;
                            }

                            float ecoConsidered = (float)Math.min(foodPoint, 20 - tempFood) + Math.min(saturation, (float)Math.min(tempFood + foodPoint, 20) - tempSat);
                            if (ecoConsidered > immediateFood) {
                                immediateFood = ecoConsidered;
                                mayBest = false;
                            }
                        }
                    }

                    if (bestRate < immediateFood / (foodPoint + saturation)) {
                        bestRate = immediateFood / (foodPoint + saturation);
                        bestIndex = i;
                        bestTiming = mayBest;
                    }
                }
            }

            if (bestIndex >= 0 && bestTiming) {
                inventory.get(bestIndex).onItemUseFinish(player.world, player);
                if (inventory.get(bestIndex).isEmpty()) {
                    inventory.set(bestIndex, ItemStack.EMPTY);
                }

                player.inventory.markDirty();
            }
        }
    }

    @Override
    public void onReform(Entity entity, ItemStack before, ItemStack after, boolean isRemote) {}

    @Override
    public void onRemove(Entity entity, ItemStack gadget, boolean isRemote) {}

    public boolean filterMatch(ItemStack gadget, ItemStack input) {
        if (!gadget.hasTagCompound() || !gadget.getTagCompound().hasKey("Items", Constants.NBT.TAG_LIST)) {
            return true;
        }

        List<ItemStack> filters = UtilItemStack.tagList2ItemList(gadget.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND));
        if (filters.isEmpty()) {
            return true;
        }

        boolean ret = true;
        for (ItemStack filter : filters) {
            ret &= filter.isEmpty();
            if (IFilter.isFilter(filter) && IFilter.match(filter, input) || IFilter.matchBetweenItemstacks(filter, input, false)) {
                return true;
            }
        }

        return ret;
    }
}
