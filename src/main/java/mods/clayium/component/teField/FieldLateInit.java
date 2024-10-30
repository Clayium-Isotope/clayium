package mods.clayium.component.teField;

import mods.clayium.util.LateInit;

public class FieldLateInit<T extends FieldComponent> extends LateInit<T> implements FieldComponent {
    @Override
    public int getFieldCount() {
        return this.get().getFieldCount();
    }

    @Override
    public int getField(int id) {
        return this.get().getField(id);
    }

    @Override
    public void setField(int id, int value) {
        this.get().setField(id, value);
    }
}
