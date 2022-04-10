package mods.clayium.machine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.common.ClayMachineTempTiered;
import net.minecraft.block.Block;

import java.util.*;

public class ClayiumMachines {
    public static final Map<EnumMachineKind, Map<TierPrefix, ClayMachineTempTiered>> machineMap = new EnumMap<>(EnumMachineKind.class);

    public static void registerMachines() {
        add(EnumMachineKind.workTable, 0, new ClayWorkTable());
        add(EnumMachineKind.craftingTable, 0, new ClayCraftingTable());

        add(EnumMachineKind.bendingMachine, new int[] { 1, 2, 3, 4, 5, 6, 7, 9 });
        add(EnumMachineKind.wireDrawingMachine, new int[] { 1, 2, 3, 4 });
        add(EnumMachineKind.pipeDrawingMachine, new int[] { 1, 2, 3, 4 });
        add(EnumMachineKind.cuttingMachine, new int[] { 1, 2, 3, 4 });
        add(EnumMachineKind.lathe, new int[] { 1, 2, 3, 4 });

        add(EnumMachineKind.millingMachine, new int[] { 1, 3, 4 });
    }

    private static void add(EnumMachineKind kind, int tier, ClayMachineTempTiered block) {
        TierPrefix _tier = TierPrefix.get(tier);

        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(_tier)) {
            ClayiumCore.logger.error("The machine already exists  [" + kind.getRegisterName() + "] [" + _tier.getPrefix() + "]");
            return;
        }

        machineMap.get(kind).put(_tier, block);
    }

    private static void add(EnumMachineKind kind, int tier) {
        add(kind, tier, new ClayiumMachine(kind, tier));
    }
    private static void add(EnumMachineKind kind, int[] tiers) {
        for (int i : tiers)
            add(kind, i, new ClayiumMachine(kind, i));
    }
    private static void add(EnumMachineKind kind, int tier, String suffix) {
        add(kind, tier, new ClayiumMachine(kind, suffix, tier));
    }

    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        return machineMap.get(kind).get(tier);
    }
    public static Block get(EnumMachineKind kind, int tier) {
        return get(kind, TierPrefix.get(tier));
    }

    public static List<Block> getBlocks() {
        List<Block> res = new ArrayList<>();

        for (Map<TierPrefix, ClayMachineTempTiered> kind : machineMap.values()) {
            res.addAll(kind.values());
        }

        return res;
    }
}
