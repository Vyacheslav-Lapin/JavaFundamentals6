package com.epam;

import op.Outer;

public class Main {

    private Main() {
    }

    private int x = 10;

    public int getX() {
        return x;
    }

    public static void main(String... args) {
        Main instanse = getInstanse();
        Main instanse2 = getInstanse();
        Outer.print("instanse.getX() = " + instanse.getX());
        System.out.println(instanse == instanse2);
    }

    private static Main instanse;

    public static Main getInstanse() {
        if (instanse == null)
            synchronized (Main.class) {
                if (instanse == null)
                    instanse = new Main();
            }

        return instanse;
    }


}
