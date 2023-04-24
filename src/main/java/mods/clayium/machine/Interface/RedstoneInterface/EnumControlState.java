package mods.clayium.machine.Interface.RedstoneInterface;

import net.minecraft.util.IStringSerializable;

public enum EnumControlState implements IStringSerializable {
    None("None", "none"),
    EmitIfIdle("Emit if idle", "emit_if_idle"),
    EmitIfWorkScheduled("Emit if work scheduled", "emit_if_work_scheduled"),
    EmitIfDoingWork("Emit if doing work", "emit_if_doing_work"),
    DoWork("Do work", "do_work"),
    DoNotWork("Do not work", "do_not_work"),
    StartWork("Start work", "start_work"),
    StopWork("Stop work", "stop_work"),
    DoWorkOnce("Do work once", "do_work_once");

    private final String message;
    private final String name;

    EnumControlState(String message, String name) {
        this.message = message;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
