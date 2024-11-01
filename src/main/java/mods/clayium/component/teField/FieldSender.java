package mods.clayium.component.teField;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;

import java.util.List;

public class FieldSender {
    protected final FieldComponent component;
    protected final int fieldCount;
    protected final int[] olds;

    public FieldSender(FieldComponent tile) {
        this.component = tile;
        this.fieldCount = this.component.getFieldCount();
        this.olds = new int[this.fieldCount];
    }

    public FieldSender(IInventory inv) {
        this(new WrapIInventory(inv));
    }

    public void detectAndSendChanges(Container container, List<IContainerListener> listeners) {
        for (int i = 0; i < this.fieldCount; i++) {
            int value = this.component.getField(i);
            if (this.olds[i] != value) {
                for (IContainerListener listener : listeners) {
                    listener.sendWindowProperty(container, i, value);
                }
                this.olds[i] = value;
            }
        }
    }

    static class WrapIInventory implements FieldComponent {
        private final IInventory internal;

        WrapIInventory(IInventory internal) {
            this.internal = internal;
        }

        @Override
        public int getFieldCount() {
            return this.internal.getFieldCount();
        }

        @Override
        public int getField(int id) {
            return this.internal.getField(id);
        }

        @Override
        public void setField(int id, int value) {
            this.internal.setField(id, value);
        }
    }
}
