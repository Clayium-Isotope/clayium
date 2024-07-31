package mods.clayium.util;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ChatGPTからスレッドセーフな {@link AtomicReference} の利用を促された。
 */
public final class LateInit<T> {
    private final AtomicReference<Optional<T>> value = new AtomicReference<>(Optional.empty());

    public T get() {
        return this.value.get().orElseThrow(() -> new NoSuchElementException("A LateInit value was absent"));
    }

    public T or(T other) {
        return this.value.get().orElse(other);
    }

    public void set(T value) {
        // set が成功したか否かに関心はない
        this.value.compareAndSet(Optional.empty(), Optional.ofNullable(value));
    }

    public boolean isInitialized() {
        return this.value.get().isPresent();
    }
}
