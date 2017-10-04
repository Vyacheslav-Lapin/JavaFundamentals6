package com.epam;

import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import lombok.val;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class PropFactory {

    static class Properties {

        @Delegate
        private java.util.Properties properties = new java.util.Properties();

        @SneakyThrows
        public Properties(String path) {
            File file = new File(path);
            if (!file.exists())
                throw new RuntimeException("Нема файла такого - " + path);
            try (val resourceAsStream = PropFactory.class.getResourceAsStream(path)) {
                properties.load(resourceAsStream);
            }
        }
    }

    public static <T> T getObject(Class<T> aClass) {
        Properties properties = new Properties(
                String.format("/%s.properties",
                        PropFactory.class.getSimpleName()));

        //noinspection unchecked
        Constructor<T> constructor = (Constructor<T>) aClass.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> type = param.getType();
            if (type == Double.class || type == double.class) {
                args[i] = Double.parseDouble(properties.getProperty(param.getName()));
            } else if (type == Integer.class || type == int.class) {
                args[i] = Integer.parseInt(properties.getProperty(param.getName()));
            }//...
        }

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
