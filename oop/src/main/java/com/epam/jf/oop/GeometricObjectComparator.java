package com.epam.jf.oop;

import java.util.Comparator;

public class GeometricObjectComparator implements Comparator<GeometricObject> {

    @Override
    public int compare(GeometricObject o1, GeometricObject o2) {
        return Double.compare(o1.getArea(), o2.getArea());
    }
}
