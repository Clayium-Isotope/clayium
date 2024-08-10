package mods.clayium.component.teField;

import mods.clayium.core.ClayiumCore;

import java.util.Arrays;

public class FieldManager implements FieldComponent {
    protected final FieldComponent[] components;
    protected final int[] heads;

    public FieldManager(FieldComponent ...components) {
        this(0, components);
    }

    public FieldManager(int startIncl, FieldComponent ...components) {
        this.components = components;

        this.heads = new int[components.length + 1];
        int acc = startIncl;
        for (int i = 0; i < components.length; i++) {
            this.heads[i] = acc;
            acc += components[i].getFieldCount();
        }
        this.heads[components.length] = acc;
    }

    @Override
    public int getFieldCount() {
        return this.heads[this.heads.length - 1] - this.heads[0];
    }

    @Override
    public int getField(int id) {
        int index = this.select(id);
        if (index == -1) return 0;

        try {
            return this.components[index].getField(id - this.heads[index]);
        } catch (IndexOutOfBoundsException e) {
            ClayiumCore.logger.error(e);
            ClayiumCore.logger.info("getField || comp: " + Arrays.toString(this.components) + ", heads: " + Arrays.toString(this.heads) + ", id: " + id + ", index: " + index);
            return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        int index = this.select(id);
        if (index == -1) return;

        try {
            this.components[index].setField(id - this.heads[index], value);
        } catch (IndexOutOfBoundsException e) {
            ClayiumCore.logger.error(e);
            ClayiumCore.logger.info("setField || comp: " + Arrays.toString(this.components) + ", heads: " + Arrays.toString(this.heads) + ", id: " + id + ", index: " + index + ", value: " + value);
        }
    }

    public int select(int id) {
        if (this.heads[0] > id || this.heads[this.heads.length - 1] <= id)
            return -1;

        for (int i = 0; i < this.heads.length - 1; i++) {
            if (this.heads[i] <= id && this.heads[i + 1] > id) {
                return i;
            }
        }

        return -1;
    }
}
