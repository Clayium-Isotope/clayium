package mods.clayium.machine.Interface;

import javax.annotation.Nullable;

public interface ISynchronizedInterface {
    void setCoreBlock(@Nullable IInterfaceCaptive tile);

    IInterfaceCaptive getCore();

    boolean isSynced();

    boolean isSyncEnabled();
    boolean markEnableSync();
}
