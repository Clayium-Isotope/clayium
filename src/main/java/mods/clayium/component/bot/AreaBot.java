package mods.clayium.component.bot;

import mods.clayium.machine.ClayMarker.AABBHolder;

/**
 * 広域に対して作用する bot
 */
public interface AreaBot extends GeneralBot<LocalBot>, AABBHolder {
    /**
     * AABBを持っていることはいつでも確認するべき。
     */
    @Override
    default boolean isReady() {
        return this.hasAxisAlignedBB();
    }

    /**
     * 一連の動作の後に、終了する({@code true})のか、
     * 継続して動作する準備をする({@code false})のかを設定する。
     */
    void setFinite(boolean willTerminate);
}
