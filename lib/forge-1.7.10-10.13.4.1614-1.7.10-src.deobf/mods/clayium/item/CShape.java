package mods.clayium.item;

public class CShape {
    public String name;
    public String iname;

    public CShape(String name, String iname, String oreDictionaryName, int meta) {
        this.name = name;
        this.iname = iname;
        this.oreDictionaryName = oreDictionaryName;
        this.meta = meta;
    }

    public String oreDictionaryName;
    public int meta;

    public CShape(String name, String iname, int meta) {
        this(name, iname, name.toLowerCase(), meta);
    }


    public boolean equals(Object obj) {
        return (obj instanceof CShape && ((CShape) obj).name
                .equals(this.name) && ((CShape) obj).iname.equals(this.iname) && ((CShape) obj).meta == this.meta);
    }


    public int hashCode() {
        return this.meta;
    }
}
