package com.epam;

import static java.lang.Math.PI;

public class CircleGO extends GeometricObject {
    private double radius;

    public CircleGO(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return PI * radius * radius;
    }
}
