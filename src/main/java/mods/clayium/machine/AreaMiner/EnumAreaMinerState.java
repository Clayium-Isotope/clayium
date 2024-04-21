package mods.clayium.machine.AreaMiner;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum EnumAreaMinerState implements IStringSerializable {
    BEFORE_INIT,
    OnceMode_Ready,
    OnceMode_Doing,
    Idle,
    LoopMode_Ready,
    LoopMode_Doing;

    public int meta() {
        return this.ordinal() - 1;
    }

    public static EnumAreaMinerState getByMeta(int meta) {
        switch (meta) {
            case -1: return BEFORE_INIT;
            case 0: return OnceMode_Ready;
            case 1: return OnceMode_Doing;
            case 2: return Idle;
            case 3: return LoopMode_Ready;
            case 4: return LoopMode_Doing;
        }
        throw new EnumConstantNotPresentException(EnumAreaMinerState.class, "[default]");
    }

    @Nonnull
    @Override
    public String getName() {
        switch (this) {
            case OnceMode_Ready: return "once_rdy";
            case OnceMode_Doing: return "once_wip";
            case Idle: return "idle";
            case LoopMode_Ready: return "loop_rdy";
            case LoopMode_Doing: return "loop_wip";
            default: return "bug_if_you_see_this";
        }
    }
}
