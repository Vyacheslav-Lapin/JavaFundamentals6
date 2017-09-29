package com.epam;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public abstract class GeometricObject {
    public abstract double getArea();

    public static void main(String... args) {
        Comparator<GeometricObject> comparator = new GeometricObjectComparator();
        Set<GeometricObject> set = new TreeSet<>(comparator);

        set.add(new RectangleGO(4, 5));
        set.add(new CircleGO(40));
        set.add(new CircleGO(40));
        set.add(new RectangleGO(4, 1));

        System.out.println("A sorted set of geometric objects");
        for (GeometricObject elements : set)
            System.out.println("area = " + elements.getArea());
    }
}
