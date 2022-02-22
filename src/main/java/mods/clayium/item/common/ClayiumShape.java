package mods.clayium.item.common;

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
    gem("gem");

    ClayiumShape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;
}
