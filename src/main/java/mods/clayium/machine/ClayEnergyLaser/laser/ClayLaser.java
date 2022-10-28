package mods.clayium.machine.ClayEnergyLaser.laser;

import javax.vecmath.Point3i;
import javax.vecmath.Tuple3i;

// will make the field, named numbers, net.minecraft.util.math.Vec3i or java.awt.Color
// will be made general about colors
public class ClayLaser {
//    static final int colorNum = 3;
    static final int lLifespan = 10;
    static final double[] lBases = { 2.5D, 1.8D, 1.5D };
    static final double[] lMaxEnergys = { 1000.0D, 300.0D, 100.0D };
    static final double[] lDampingRate = { 0.1D, 0.1D, 0.1D };
    public Tuple3i numbers;
    public int age;

    public ClayLaser(int age, Tuple3i numbers) {
        this.age = age;
        this.numbers = numbers;
    }

    public ClayLaser(int age, int blue, int green, int red) {
        this(age, new Point3i(blue, green, red));
    }

    /**
     * @param n_i number
     * @param b_i base
     * @param m_i maxEnergy
     * @param r dampingRate
     */
    public static double calculateEnergyPerColor(int n_i, double b_i, double m_i, double r) {
        if (n_i <= 0 || r <= 0.0D || m_i < 0.0D || b_i < 1.0D) {
            return 1.0D;
        }

        double c = Math.log(m_i) / (Math.log((1.0D + r) / r) * Math.log(b_i));
        double a = Math.pow(Math.E, (1.0D + r) / c);
        double e = Math.pow(m_i, Math.log((1.0D + r) / (Math.pow(a, -n_i) + r)) / Math.log((1.0D + r) / r));
        double m = (1.0D + r * Math.pow(a, n_i) * (double)n_i) / (1.0D + r * Math.pow(a, n_i));
        return Math.max(e * m, 1.0D);
    }

    private static double getEnergy(Tuple3i numbers) {
        double res = 1.0D;

        res *= calculateEnergyPerColor(numbers.getX(), lBases[0], lMaxEnergys[0], lDampingRate[0]);
        res *= calculateEnergyPerColor(numbers.getY(), lBases[1], lMaxEnergys[1], lDampingRate[1]);
        res *= calculateEnergyPerColor(numbers.getZ(), lBases[2], lMaxEnergys[2], lDampingRate[2]);

        return res - 1.0D;
    }

    public static double getEnergy(int blue, int green, int red) {
        return getEnergy(new Point3i(blue, green, red));
    }

    public double getEnergy() {
        return getEnergy(this.numbers);
    }

    public static ClayLaser mergeClayLasers(ClayLaser[] lasers) {
        Tuple3i numbers = new Point3i();
        int age = 0;

        for (ClayLaser laser : lasers) {
            if (laser.age >= 10) {
                age = 10;
                break;
            }
            age = Math.max(age, laser.age);
        }

        if (age >= 10) {
            for(ClayLaser laser : lasers) {
                numbers.set(Math.max(numbers.x, laser.numbers.x), Math.max(numbers.y, laser.numbers.y), Math.max(numbers.z, laser.numbers.z));
            }
        } else {
            for(ClayLaser laser : lasers) {
                numbers.add(laser.numbers);
            }
        }

        return new ClayLaser(age, numbers);
    }
}
