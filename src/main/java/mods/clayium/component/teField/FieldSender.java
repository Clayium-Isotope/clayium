package mods.clayium.component.teField;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

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
}
