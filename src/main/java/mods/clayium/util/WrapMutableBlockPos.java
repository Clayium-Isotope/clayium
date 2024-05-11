package mods.clayium.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class WrapMutableBlockPos extends BlockPos.MutableBlockPos {
    public void setX(int xIn) {
        this.x = xIn;
    }

//    @Override
//    public void setY(int yIn) {
//        this.y = yIn;
//    }

    public void setZ(int zIn) {
        this.z = zIn;
    }

    public void incrX() {
        this.x++;
    }

    public void incrY() {
        this.y++;
    }

    public void incrZ() {
        this.z++;
    }

    public void decrX() {
        this.x--;
    }

    public void decrY() {
        this.y--;
    }

    public void decrZ() {
        this.z--;
    }

    public void addAndSet(Vec3i vec) {
        this.setPos(this.add(vec));
    }
}
