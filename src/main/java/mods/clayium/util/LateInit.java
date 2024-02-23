package mods.clayium.util;

import java.util.NoSuchElementException;

public class LateInit<T> {
    private T value = null;

    public T get() {
        if (!this.isPresent()) {
            throw new NoSuchElementException("a LateInit value was absent");
        }
        return this.value;
    }

    public T or(T other) {
        return this.isPresent() ? this.value : other;
    }

    public void set(T value) {
        if (!this.isPresent()) {
            this.value = value;
        }
    }

    public boolean isPresent() {
        return this.value == null;
    }
}
