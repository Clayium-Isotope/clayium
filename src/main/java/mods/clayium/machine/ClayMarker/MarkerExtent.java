package mods.clayium.machine.ClayMarker;

import net.minecraft.util.math.AxisAlignedBB;

import java.util.function.UnaryOperator;

public enum MarkerExtent implements UnaryOperator<AxisAlignedBB> {
    None(UnaryOperator.identity()),
    OpenPit($ -> $.offset(0, 1 - $.minY, 0)),
    GroundLeveling($ -> $.setMaxY(255)),
    Prism($ -> new AxisAlignedBB($.minX, 1, $.minZ, $.maxX, 255, $.maxZ));

    private final UnaryOperator<AxisAlignedBB> offset;

    MarkerExtent(UnaryOperator<AxisAlignedBB> offset) {
        this.offset = offset;
    }

    public AxisAlignedBB apply(AxisAlignedBB aabb) {
        return this.offset.apply(aabb);
    }
}
