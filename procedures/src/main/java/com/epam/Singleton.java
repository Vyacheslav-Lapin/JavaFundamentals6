package com.epam;

public class Singleton {

    private Singleton() {
    }

    @SuppressWarnings("FieldCanBeLocal")
    private int x = 10;

    public int getX() {
        return x;
    }

    private static Singleton instance;

    // Dbl-check locking
    @SuppressWarnings("WeakerAccess")
    public static Singleton getInstance() {
        if (instance == null)
            synchronized (Singleton.class) {
                if (instance == null)
                    instance = new Singleton();
            }

        return instance;
    }

}
