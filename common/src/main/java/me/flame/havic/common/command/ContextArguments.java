package me.flame.havic.common.command;

import me.flame.havic.common.utils.Option;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class ContextArguments<S> {
    protected String[] args;
    protected int length;

    protected static final Map<Class<?>, Function<String, ?>> typeConverters = new HashMap<>();

    static {
        typeConverters.put(Integer.class, Integer::parseInt);
        typeConverters.put(Long.class, Long::parseLong);
        typeConverters.put(Float.class, Float::parseFloat);
        typeConverters.put(Double.class, Double::parseDouble);
        typeConverters.put(int.class, Integer::parseInt);
        typeConverters.put(long.class, Long::parseLong);
        typeConverters.put(float.class, Float::parseFloat);
        typeConverters.put(double.class, Double::parseDouble);

        typeConverters.put(UUID.class, UUID::fromString);
        typeConverters.put(String.class, Function.identity());
    }

    public ContextArguments(String[] args) {
        this.args = args;
        this.length = args.length;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public String get(int index) {
        if (index < 0 || index >= length) return null;

        return args[index];
    }

    public String getQuoting(int index) {
        if (index < 0 || index >= length) return null;

        String arg = args[index];
        return arg.charAt(0) == '"'
                ? string(1, index, arg.length(), arg, args, new StringBuilder())
                : arg;
    }

    public String greedy(int index) {
        if (index < 0 || index >= length) return null;

        String[] newArgs = new String[length - index];
        System.arraycopy(args, index, newArgs, 0, newArgs.length);
        return String.join(" ", newArgs);
    }

    private static String string(int n, int index, int length, String arg, String[] args, StringBuilder builder) {
        int i = (n == 1) ? 1 : 0;
        while (i != length) {
            char character = arg.charAt(i);
            if (character == '"') return builder.toString();
            builder.append(character);
            i++;
        }
        String newString = get(index + 1, args);
        if (newString != null) return string(n + 1, index, newString.length(), newString, args, builder);
        return builder.toString();
    }

    @Contract(pure = true)
    private static @Nullable String get(int index, String @NotNull [] args) {
        try {
            return args[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public <T> T get(int index, Class<T> type) {
        if (index >= 0 && index < length) {
            String arg = args[index];
            Function<String, ?> function = typeConverters.get(type);
            if (function == null) return null;
            return type.cast(function.apply(arg));
        }
        return null;
    }

    public Option<String> getOption(int index) {
        return Option.of(get(index));
    }

    public <T> Option<T> getOption(int index, Class<T> type) {
        if (index >= 0 && index < args.length) {
            String arg = args[index] != null ? args[index] : "-1";
            Function<String, ?> function = typeConverters.get(type);
            if (function == null) return Option.empty();
            return Option.of(type.cast(function.apply(arg)));
        }
        return Option.empty();
    }

    public abstract S getPlayer(int index);

    public abstract Option<S> getOptionalPlayer(int index);
}
