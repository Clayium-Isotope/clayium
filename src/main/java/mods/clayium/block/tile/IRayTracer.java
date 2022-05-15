package mods.clayium.block.tile;

import mods.clayium.entity.RayTraceMemory;

public interface IRayTracer {
    void setRayTraceMemory(RayTraceMemory paramRayTraceMemory);

    boolean acceptRayTraceMemory(RayTraceMemory paramRayTraceMemory);

    RayTraceMemory getRayTraceMemory();
}
