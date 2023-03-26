package mods.clayium.machine.common;

import mods.clayium.item.common.IClayEnergy;
import mods.clayium.item.filter.IFilter;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UtilDirection;
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
    EnumFacing getFront();
    int getImportRoute(EnumSide side);
    int getExportRoute(EnumSide side);
    void setImportRoute(EnumSide side, int route);
    void setExportRoute(EnumSide side, int route);
    Map<EnumFacing, ItemStack> getFilters();
    NonNullList<ItemStack> getContainerItemStacks();

    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack) {
        return IClayInventory.isItemValidForSlot(this, index, stack);
    }

    static boolean isItemValidForSlot(IClayInventory inv, int index, ItemStack stack) {
        if (inv instanceof IClayEnergyConsumer && index == ((IClayEnergyConsumer) inv).getEnergySlot()) {
            return inv.acceptEnergyClay() && IClayEnergy.hasClayEnergy(stack)
                    && (((IClayEnergyConsumer) inv).getEnergyStack().isEmpty()
                    || ((IClayEnergyConsumer) inv).getEnergyStack().getCount() < inv.getClayEnergyStorageSize());
        }

        for (int[] slots : inv.getListSlotsImport()) {
            if (Arrays.stream(slots).anyMatch(e -> e == index))
                return true;
        }
        return false;
    }

    @Override
    default boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return IClayInventory.canInsertItem(this, index, itemStackIn, direction);
    }

    static boolean canInsertItem(IClayInventory inv, int index, ItemStack itemStackIn, EnumFacing direction) {
        if (inv.checkBlocked(itemStackIn, direction)) return false;
        EnumSide side = UtilDirection.getSideOfDirection(inv.getFront(), direction);

        int route = inv.getImportRoute(side);
        if (inv instanceof IClayEnergyConsumer && route == -2 && index == ((IClayEnergyConsumer) inv).getEnergySlot()) return inv.isItemValidForSlot(index, itemStackIn);

        if (route >= 0 && route < inv.getListSlotsImport().size()) {
            return Arrays.stream(inv.getListSlotsImport().get(route)).anyMatch(e -> e == index);
        }

        return false;
    }

    @Override
    default boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return IClayInventory.canExtractItem(this, index, itemStackIn, direction);
    }

    static boolean canExtractItem(IClayInventory inv, int index, ItemStack itemStackIn, EnumFacing direction) {
        if (inv.checkBlocked(itemStackIn, direction)) return false;
        EnumSide side = UtilDirection.getSideOfDirection(inv.getFront(), direction);

        int route = inv.getExportRoute(side);
        if (route >= 0 && route < inv.getListSlotsExport().size()) {
            return Arrays.stream(inv.getListSlotsExport().get(route)).anyMatch(e -> e == index);
        }

        return false;
    }

    default boolean checkBlocked(ItemStack itemStackIn, EnumFacing direction) {
        return IFilter.isFilter(this.getFilters().get(direction)) && !IFilter.match(this.getFilters().get(direction), itemStackIn);
    }

    @Override
    default int[] getSlotsForFace(EnumFacing side) {
        return IClayInventory.getSlotsForFace(this, side);
    }

    /**
     * int[] getAccessibleSlotsFromSide(int side)
     */
    static int[] getSlotsForFace(IClayInventory inv, EnumFacing facing) {
        EnumSide side = UtilDirection.getSideOfDirection(inv.getFront(), facing);
        Boolean[] flags = new Boolean[inv.getContainerItemStacks().size()];
        switch (inv.getImportRoute(side)) {
            case -2:
                if (inv instanceof IClayEnergyConsumer)
                    flags[((IClayEnergyConsumer) inv).getEnergySlot()] = true;
                break;
            case -1:
                break;
            default:
                for (int slot : inv.getListSlotsImport().get(inv.getImportRoute(side))) {
                    flags[slot] = true;
                }
        }

        if (inv.getExportRoute(side) != -1) {
            for (int slot : inv.getListSlotsExport().get(inv.getExportRoute(side))) {
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

    default void setImportRoutes(int[] routes) {
        for (int i = 0; i < EnumSide.VALUES.length; i++) {
            this.setImportRoute(EnumSide.getFront(i), routes[i]);
        }
    }

    default void setExportRoutes(int[] routes) {
        for (int i = 0; i < EnumSide.VALUES.length; i++) {
            this.setExportRoute(EnumSide.getFront(i), routes[i]);
        }
    }

    default void setImportRoutes(int d, int u, int f, int b, int l, int r) {
        this.setImportRoutes(new int[] {d, u, f, b, l, r});
    }

    default void setExportRoutes(int d, int u, int f, int b, int l, int r) {
        this.setExportRoutes(new int[] {d, u, f, b, l, r});
    }

    default void clearImportRoutes() {
        this.setImportRoutes(-1, -1, -1, -1, -1, -1);
    }

    default void clearExportRoutes() {
        this.setExportRoutes(-1, -1, -1, -1, -1, -1);
    }
}
