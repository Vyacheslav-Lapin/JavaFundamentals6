package com.epam;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.*;

@UtilityClass
public class PropFactory {

    private static final Function<String, InputStream> toInputStream =
            PropFactory.class::getResourceAsStream;

    private static final Function<String, String> toPath = name ->
            String.format("/%s.properties", name);

    @SneakyThrows
    private static Properties getProperties(String name) {
        val path = toPath.apply(name);
        assert new File(path).exists();

        try (val resourceAsStream = toInputStream.apply(path)) {
            val properties = new Properties();
            properties.load(resourceAsStream);
            return properties;
        }
    }

    @SneakyThrows
    public static <T> T getObject(Class<T> aClass) {
        val properties = getProperties(aClass.getSimpleName());

        //noinspection unchecked
        val constructor = (Constructor<T>) aClass.getConstructors()[0];
        val parameters = constructor.getParameters();
        int length = parameters.length;
        val args = new Object[length];
        for (int i = 0; i < length; i++)
            args[i] = parse(parameters[i], properties::getProperty);

        return constructor.newInstance(args);
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
