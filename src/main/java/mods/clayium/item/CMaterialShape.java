package mods.clayium.item;

public class CMaterialShape {
    public CMaterial material;
    public CShape shape;

    public CMaterialShape(CMaterial material, CShape shape) {
        this.material = material;
        this.shape = shape;
    }


    public boolean equals(Object obj) {
        return (obj instanceof CMaterialShape && ((CMaterialShape) obj).material.equals(this.material) && ((CMaterialShape) obj).shape.equals(this.shape));
    }


    public int hashCode() {
        return (this.material.hashCode() << 16) + this.shape.hashCode();
    }
}
