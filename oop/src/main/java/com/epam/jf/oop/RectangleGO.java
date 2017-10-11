package com.epam.jf.oop;

public class RectangleGO extends GeometricObject {
    private double sideA;
    private double sideB;


    public RectangleGO(double sideA, double sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
    }

    @Override
    public double getArea() {
        return sideA * sideB;
    }

}
