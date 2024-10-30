package mods.clayium.component.teField;

public interface FieldDelegate extends FieldComponent {
    FieldComponent getDelegate();

    @Override
    default int getFieldCount() {
        return this.getDelegate().getFieldCount();
    }

    @Override
    default int getField(int id) {
        return this.getDelegate().getField(id);
    }

    @Override
    default void setField(int id, int value) {
        this.getDelegate().setField(id, value);
    }
}
