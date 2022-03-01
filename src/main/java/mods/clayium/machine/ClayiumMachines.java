package mods.clayium.machine;

import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.common.ClayMachineTempTiered;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;

public class ClayiumMachines {
    public static final Map<EnumMachineKind, Map<TierPrefix, ClayMachineTempTiered>> machineMap = new HashMap<>();
    static {
        for (EnumMachineKind kind : EnumMachineKind.values()) {
            machineMap.put(kind, new HashMap<>());
        }

        /* Tier 0... */
        machineMap.get(EnumMachineKind.workTable).put(TierPrefix.none, new ClayWorkTable());
        machineMap.get(EnumMachineKind.craftingTable).put(TierPrefix.none, new ClayCraftingTable());
        /* ...Tier 0 */

        /* Tier 1... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.millingMachine,
                EnumMachineKind.waterWheel
        }) {
            machineMap.get(kind).put(TierPrefix.clay, new ClayiumMachine(kind, 1));
        }
        /* ...Tier 1 */

        /* Tier 2... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.grinder,
                EnumMachineKind.decomposer,
                EnumMachineKind.condenser,

                EnumMachineKind.waterWheel
        }) {
            machineMap.get(kind).put(TierPrefix.denseClay, new ClayiumMachine(kind, 2));
        }
        /* ...Tier 2 */

        /* Tier 3... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.grinder,
                EnumMachineKind.decomposer,
                EnumMachineKind.condenser,

                EnumMachineKind.centrifuge,
                EnumMachineKind.inscriber,
                EnumMachineKind.assembler,
                EnumMachineKind.millingMachine
        }) {
            machineMap.get(kind).put(TierPrefix.simple, new ClayiumMachine(kind, 3));
        }
        machineMap.get(EnumMachineKind.ECCondenser).put(TierPrefix.simple, new ClayiumMachine(EnumMachineKind.ECCondenser, "mk1", 3));
        /* ...Tier 3 */

        /* Tier 4... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.grinder,
                EnumMachineKind.decomposer,
                EnumMachineKind.condenser,

                EnumMachineKind.centrifuge,
                EnumMachineKind.inscriber,
                EnumMachineKind.assembler,
                EnumMachineKind.millingMachine
        }) {
            machineMap.get(kind).put(TierPrefix.basic, new ClayiumMachine(kind, 4));
        }
        machineMap.get(EnumMachineKind.ECCondenser).put(TierPrefix.basic, new ClayiumMachine(EnumMachineKind.ECCondenser, "mk2", 4));
        /* ...Tier 4 */
    }

    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        return machineMap.get(kind).get(tier);
    }
    public static Block get(EnumMachineKind kind, int tier) {
        return get(kind, TierPrefix.get(tier));
    }
}
