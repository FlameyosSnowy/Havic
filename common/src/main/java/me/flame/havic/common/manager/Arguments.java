package me.flame.havic.common.manager;

import me.flame.havic.common.command.registries.TypeConversionRegistry;
import me.flame.havic.common.utils.Option;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class Arguments {
    private final String[] args;
    private final int length;

    private static TypeConversionRegistry typeConversionRegistry;

    private static final char QUOTING_END = '"';

    static void typeConversionRegistry(TypeConversionRegistry typeConversionRegistry) {
        Arguments.typeConversionRegistry = typeConversionRegistry;
    }

    @Contract(pure = true)
    public Arguments(String @NotNull [] args) {
        this.args = args;
        this.length = args.length;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param  index  the index of the element to retrieve
     * @return        the element at the specified index, or null if the index is out of range
     */
    public String get(int index) {
        return Arguments.get(index, args);
    }

    /**
     * Retrieves the quoting at the specified index.
     *
     * @param  index  the index of the quoting to retrieve
     * @return        the quoting at the specified index, or null if the index is out of bounds
     */
    public String getQuoting(final int index) {
        if (index < 0 || index >= this.length) return null;

        final String arg = args[index];
        if (arg.charAt(0) == QUOTING_END) return arg;

        final int length = arg.length();
        final StringBuilder builder = new StringBuilder(28);
        return quoteStringWhilePossible(index, length, arg, args, builder);
    }

    /**
     * A function that returns a string by joining the elements of the 'args' array
     * starting from the 'index' position.
     *
     * @param  index  the starting index from which elements are joined
     * @return        the joined string
     */
    public @Nullable String greedy(int index) {
        if (index < 0 || index >= length) return null;
        return String.join(" ", index == 0 ? args : getStrings(index));
    }

    @NotNull
    private String @NotNull [] getStrings(int index) {
        String[] newArgs = new String[length - index];
        System.arraycopy(args, index, newArgs, 0, newArgs.length);
        return newArgs;
    }

    private static @NotNull String quoteStringWhilePossible(int index, int length, final CharSequence arg, final String[] args, final StringBuilder builder) {
        int characterIndex = 1;
        while (true) {
            while (characterIndex < length) {
                char character = arg.charAt(characterIndex);
                if (character == QUOTING_END) return builder.toString();

                builder.append(character);
                characterIndex++;
            }
            String string = get(index + 1, args);
            if (string == null) {
                builder.trimToSize();
                return builder.toString();
            }
            characterIndex = 0;
        }
    }

    @Nullable
    @Contract(pure = true)
    private static String get(int index, String @NotNull [] args) {
        return (index < 0 || index >= args.length) ? null : args[index];
    }

    /**
     * Retrieves an object of the specified type at the given index.
     *
     * @param  index  the index at which to retrieve the object
     * @param  type   the type of object to retrieve
     * @return        the retrieved object of the specified type, or null if the index is out of bounds or the type conversion is not supported
     */
    @Nullable
    public <T> T get(int index, Class<T> type) {
        String arg = Arguments.get(index, args);
        if (arg == null) return null;

        Function<String, ?> function = typeConversionRegistry.convert(type);
        if (function == null) return null;
        return type.cast(function.apply(arg));
    }

    public Option<String> getOption(int index) {
        return Option.of(get(index));
    }

    public <T> Option<T> getOption(int index, Class<T> type) {
        return Option.of(get(index, type));
    }

    /**
     * Copies the elements of the given array starting from the specified index
     * into a new array, and returns a new instance of the Arguments class
     * initialized with the copied elements.
     *
     * @param  arguments      the array of arguments to copy
     * @param  startingIndex  the index to start copying from
     * @return                a new instance of the Arguments class initialized
     *                        with the copied elements
     */
    @NotNull
    @Contract("_, _ -> new")
    public Arguments copy(String[] arguments, int startingIndex) {
        System.arraycopy(args, startingIndex, arguments, 0, arguments.length);
        return new Arguments(arguments);
    }
}
