package com.epam;

@FunctionalInterface
public interface Flier {

    int X = 1;

    String getEggs(long l);

    default String getEggs(int i) {
        return "";
    }

    static void fly(String s) {

    }

    static void main(String... args) {
        Flier flier = x -> "eggs";
        System.out.println(flier.getEggs(5L));
    }
}
