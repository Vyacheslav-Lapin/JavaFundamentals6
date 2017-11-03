package com.epam.jf.concurrency;

public class PriorityDemo {
    public static void main(String... args) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Clicker hi = new Clicker();
        Clicker hi2 = new Clicker();
        Clicker hi3 = new Clicker();
        Clicker hi4 = new Clicker();
        Clicker lo = new Clicker();
        Clicker lo2 = new Clicker();
        Clicker lo3 = new Clicker();
        Clicker lo4 = new Clicker();

        hi.setPriority(Thread.MAX_PRIORITY);
        hi2.setPriority(Thread.MAX_PRIORITY);
        hi3.setPriority(Thread.MAX_PRIORITY);
        hi4.setPriority(Thread.MAX_PRIORITY);
        lo.setPriority(Thread.MIN_PRIORITY);
        lo2.setPriority(Thread.MIN_PRIORITY);
        lo3.setPriority(Thread.MIN_PRIORITY);
        lo4.setPriority(Thread.MIN_PRIORITY);

        hi.start();
        hi2.start();
        hi3.start();
        hi4.start();
        lo.start();
        lo2.start();
        lo3.start();
        lo4.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }

        lo.stopClick();
        lo2.stopClick();
        lo3.stopClick();
        lo4.stopClick();

        hi.stopClick();
        hi2.stopClick();
        hi3.stopClick();
        hi4.stopClick();

        try {
            hi.join();
            hi2.join();
            hi3.join();
            hi4.join();
            lo.join();
            lo2.join();
            lo3.join();
            lo4.join();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
        System.out.println("Low-priority thread: " + lo.click);
        System.out.println("Low-priority thread2: " + lo2.click);
        System.out.println("Low-priority thread3: " + lo3.click);
        System.out.println("Low-priority thread4: " + lo4.click);
        System.out.println("High-priority thread: " + hi.click);
        System.out.println("High-priority thread2: " + hi2.click);
        System.out.println("High-priority thread3: " + hi3.click);
        System.out.println("High-priority thread4: " + hi4.click);

    }
}
