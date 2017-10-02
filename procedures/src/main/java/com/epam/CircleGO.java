package com.epam;

import lombok.*;

import java.util.Objects;

import static java.lang.Math.PI;

@Data
@EqualsAndHashCode(exclude = "radius")
@AllArgsConstructor
public class CircleGO extends GeometricObject {

    private double radius;

    @Override
    public double getArea() {
        return PI * radius * radius;
    }
}
