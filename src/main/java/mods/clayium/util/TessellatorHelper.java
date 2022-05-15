package mods.clayium.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;

public class TessellatorHelper {
    private static List<Matrix2D> coordTransformations = new ArrayList<Matrix2D>();
    private static List<Matrix2D> normalTransformations = new ArrayList<Matrix2D>();

    public static void init() {
        coordTransformations = new ArrayList<Matrix2D>();
        coordTransformations.add(Matrix2D.IdentityMatrix(4));
        normalTransformations = new ArrayList<Matrix2D>();
        normalTransformations.add(Matrix2D.IdentityMatrix(4));
    }

    public static int draw() {
        return Tessellator.instance.draw();
    }

    public static void startDrawingQuads() {
        Tessellator.instance.startDrawingQuads();
    }

    public static void startDrawing(int i) {
        Tessellator.instance.startDrawing(i);
    }


    public static void setTextureUV(double u, double v) {
        Tessellator.instance.setTextureUV(u, v);
    }


    public static void setBrightness(int b) {
        Tessellator.instance.setBrightness(b);
    }


    public static void setColorOpaque_F(float p_78386_1_, float p_78386_2_, float p_78386_3_) {
        Tessellator.instance.setColorOpaque_F(p_78386_1_, p_78386_2_, p_78386_3_);
    }


    public static void setColorRGBA_F(float p_78369_1_, float p_78369_2_, float p_78369_3_, float p_78369_4_) {
        Tessellator.instance.setColorRGBA_F(p_78369_1_, p_78369_2_, p_78369_3_, p_78369_4_);
    }


    public static void setColorOpaque(int p_78376_1_, int p_78376_2_, int p_78376_3_) {
        Tessellator.instance.setColorOpaque(p_78376_1_, p_78376_2_, p_78376_3_);
    }


    public static void setColorRGBA(int p_78370_1_, int p_78370_2_, int p_78370_3_, int p_78370_4_) {
        Tessellator.instance.setColorRGBA(p_78370_1_, p_78370_2_, p_78370_3_, p_78370_4_);
    }


    public static void addVertexWithUV(double x, double y, double z, double u, double v) {
        setTextureUV(u, v);
        addVertex(x, y, z);
    }


    public static void addVertex(double x, double y, double z) {
        double[] c = getTransformatedCoord(x, y, z);
        Tessellator.instance.addVertex(c[0], c[1], c[2]);
    }


    public static void setColorOpaque_I(int p_78378_1_) {
        Tessellator.instance.setColorOpaque_I(p_78378_1_);
    }


    public static void setColorRGBA_I(int p_78384_1_, int p_78384_2_) {
        Tessellator.instance.setColorRGBA_I(p_78384_1_, p_78384_2_);
    }


    public static void disableColor() {
        Tessellator.instance.disableColor();
    }


    public static void setNormal(float p, float q, float r) {
        double[] c = getTransformatedNormal(p, q, r);

        Tessellator.instance.setNormal((float) c[0], (float) c[1], (float) c[2]);
    }

    public static void pushMatrix() {
        double[][] mat = coordTransformation().getArrays();
        double[][] newMat = new double[coordTransformation().getRow()][coordTransformation().getCol()];
        int i;
        for (i = 0; i < coordTransformation().getRow(); i++) {
            for (int j = 0; j < coordTransformation().getCol(); j++) {
                newMat[i][j] = mat[i][j];
            }
        }
        coordTransformations.add(new Matrix2D(newMat));

        mat = normalTransformation().getArrays();
        newMat = new double[normalTransformation().getRow()][normalTransformation().getCol()];
        for (i = 0; i < normalTransformation().getRow(); i++) {
            for (int j = 0; j < normalTransformation().getCol(); j++) {
                newMat[i][j] = mat[i][j];
            }
        }
        normalTransformations.add(new Matrix2D(newMat));
    }

    public static void popMatrix() {
        if (coordTransformations.size() != 0) {
            coordTransformations.remove(coordTransformations.size() - 1);
        }
        if (normalTransformations.size() != 0) {
            normalTransformations.remove(normalTransformations.size() - 1);
        }
    }

