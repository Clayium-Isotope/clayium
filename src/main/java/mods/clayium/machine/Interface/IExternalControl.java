package mods.clayium.machine.Interface;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface IExternalControl {

    IExternalControl NONE = new None();

    void doWorkOnce();

    /**
     * Mark as ready to work
     */
    void startWork();

    /**
     * Unmark as ready to work
     */
    void stopWork();

    /**
     * @return Is it ready to work
     */
    boolean isScheduled();

    /**
     * @return Is it working
     */
    boolean isDoingWork();

    static void update(IExternalControl marionette) {
        if (marionette.isScheduled()) {
            marionette.doWorkOnce();
        }
    }

    static IExternalControl cast(Object obj) {
        if (obj instanceof IExternalControl) return (IExternalControl) obj;
        return NONE;
    }

    class None implements IExternalControl {

        @Override
        public void doWorkOnce() {}

        @Override
        public void startWork() {}

        @Override
        public void stopWork() {}

        @Override
        public boolean isScheduled() {
            return false;
        }

        @Override
        public boolean isDoingWork() {
            return false;
        }
    }
}
