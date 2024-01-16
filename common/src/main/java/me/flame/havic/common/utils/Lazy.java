package me.flame.havic.common.utils;

import lombok.Getter;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {

    private Supplier<T> supplier;
    @Getter
    private boolean initialized;
    private T value;
    private Exception exception;

    public Lazy(T value) {
        this.initialized = true;
        this.value = value;
    }

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static Lazy<Void> run(Runnable runnable) {
        return new Lazy<>(() -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public synchronized T get() {
        if (exception != null) throw new IllegalArgumentException("Lazy value has been already initialized with exception", exception);

        if (initialized) return value;
        initialized = true;

        try {
            return (value = supplier.get());
        } catch (Exception exception) {
            this.exception = exception;
            throw new IllegalArgumentException("Cannot initialize lazy value", exception);
        }
    }

    public boolean hasFailed() {
        return exception != null;
    }

    public boolean hasInitialized() {
        return initialized;
    }
}