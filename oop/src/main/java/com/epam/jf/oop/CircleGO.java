package com.epam.jf.oop;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

import static java.lang.Math.PI;

@SuppressWarnings("Lombok")
@Log4j2
@Value
public class CircleGO extends GeometricObject {

    private double radius;

    @Override
    public double getArea() {
        return PI * radius * radius;
    }

    /**
     * WARN!!! Use for tests only! Don`t use it for business-logic!
     */
    @SuppressWarnings("unused")
    @SneakyThrows
    CircleGO setRadius(double radius) {
        Field field;
        //noinspection JavaReflectionMemberAccess
        field = CircleGO.class.getField("radius");
        field.setAccessible(true);
        field.set(this, radius);

        return this;
    }
}
