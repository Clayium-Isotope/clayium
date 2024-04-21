package mods.clayium.util;

import java.util.NoSuchElementException;

public class LateInit<T> {
    private T value = null;

    public T get() {
        if (!this.isInitialized()) {
            throw new NoSuchElementException("a LateInit value was absent");
        }
        return this.value;
    }

    public T or(T other) {
        return this.isInitialized() ? this.value : other;
    }

    public void set(T value) {
        if (!this.isInitialized()) {
            this.value = value;
        }
    }

    public boolean isInitialized() {
        return this.value != null;
    }
}
