package mods.clayium.component.teField;

public interface FieldComponent {
    /**
     * @return {@code int}値としての長さ
     */
    int getFieldCount();
    int getField(int id);
    void setField(int id, int value);
}
