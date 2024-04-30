package mods.clayium.item.gadget;

import java.util.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilItemStack;

public class GadgetHolder extends ItemTiered {

    /**
     * Key: each entity
     * Value:
     * Key: name of gadget kind
     * Value: applied gadget
     */
    protected static Map<Entity, Map<String, ItemStack>> appliedGadgets = new HashMap<>();
    protected static Map<Entity, Map<String, ItemStack>> clientAppliedGadgets = new HashMap<>();

    public GadgetHolder() {
        super("gadget_holder", TierPrefix.unknown);

        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.openGui(ClayiumCore.instance(), GuiHandler.GuiIdGadgetHolder, worldIn, (int) playerIn.posX,
                (int) playerIn.posY, (int) playerIn.posZ);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onUpdate(ItemStack gadgetHolder, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        for (ItemStack gadget : getGadgetMap(entityIn, worldIn.isRemote).values()) {
            if (IGadget.isGadget(gadget)) {
                ((IGadget) gadget.getItem()).onTick(entityIn, gadget, worldIn.isRemote);
            }
        }
    }

    // TODO for回しすぎなので要リファクタリング。そこまで気にしなくていいかもね
    public static void loadGadgets(List<ItemStack> holdingGadgets, Entity entityIn, World worldIn) {
        Map<String, ItemStack> latestMap = getGadgetMap(entityIn, worldIn.isRemote);
        Map<String, ItemStack> newMap = new HashMap<>();

        for (ItemStack stack : holdingGadgets) {
            if (IGadget.isGadget(stack)) {
                IGadget kind = (IGadget) stack.getItem();
                if (!newMap.containsKey(kind.getGroupName()) ||
                        kind.compare(stack, newMap.get(kind.getGroupName())) > 0)
                    newMap.put(kind.getGroupName(), stack);
            }
        }

        setGadgetMap(entityIn, worldIn.isRemote, newMap);

        Set<String> removes = latestMap.keySet();
        Set<String> reforms = new HashSet<>();
        Set<String> applies = new HashSet<>();

        for (Map.Entry<String, ItemStack> entry : newMap.entrySet()) {
            if (removes.contains(entry.getKey())) {
                removes.remove(entry.getKey());
                if (!UtilItemStack.areItemDamageTagEqual(latestMap.get(entry.getKey()), newMap.get(entry.getKey()))) {
                    reforms.add(entry.getKey());
                }
            } else {
                applies.add(entry.getKey());
            }
        }

        for (String removeKey : removes) {
            ItemStack stack = latestMap.get(removeKey);
            if (IGadget.isGadget(stack)) {
                ((IGadget) stack.getItem()).onRemove(entityIn, stack, worldIn.isRemote);
                ClayiumCore.logger.info("Removed: " + ((IGadget) stack.getItem()).getGroupName());
            }
        }

        for (String reformKey : reforms) {
            ItemStack stack = newMap.get(reformKey);
            if (IGadget.isGadget(stack)) {
                ((IGadget) stack.getItem()).onReform(entityIn, latestMap.get(reformKey), stack, worldIn.isRemote);
                ClayiumCore.logger.info("Reformed: " + ((IGadget) stack.getItem()).getGroupName());
            }
        }

        for (String applyKey : applies) {
            ItemStack stack = newMap.get(applyKey);
            if (IGadget.isGadget(stack)) {
                ((IGadget) stack.getItem()).onApply(entityIn, stack, worldIn.isRemote);
                ClayiumCore.logger.info("Applied: " + ((IGadget) stack.getItem()).getGroupName());
            }
        }
    }

    protected static Map<Entity, Map<String, ItemStack>> getGadgetMap(boolean isWorldRemote) {
        return isWorldRemote ? appliedGadgets : clientAppliedGadgets;
    }

    protected static Map<String, ItemStack> getGadgetMap(Entity entity, boolean isRemote) {
        Map<Entity, Map<String, ItemStack>> gadgetMap = getGadgetMap(isRemote);
        if (!gadgetMap.containsKey(entity)) {
            gadgetMap.put(entity, new HashMap<>());
        }

        return gadgetMap.get(entity);
    }

    protected static void setGadgetMap(Entity entity, boolean isRemote, Map<String, ItemStack> newMap) {
        Map<Entity, Map<String, ItemStack>> gadgetMap = getGadgetMap(isRemote);
        gadgetMap.put(entity, newMap);
    }
}
