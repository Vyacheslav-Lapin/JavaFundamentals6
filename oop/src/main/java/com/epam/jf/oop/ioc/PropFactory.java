package com.epam.jf.oop.ioc;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
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

    @SneakyThrows
    @SuppressWarnings("WeakerAccess")
    public static <T> T getObject(@NotNull Constructor<T> constructor) {
        val parameters = constructor.getParameters();
        int length = parameters.length;
        val args = new Object[length];
        val properties = getProperties(
                constructor.getDeclaringClass().getSimpleName());

        Function<String, String> valueExtractor = properties::getProperty;
        for (int i = 0; i < length; i++)
            args[i] = parse(parameters[i], valueExtractor);

        return constructor.newInstance(args);
    }

    private static Properties getProperties(@NotNull String name) {
        return new Properties() {
            @SneakyThrows
            Properties load(@NotNull String className) {
                val path = toFileName.apply(className);
                InputStream inputStream = PropFactory.class.getResourceAsStream(path);
                if (inputStream != null)
                    try (inputStream) {
                        load(inputStream);
                        return this;
                    }
                return null;
            }
        }.load(name);
    }

    @SneakyThrows
    public static <T> T getObject(Class<T> aClass) {
        //noinspection unchecked
        val constructor = (Constructor<T>) aClass.getConstructors()[0];
        return getObject(constructor);
    }

    private static Object parse(Parameter param, Function<String, String> valueExtractor) {
        Class<?> type = param.getType();
        String property = valueExtractor.apply(param.getName());
        return PARSERS.get(type).apply(property);
    }
}
