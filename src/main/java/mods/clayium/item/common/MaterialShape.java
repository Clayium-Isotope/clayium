package mods.clayium.item.common;

import java.util.Arrays;
import java.util.List;

public class MaterialShape {
    public static class Material {
        Material(String name, String ODName) {
            this(name, ODName, 1.0F);
        }

        Material(String name, String ODName, float hardness) {
            this.name = name;
            this.ODName = ODName;
            this.hardness = hardness;
        }

        public String getName() {
            return name;
        }
        public String getODName() {
            return ODName;
        }
        public float getHardness() {
            return hardness;
        }

        public static List<Material> values() {
            return null;
        }

        private final String name;
        private final String ODName;
        private final float hardness;
    }

    public static class GenericMaterial extends Material {
        public static final Material aluminum = new GenericMaterial("aluminum", "Aluminum").setID(13).setColor(190, 200, 202).setColor(255, 255, 255, 2);
        public static final Material antimatter = new GenericMaterial("antimatter", "Antimatter").setID(800).setColor(0, 0, 235).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
        public static final Material pureAntimatter = new GenericMaterial("pure_antimatter", "PureAntimatter").setID(801).setColor(255, 50, 255).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
        public static final GenericMaterial[] compressedCureAntimatter = new GenericMaterial[9];
        static {
            for (int i = 0; i <= 8; i++) {
                switch (i) {
                    case 0:
                        compressedCureAntimatter[i] = (GenericMaterial) pureAntimatter;
                    default:
                        compressedCureAntimatter[i] = new GenericMaterial("pure_antimatter_" + i, "pureAntimatter" + i).setID(801 + i);
                    case 8:
                        compressedCureAntimatter[i] = new GenericMaterial("octuple_pure_antimatter", "OctuplePureAntimatter").setID(801 + i);
                }
                double r = i / 8.0D;
                double l = 1.0D - ((r < 0.5D) ? r : (1.0D - r)) * 1.5D;
                compressedCureAntimatter[i].setColor((int) (l * (255.0D * (1.0D - r) + 150.0D * r)), (int) (l * (50.0D * (1.0D - r) + 0.0D * r)), (int) (l * (255.0D * (1.0D - r) + 0.0D * r))).setColor((int) (200.0D * r), (int) (200.0D * r), 0, 1).setColor(255, 255, 255, 2);
            }
        }
        public static final Material octupleClay = new GenericMaterial("octuple_energetic_clay", "OctupleEnergeticClay").setID(525).setColor(255, 255, 0).setColor(140, 140, 140, 1).setColor(255, 255, 255, 2);
        public static final Material octuplePureAntimatter = compressedCureAntimatter[8];


        private int id;
        private final int[][] colors = new int[][] {{140, 140, 140}, {25, 25, 25}, {255, 255, 255}};

        GenericMaterial(String name, String ODName) {
            super(name, ODName);
        }

        public static List<Material> values() {
            return Arrays.asList(aluminum, antimatter, pureAntimatter);
        }

        private GenericMaterial setID(int id) {
            this.id = id;
            return this;
        }
        private GenericMaterial setColor(int r, int g, int b, int index) {
            this.colors[index] = new int[] { r, g, b };
            return this;
        }
        private GenericMaterial setColor(int r, int g, int b) {
            setColor(r, g, b, 0);
            setColor(r / 6, g / 6, b / 6, 1);
            setColor(Math.min(r * 2, 255), Math.min(g * 2, 255), Math.min(b * 2, 255), 2);
            return this;
        }
        private GenericMaterial setColor(int r1, int g1, int b1, int r3, int g3, int b3) {
            setColor((r1 + r3) / 2, (g1 + g3) / 2, (b1 + b3) / 2, 0);
            setColor(r1, g1, b1, 1);
            setColor(r3, g3, b3, 2);
            return this;
        }
    }

    public static class ClayMaterial extends Material {
        public static final Material clay = new ClayMaterial("clay");
        public static final Material denseClay = new ClayMaterial("dense_clay");
        public static final Material indClay = new ClayMaterial("ind_clay");
        public static final Material advIndClay = new ClayMaterial("adv_ind_clay");

        ClayMaterial(String name) {
            super(name, "");
        }

        public static List<Material> values() {
            return Arrays.asList(clay, denseClay, indClay, advIndClay);
        }
    }

    public interface IShape {
        String getName();
    }

    public enum ClayShape implements IShape {
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
        waterWheel("water_wheel"),
        block("block"),
        ball("ball"),
        dust("dust"),
        ;

        ClayShape(String name) {
            this.name = name;
        }

        @Override public String getName() {
            return name;
        }

        private final String name;
    }

    public enum GenericShape implements IShape {
        plate("plate"),
        largePlate("large_plate"),
        block("block"),
        dust("dust"),
        ingot("ingot"),
        gem("gem");

        GenericShape(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        private final String name;
    }
}
