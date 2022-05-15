package mods.clayium.block.tile;

public class TileClayOpenPitMarker extends TileClayMarker {
    public void activate() {
        super.activate();
        if (this.aabb != null)
            this.aabb.minY = 1.0D;
    }

    public static class TileClayGroundLevelingMarker
            extends TileClayMarker {
        public void activate() {
            super.activate();
            if (this.aabb != null)
                this.aabb.maxY = 255.0D;
        }
    }

    public static class TileClayPrismMarker
            extends TileClayMarker {
        public void activate() {
            super.activate();
            if (this.aabb != null) {
                this.aabb.minY = 1.0D;
                this.aabb.maxY = 255.0D;
            }
        }
    }
}
