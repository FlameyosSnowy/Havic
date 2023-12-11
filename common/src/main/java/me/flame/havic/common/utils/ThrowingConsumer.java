package me.flame.havic.common.utils;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {
    void accept(T value) throws E;
}