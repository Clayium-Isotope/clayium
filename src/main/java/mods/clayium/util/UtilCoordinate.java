package mods.clayium.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class UtilCoordinate {
    public static Vec3 v3(double x, double y, double z) {
        return Vec3.createVectorHelper(x, y, z);
    }

    public static Vec3RYP v3RYP(double r, float yaw, float pitch) {
        return new Vec3RYP(r, yaw, pitch);
    }

    public static Vec3SYF v3SYF(double strafe, double y, double forward, float yaw) {
        return new Vec3SYF(strafe, y, forward, yaw);
    }

    public static class Vec3RYP extends Vec3 {
        protected Vec3RYP(double r, float yaw, float pitch) {
            super(r, yaw, pitch);
        }

        public double getR() {
            return this.xCoord;
        }

        public float getYaw() {
            return (float) this.yCoord;
        }

        public float getPitch() {
            return (float) this.zCoord;
        }

        public void setR(double r) {
            this.xCoord = r;
        }

        public void setYaw(double yaw) {
            this.yCoord = yaw;
        }

        public void setPitch(double pitch) {
            this.zCoord = pitch;
        }

        public static Vec3RYP fromXYZ(Vec3 vec3) {
            return UtilCoordinate.v3RYP(vec3.lengthVector(), UtilCoordinate.r2d((float) Math.atan2(-vec3.xCoord, vec3.zCoord)), UtilCoordinate.r2d((float) -Math.atan2(vec3.yCoord, MathHelper.sqrt_double(vec3.xCoord * vec3.xCoord + vec3.zCoord * vec3.zCoord))));
        }

        public static Vec3 toXYZ(double r, float yaw, float pitch) {
            return UtilCoordinate.v3(-r * MathHelper.sin(UtilCoordinate.d2r(yaw)) * MathHelper.cos(UtilCoordinate.d2r(pitch)), -r * MathHelper.sin(UtilCoordinate.d2r(pitch)), r * MathHelper.cos(UtilCoordinate.d2r(yaw)) * MathHelper.cos(UtilCoordinate.d2r(pitch)));
        }

        public Vec3 toXYZ() {
            return toXYZ(getR(), getYaw(), getPitch());
        }
    }

    public static class Vec3SYF extends Vec3 {
        private float yaw;
        private float yawr;

        public float getYaw() {
            return this.yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
            this.yawr = UtilCoordinate.d2r(yaw);
        }


        public void updateYaw(float newYaw) {
            float newYawr = UtilCoordinate.d2r(newYaw);
            float f = this.yawr - newYawr;
            float sin = MathHelper.sin(f);
            float cos = MathHelper.cos(f);
            double newStrafe = cos * getStrafe() - sin * getForward();
            setForward(sin * getStrafe() + sin * getForward());
            setStrafe(newStrafe);
            this.yaw = newYaw;
            this.yawr = newYawr;
        }

        public double getStrafe() {
            return this.xCoord;
        }

        public double getY() {
            return this.yCoord;
        }

        public double getForward() {
            return this.zCoord;
        }

        public void setStrafe(double strafe) {
            this.xCoord = strafe;
        }

        public void setY(double y) {
            this.yCoord = y;
        }

        public void setForward(double forward) {
            this.zCoord = forward;
        }

        protected Vec3SYF(double strafe, double y, double forward, float yaw) {
            super(strafe, y, forward);
            setYaw(yaw);
        }

        public static Vec3SYF fromXYZ(Vec3 vec3, float yaw) {
            float yawr = yaw * 3.1415927F / 180.0F;
            float sin = MathHelper.sin(yawr);
            float cos = MathHelper.cos(yawr);
            return UtilCoordinate.v3SYF(vec3.xCoord * cos + vec3.zCoord * sin, vec3.yCoord, vec3.zCoord * cos - vec3.xCoord * sin, yaw);
        }

        public Vec3 toXYZ() {
            return toXYZ(getStrafe(), getY(), getForward(), getYaw());
        }

        public static Vec3 toXYZ(double strafe, double y, double forward, float yaw) {
            float yawr = yaw * 3.1415927F / 180.0F;
            float sin = MathHelper.sin(yawr);
            float cos = MathHelper.cos(yawr);
            return UtilCoordinate.v3(strafe * cos - forward * sin, y, forward * cos + strafe * sin);
        }
    }

    private static double d2rRate = 0.017453292519943295D;

    public static float d2r(float degree) {
        return degree * (float) d2rRate;
    }

    private static double r2dRate = 57.29577951308232D;

    public static float r2d(float radian) {
        return radian * (float) r2dRate;
    }
}


