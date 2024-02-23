package mods.clayium.item.common;

import java.util.Arrays;
import java.util.List;

public enum ClayiumShape {
    plate("plate"),
    stick("stick"),
    shortStick("short_stick"),
    ring("ring"),
    smallRing("small_ring"),
    gear("gear"),
    blade("blade"),
    needle("needle"),
    disc("disc"),
    smallDisc("small_disc"),
    cylinder("cylinder"),
    pipe("pipe"),
    largeBall("large_ball"),
    largePlate("large_plate"),
    grindingHead("grinding_head"),
    bearing("bearing"),
    spindle("spindle"),
    cuttingHead("cutting_head"),
    waterWheel("water_wheel_vane"),
    block("block"),
    ball("ball"),
    dust("dust"),
    ingot("ingot"),
    gem("gem"),
    crystal("crystal");

    ClayiumShape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;

    public static ClayiumShape fromName(String name) {
        for (ClayiumShape shape : ClayiumShape.values()) {
            if (shape.getName().equals(name)) {
                return shape;
            }
        }
        return null;
    }

    public static String getLocalizeKey(ClayiumShape shape) {
        return "util.shape." + shape.name;
    }

    public static final List<ClayiumShape> clayPartShapes = Arrays.asList(
            plate, stick, shortStick, ring, smallRing, gear, blade, needle, disc, smallDisc,
            cylinder, pipe, largeBall, largePlate, grindingHead, bearing, spindle, cuttingHead, waterWheel, block,
            ball, dust);

    public static final List<ClayiumShape> metalPartShapes = Arrays.asList(
            plate, largePlate, block, dust, ingot, gem
    );
}
