package mods.clayium.machine.common;

import mods.clayium.item.filter.IFilter;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UsedFor;
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
 * You can use these static methods even if the parent class modified the method(s).
 */
@UsedFor(UsedFor.Type.TileEntity)
public interface IClayInventory extends ISidedInventory {
    NonNullList<ItemStack> EMPTY_INV = NonNullList.withSize(0, ItemStack.EMPTY);
    int ENERGY_ROUTE = -2;
    int NONE_ROUTE = -1;

    List<int[]> getListSlotsImport();
    List<int[]> getListSlotsExport();
    EnumFacing getFront();
    int getImportRoute(EnumSide side);
    int getExportRoute(EnumSide side);
    void setImportRoute(EnumSide side, int route);
    void setExportRoute(EnumSide side, int route);
    Map<EnumFacing, ItemStack> getFilters();
    NonNullList<ItemStack> getContainerItemStacks();
    boolean getAutoInsert();
    boolean getAutoExtract();

    static boolean isItemValidForSlot(IClayInventory inv, int index, ItemStack stack) {
        if (inv instanceof IClayEnergyConsumer) {
            return IClayEnergyConsumer.isItemValidForSlot((IClayEnergyConsumer) inv, index, stack);
        }

        for (int[] slots : inv.getListSlotsImport()) {
            if (Arrays.stream(slots).anyMatch(e -> e == index))
                return true;
        }
        return false;
    }

    static boolean canInsertItem(IClayInventory inv, int index, ItemStack itemStackIn, EnumFacing direction) {
        if (inv.checkBlocked(itemStackIn, direction)) return false;
        EnumSide side = UtilDirection.getSideOfDirection(inv.getFront(), direction);

        int route = inv.getImportRoute(side);
        if (inv instanceof IClayEnergyConsumer && route == ENERGY_ROUTE && index == ((IClayEnergyConsumer) inv).getEnergySlot()) return inv.isItemValidForSlot(index, itemStackIn);

        if (route >= 0 && route < inv.getListSlotsImport().size()) {
            return Arrays.stream(inv.getListSlotsImport().get(route)).anyMatch(e -> e == index);
        }

        return false;
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

    /**
     * int[] getAccessibleSlotsFromSide(int side)
     */
    static int[] getSlotsForFace(IClayInventory inv, EnumFacing facing) {
        EnumSide side = UtilDirection.getSideOfDirection(inv.getFront(), facing);
        Boolean[] flags = new Boolean[inv.getContainerItemStacks().size()];
        switch (inv.getImportRoute(side)) {
            case ENERGY_ROUTE:
                if (inv instanceof IClayEnergyConsumer)
                    flags[((IClayEnergyConsumer) inv).getEnergySlot()] = true;
                break;
            case NONE_ROUTE:
                break;
            default:
                if (!inv.getListSlotsImport().isEmpty()) {
                    for (int slot : inv.getListSlotsImport().get(inv.getImportRoute(side))) {
                        flags[slot] = true;
                    }
                }
        }

        if (inv.getExportRoute(side) != NONE_ROUTE) {
            if (!inv.getListSlotsExport().isEmpty()) {
                for (int slot : inv.getListSlotsExport().get(inv.getExportRoute(side))) {
                    flags[slot] = true;
                }
            }
        }

        Collector<Boolean, ArrayList<Integer>, int[]> verifiedIndexJoiner = Collector.of(
                ArrayList::new,
                (list, flag) -> list.add(flag != null && flag ? list.size() : -1),
                (list, list1) -> { list.addAll(list1); return list; },
                list -> list.stream().mapToInt(e -> e).filter(e -> e != NONE_ROUTE).toArray()
        );

        return Arrays.stream(flags).collect(verifiedIndexJoiner);
    }

    /**
     * 0 more : Item
     * <br>-1 : none
     * <br>-2 : Energy Block
     */
    default void setImportRoutes(int d, int u, int f, int b, int l, int r) {
        this.setImportRoute(EnumSide.DOWN, d);
        this.setImportRoute(EnumSide.UP, u);
        this.setImportRoute(EnumSide.FRONT, f);
        this.setImportRoute(EnumSide.BACK, b);
        this.setImportRoute(EnumSide.LEFT, l);
        this.setImportRoute(EnumSide.RIGHT, r);
    }

    /**
     * 0 more : Item
     * <br>-1 : none
     */
    default void setExportRoutes(int d, int u, int f, int b, int l, int r) {
        this.setExportRoute(EnumSide.DOWN, d);
        this.setExportRoute(EnumSide.UP, u);
        this.setExportRoute(EnumSide.FRONT, f);
        this.setExportRoute(EnumSide.BACK, b);
        this.setExportRoute(EnumSide.LEFT, l);
        this.setExportRoute(EnumSide.RIGHT, r);
    }
}
