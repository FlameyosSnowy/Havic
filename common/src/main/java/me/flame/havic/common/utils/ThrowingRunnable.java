package me.flame.havic.common.utils;

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;
}
