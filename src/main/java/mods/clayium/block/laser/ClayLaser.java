package mods.clayium.block.laser;


public class ClayLaser {
    static final int colorNum = 3;
    static final int lLifespan = 10;
    static final double[] lBases = new double[] {2.5D, 1.8D, 1.5D};
    static final double[] lMaxEnergys = new double[] {1000.0D, 300.0D, 100.0D};
    static final double[] lDampingRate = new double[] {0.1D, 0.1D, 0.1D};

    public int[] numbers = new int[3];
    public int age = 0;

    public ClayLaser(int age, int... n) {
        this.age = age;
        for (int i = 0; i < n.length && i < 3; i++) {
            this.numbers[i] = n[i];
        }
    }


    public static double calculateEnergyPerColor(int number, double base, double maxEnergy, double dampingRate) {
        double r = dampingRate;
        if (number <= 0 || r <= 0.0D || maxEnergy < 0.0D || base < 1.0D) return 1.0D;
        double c = Math.log(maxEnergy) / Math.log((1.0D + r) / r) * Math.log(base);
        double a = Math.pow(Math.E, (1.0D + r) / c);
        double e = Math.pow(maxEnergy, Math.log((1.0D + r) / (Math.pow(a, -number) + r)) / Math.log((1.0D + r) / r));
        double m = 1.0D / (1.0D + r * Math.pow(a, number)) + r * Math.pow(a, number) / (1.0D + r * Math.pow(a, number)) * number;
        double u = Math.max(e * m, 1.0D);
        return u;
    }

    public static double getEnergy(int... n) {
        double res = 1.0D;
        for (int i = 0; i < n.length && i < 3; i++) {
            res *= calculateEnergyPerColor(n[i], lBases[i], lMaxEnergys[i], lDampingRate[i]);
        }
        return res - 1.0D;
    }

    public double getEnergy() {
        return getEnergy(this.numbers);
    }

    public static ClayLaser mergeClayLasers(ClayLaser[] lasers) {
        int[] numbers = new int[3];
        int age = 0;

        for (ClayLaser laser : lasers) {
            age = Math.max(age, laser.age);
        }

        if (age >= 10) {
            for (ClayLaser laser : lasers) {
                for (int i = 0; i < 3; i++) {
                    numbers[i] = Math.max(numbers[i], laser.numbers[i]);
                }
            }
        } else {

            for (ClayLaser laser : lasers) {
                for (int i = 0; i < 3; i++) {
                    numbers[i] = numbers[i] + laser.numbers[i];
                }
            }
        }

        return new ClayLaser(age, numbers);
    }
}
