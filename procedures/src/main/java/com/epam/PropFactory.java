package com.epam;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Properties;

public class PropFactory {

    public static <T> T getObject(Class<T> aClass) {
        Properties properties = new Properties();

        try {
            properties.load(
                    PropFactory.class.getResourceAsStream(
                            String.format("/%s.properties", aClass.getSimpleName())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
