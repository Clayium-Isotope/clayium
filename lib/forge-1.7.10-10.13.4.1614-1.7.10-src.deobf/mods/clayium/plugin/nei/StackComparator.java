package mods.clayium.plugin.nei;

import codechicken.nei.PositionedStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class StackComparator implements Comparator<List<PositionedStack>> {
    public int compare(List<PositionedStack> la, List<PositionedStack> lb) {
        if (la == null && lb == null) return 0;
        if (lb == null) return 1;
        if (la == null) return -1;
        if (la.size() > lb.size()) return 1;
        if (lb.size() > la.size()) return -1;
        int[] aa = getMaxId(la);
        int[] bb = getMaxId(lb);
        if (aa[0] > bb[0]) return 1;
        if (bb[0] > aa[0]) return -1;
        if (aa[1] > bb[1]) return 1;
        if (bb[1] > aa[1]) return -1;
        if (aa[2] > bb[2]) return 1;
        if (bb[2] > aa[2]) return -1;
        return 0;
    }

    private int[] getMaxId(List<PositionedStack> a) {
        int id = Integer.MIN_VALUE;
        int damage = Integer.MIN_VALUE;
        int stacksize = Integer.MIN_VALUE;
        for (PositionedStack s : a) {
            int[] m = getMin(s.items);
            if (m[0] > id || (m[0] == id && m[1] > damage) || (m[0] == id && m[1] == damage && m[2] > stacksize)) {
                id = m[0];
                damage = m[1];
                stacksize = m[2];
            }
        }
        return new int[] {id, damage, stacksize};
    }

    private int[] getMin(ItemStack[] a) {
        int id = Integer.MAX_VALUE;
        int damage = Integer.MAX_VALUE;
        int stacksize = Integer.MAX_VALUE;
        for (ItemStack item : a) {
            int id1 = Item.getIdFromItem(item.getItem());
            int damage1 = item.getItemDamage();
            int stacksize1 = item.stackSize;
            if (id1 < id
                    || (id1 == id && damage1 < damage)
                    || (id1 == id && damage1 == damage && stacksize1 < stacksize)) {
                id = id1;
                damage = damage1;
                stacksize = stacksize1;
            }
        }
        return new int[] {id, damage, stacksize};
    }
}
