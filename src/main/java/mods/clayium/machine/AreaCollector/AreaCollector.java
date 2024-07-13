package mods.clayium.machine.AreaCollector;

import mods.clayium.machine.AreaMiner.AreaMiner;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class AreaCollector extends AreaMiner {
    public AreaCollector() {
        super(TierPrefix.claySteel, "area_collector", TileEntityAreaCollector::new, EnumMachineKind.areaCollector);
    }
}
