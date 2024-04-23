package me.flame.havic.common.command.registries;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("unused")
public class TypeConversionRegistry {
    private static final Map<Class<?>, Function<String, ?>> typeConverters = new HashMap<>(18);

    public TypeConversionRegistry() {
        typeConverters.put(Integer.class, Integer::parseInt);
        typeConverters.put(Long.class, Long::parseLong);
        typeConverters.put(Float.class, Float::parseFloat);
        typeConverters.put(Double.class, Double::parseDouble);

        typeConverters.put(int.class, Integer::parseInt);
        typeConverters.put(long.class, Long::parseLong);
        typeConverters.put(float.class, Float::parseFloat);
        typeConverters.put(double.class, Double::parseDouble);

        typeConverters.put(BigInteger.class, BigInteger::new);
        typeConverters.put(BigDecimal.class, BigDecimal::new);

        typeConverters.put(UUID.class, UUID::fromString);
        typeConverters.put(String.class, Function.identity());
    }
    
    public void add(Class<?> clazz, Function<String, ?> function) {
        typeConverters.put(clazz, function);
    }

    public void add(Map<Class<?>, Function<String, ?>> map) {
        typeConverters.putAll(map);
    }

    public Function<String, ?> convert(Class<?> clazz) {
        return typeConverters.get(clazz);
    }

    public boolean contains(Class<?> clazz) {
        return typeConverters.containsKey(clazz);
    }

    public void remove(Class<?> clazz) {
        typeConverters.remove(clazz);
    }
}