    public static void translate(double x, double y, double z) {
        addCoordTransformation(getTranslateMatrix(x, y, z));
    }

    public static void scale(double x, double y, double z) {
        addCoordTransformation(getScaleMatrix(x, y, z));
        addNormalTransformation(getTranslateMatrix(1.0D / x, 1.0D / y, 1.0D / z));
    }

    public static void rotate(double t, double x, double y, double z) {
        addCoordTransformation(getRotateMatrix(t, x, y, z));
        addNormalTransformation(getRotateMatrix(t, x, y, z));
    }

    private static double[] getTransformatedCoord(double x, double y, double z) {
        return getCoord(Matrix2D.mult(coordTransformation(), getVectorMatrix(x, y, z)));
    }

    private static double[] getTransformatedNormal(double p, double q, double r) {
        return getNormalizedCoord(Matrix2D.mult(normalTransformation(), getVectorMatrix(p, q, r)));
    }

    private static void addCoordTransformation(Matrix2D mat) {
        if (coordTransformations.size() == 0) init();
        coordTransformations.set(coordTransformations.size() - 1, Matrix2D.mult(coordTransformation(), mat));
    }

    private static void addNormalTransformation(Matrix2D mat) {
        if (normalTransformations.size() == 0) init();
        normalTransformations.set(normalTransformations.size() - 1, Matrix2D.mult(normalTransformation(), mat));
    }

    private static Matrix2D coordTransformation() {
        if (coordTransformations.size() == 0) init();
        return coordTransformations.get(coordTransformations.size() - 1);
    }

    private static Matrix2D normalTransformation() {
        if (normalTransformations.size() == 0) init();
        return normalTransformations.get(normalTransformations.size() - 1);
    }

    private static Matrix2D getVectorMatrix(double x, double y, double z) {
        return new Matrix2D(new double[] {x, y, z, 1.0D});
    }

    private static double[] getCoord(Matrix2D mat) {
        if (mat.getCol() == 1 && mat.getRow() == 4) {
            double[][] a = mat.getArrays();
            return new double[] {a[0][0], a[1][0], a[2][0]};
        }
        return null;
    }

    private static double[] getNormalizedCoord(Matrix2D mat) {
        double[] a = getCoord(mat);
        if (a != null && (a[0] != 0.0D || a[1] != 0.0D || a[2] != 0.0D)) {
            double l = Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
            a[0] = a[0] / l;
            a[1] = a[1] / l;
            a[2] = a[2] / l;
            return a;
        }
        return null;
    }


    private static Matrix2D getTranslateMatrix(double x, double y, double z) {
        return new Matrix2D(new double[][] {{1.0D, 0.0D, 0.0D, x}, {0.0D, 1.0D, 0.0D, y}, {0.0D, 0.0D, 1.0D, z}, {0.0D, 0.0D, 0.0D, 1.0D}});
    }


    private static Matrix2D getScaleMatrix(double x, double y, double z) {
        return new Matrix2D(new double[][] {{x, 0.0D, 0.0D, 0.0D}, {0.0D, y, 0.0D, 0.0D}, {0.0D, 0.0D, z, 0.0D}, {0.0D, 0.0D, 0.0D, 1.0D}});
    }


    private static Matrix2D getRotateMatrix(double t, double x, double y, double z) {
        double l = x / Math.sqrt(x * x + y * y + z * z);
        double m = y / Math.sqrt(x * x + y * y + z * z);
        double n = z / Math.sqrt(x * x + y * y + z * z);
        double c = Math.cos(t * Math.PI / 180.0D);
        double s = Math.sin(t * Math.PI / 180.0D);
        return new Matrix2D(new double[][] {{l * l + (1.0D - l * l) * c, l * m * (1.0D - c) - n * s, l * n * (1.0D - c) + m * s, 0.0D}, {l * m * (1.0D - c) + n * s, m * m + (1.0D - m * m) * c, m * n * (1.0D - c) - l * s, 0.0D}, {l * n * (1.0D - c) - m * s, m * n * (1.0D - c) + l * s, n * n + (1.0D - n * n) * c, 0.0D}, {0.0D, 0.0D, 0.0D, 1.0D}});
    }
}


