package mods.clayium.machine.ClayContainer;

import mods.clayium.item.common.IClayEnergy;
import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

/**
 * NOT NECESSARILY
 *
 * You can use even if the super class modified the method(s).
 *
 * <br><br>Note: example for using default isItemValidForSlot(),
 * <pre>
 * ((IClayInventory) this).isItemValidForSlot(int index, ItemStack stack);
 * </pre>
 */
public interface IClayInventory extends ISidedInventory {
    default boolean acceptEnergyClay() {
        return true;
    }
    default int getClayEnergyStorageSize() {
        return 1;
    }
    List<int[]> getListSlotsImport();
    List<int[]> getListSlotsExport();
    Map<EnumFacing, Integer> getImportRoutes();
    Map<EnumFacing, Integer> getExportRoutes();
    Map<EnumFacing, ItemStack> getFilters();
    NonNullList<ItemStack> getContainerItemStacks();

    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack) {
        if (this instanceof IClayEnergyConsumer && index == ((IClayEnergyConsumer) this).getEnergySlot()) {
            return acceptEnergyClay() && IClayEnergy.hasClayEnergy(stack)
                    && (((IClayEnergyConsumer) this).getEnergyStack().isEmpty()
                    || ((IClayEnergyConsumer) this).getEnergyStack().getCount() < this.getClayEnergyStorageSize());
        }

        for (int[] slots : this.getListSlotsImport()) {
            if (Arrays.stream(slots).anyMatch(e -> e == index))
                return true;
        }
        return false;
    }

    @Override
    default boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        int route = this.getImportRoutes().get(direction);
        if (this instanceof IClayEnergyConsumer && route == -2 && index == ((IClayEnergyConsumer) this).getEnergySlot()) return isItemValidForSlot(index, itemStackIn);

        if (route >= 0 && route < this.getListSlotsImport().size()) {
            return Arrays.stream(this.getListSlotsImport().get(route)).anyMatch(e -> e == index);
        }

        return false;
    }

    @Override
    default boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        int route = this.getExportRoutes().get(direction);
        if (route >= 0 && route < this.getListSlotsExport().size()) {
            return Arrays.stream(this.getListSlotsExport().get(route)).anyMatch(e -> e == index);
        }

        return false;
    }

    default boolean checkBlocked(ItemStack itemStackIn, EnumFacing direction) {
        return IFilter.isFilter(this.getFilters().get(direction)) && !IFilter.match(this.getFilters().get(direction), itemStackIn);
    }

    /**
     * int[] getAccessibleSlotsFromSide(int side)
     */
    @Override
    default int[] getSlotsForFace(EnumFacing side) {
        Boolean[] flags = new Boolean[this.getContainerItemStacks().size()];
        switch (this.getImportRoutes().get(side)) {
            case -2:
                if (this instanceof IClayEnergyConsumer)
                    flags[((IClayEnergyConsumer) this).getEnergySlot()] = true;
                break;
            case -1:
                break;
            default:
                for (int slot : this.getListSlotsImport().get(this.getImportRoutes().get(side))) {
                    flags[slot] = true;
                }
        }

        if (this.getExportRoutes().get(side) != -1) {
            for (int slot : this.getListSlotsExport().get(this.getExportRoutes().get(side))) {
                flags[slot] = true;
            }
        }

        Collector<Boolean, ArrayList<Integer>, int[]> verifiedIndexJoiner = Collector.of(
                ArrayList::new,
                (list, flag) -> list.add(flag != null && flag ? list.size() : -1),
                (list, list1) -> { list.addAll(list1); return list; },
                list -> list.stream().mapToInt(e -> e).filter(e -> e != -1).toArray()
        );

        return Arrays.stream(flags).collect(verifiedIndexJoiner);
    }

    default void setImportRoute(EnumFacing facing, int route) {
        this.getImportRoutes().put(facing, route);
    }

    default void setExportRoute(EnumFacing facing, int route) {
        this.getExportRoutes().put(facing, route);
    }
}
