package com.epam.jf.oop.ioc;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@UtilityClass
public class PropFactory {

    private static final Function<String, String> toFileName = name ->
            String.format("/%s.properties", name);

    @SneakyThrows
    @SuppressWarnings("WeakerAccess")
    public static <T> T getObject(Constructor<T> constructor) {
        val parameters = constructor.getParameters();
        int length = parameters.length;
        val args = new Object[length];

        val properties = new Properties() {
            @SneakyThrows
            Properties load(String className) {
                val path = toFileName.apply(className);
                assert PropFactory.class.getResource(path) != null;
                try (val inputStream = PropFactory.class.getResourceAsStream(path)) {
                    load(inputStream);
                    return this;
                }
            }
        }.load(constructor.getDeclaringClass().getSimpleName());

        Function<String, String> valueExtractor = properties::getProperty;

        for (int i = 0; i < length; i++)
            args[i] = parse(parameters[i], valueExtractor);

        return constructor.newInstance(args);
    }

    @SneakyThrows
    public static <T> T getObject(Class<T> aClass) {
        //noinspection unchecked
        val constructor = (Constructor<T>) aClass.getConstructors()[0];
        return getObject(constructor);
    }

    private static Map<Class<?>, Function<String, ?>> PARSERS =
            Map.of(String.class, (Function<String, String>) s -> s,
                    Double.class, (Function<String, Double>) Double::valueOf,
                    double.class, (Function<String, Double>) Double::parseDouble, // double.class, (ToDoubleFunction<String>) Double::parseDouble,
                    Integer.class, (Function<String, Integer>) Integer::valueOf,
                    int.class, (Function<String, Integer>) Integer::parseInt, // int.class, (ToIntFunction<String>) Integer::parseInt,
                    Long.class, (Function<String, Long>) Long::valueOf,
                    long.class, (Function<String, Long>) Long::parseLong, // long.class, (ToLongFunction<String>) Long::parseLong,
                    Float.class, (Function<String, Float>) Float::valueOf,
                    float.class, (Function<String, Float>) Float::parseFloat,
                    Date.class, (Function<String, Date>) s -> {
                        try {
                            return DateFormat.getDateInstance().parse(s);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    });

    private static Object parse(Parameter param, Function<String, String> valueExtractor) {
        Class<?> type = param.getType();
        String property = valueExtractor.apply(param.getName());
        return PARSERS.get(type).apply(property);
    }
}
