package mods.clayium.machine.AreaCollector;

import mods.clayium.component.bot.AreaBotCollector;
import mods.clayium.component.bot.LocalBotIdle;
import mods.clayium.machine.AreaMiner.EnumAreaMinerState;
import mods.clayium.machine.AreaMiner.TileEntityAreaWorker;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;

import java.util.stream.IntStream;

public class TileEntityAreaCollector extends TileEntityAreaWorker {
    @Override
    public void initParamsByTier(TierPrefix tier) {
        this.areaBot.set(new AreaBotCollector());
        this.localBot.set(new LocalBotIdle());
        this.botOutput.set(UtilTransfer.getItemHandler(this, null, IntStream.range(0, 9).toArray()));
        super.initParamsByTier(tier);
        this.setState(EnumAreaMinerState.LoopMode_Ready);
    }
}
