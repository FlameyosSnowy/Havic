package me.flame.havic.common.utils;

@FunctionalInterface
public interface ThrowingSupplier<V, E extends Throwable> {
    V get() throws E;
}
