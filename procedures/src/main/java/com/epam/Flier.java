package com.epam;

@FunctionalInterface
public interface Flier {

    int x = 1;

    String getEggs(long l);

    default String getEggs(int i) {
        return "";
    }

    static void fly(String s) {

    }

    static void main(String... args) {
        Flier flier = x -> "kjhbsg";
        System.out.println(flier.getEggs(5L));
    }
}
