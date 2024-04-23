package me.flame.havic.common.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings({ "unchecked", "unused" })
public class Option<T> {
    @Nullable
    private final T value;

    private static final Option<?> EMPTY = new Option<>(null);

    private Option(@Nullable T value) {
        this.value = value;
    }

    public static <T> Option<T> of(T value) {
        return value != null ? new Option<>(value) : empty();
    }


    public static <T> Option<CompletableFuture<T>> completed(T value) {
        return value != null ? new Option<>(CompletableFuture.completedFuture(value)) : empty();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // that is so long
    public static <T> Option<T> from(Optional<T> value) {
        T val = value.orElse(null);
        return val != null ? new Option<>(val) : empty();
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    public static <T> Option<T> when(boolean condition, T value) {
        return condition ? new Option<>(value) : empty();
    }

    public static <T> Option<T> when(boolean condition, Supplier<T> value) {
        return condition ? new Option<>(value.get()) : empty();
    }

    public static <T> Option<T> empty() {
        return (Option<T>) Option.EMPTY;
    }

    public boolean isSet() {
        return this.value != null;
    }

    public boolean isEmpty() {
        return this.value == null;
    }

    public Option<T> peek(Consumer<? super T> action) {
        if (this.value != null) action.accept(this.value);
        return this;
    }

    public Option<T> peekOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (this.value != null) action.accept(this.value);
        else emptyAction.run();
        return this;
    }

    public Option<T> peekIf(Predicate<T> predicate, Consumer<? super T> action) {
        if (this.value != null && predicate.test(value)) action.accept(this.value);
        return this;
    }

    public Option<T> peekIfOrElse(Predicate<T> predicate, Consumer<? super T> action, Runnable emptyAction) {
        if (this.value != null && predicate.test(value)) action.accept(this.value);
        else emptyAction.run();
        return this;
    }

    public Option<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (value == null) return this;
        return predicate.test(this.value) ? this : empty();
    }

    public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return of(mapper.apply(this.value));
    }

    public <U> Option<U> flatMap(@NonNull Function<? super T, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (value == null) return empty();
        return (Option<U>) Objects.requireNonNull(mapper.apply(this.value));
    }

    public Option<T> or(Supplier<? extends Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (value == null) return (Option<T>) Objects.requireNonNull(supplier.get());
        return this;
    }

    public Stream<T> stream() {
        return value == null ? Stream.empty() : Stream.of(this.value);
    }

    public T orElse(T other) {
        return this.value != null ? this.value : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return this.value != null ? this.value : supplier.get();
    }

    @SuppressWarnings("DataFlowIssue")
    public Iterable<T> iterator() {
        return value != null
                ? (Iterable<T>) new SingletonIterator(value)
                : (Iterable<T>) Collections.emptyIterator();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(@Nullable Object to) {
        if (!(to instanceof Option)) return false;
        return contentEquals(((Option<?>) to).value);
    }

    public boolean contentEquals(@Nullable Object to) {
        return Objects.equals(value, to);
    }

    public String toString() {
        return this.value != null ? "Option[" + this.value + "]" : "Optional.empty";
    }

    public <S> Option<S> is(Class<S> type) {
        return this
                .filter(type::isInstance)
                .map(type::cast);
    }

    public <TH extends Throwable> T orElseThrow(Supplier<TH> throwable) throws TH {
        if (value == null) throw throwable.get();
        return value;
    }

    @RequiredArgsConstructor
    public class SingletonIterator implements Iterator<T> {
        private boolean hasNotCheckedValue = true;
        private final T value;

        @Override
        public boolean hasNext() {
            return hasNotCheckedValue;
        }

        @Override
        public T next() {
            hasNotCheckedValue = false;
            return value;
        }
    }
}
