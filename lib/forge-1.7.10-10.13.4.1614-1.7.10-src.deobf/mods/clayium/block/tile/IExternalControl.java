package mods.clayium.block.tile;

public interface IExternalControl {
    void doWorkOnce();

    void startWork();

    void stopWork();

    boolean isScheduled();

    boolean isDoingWork();
}
