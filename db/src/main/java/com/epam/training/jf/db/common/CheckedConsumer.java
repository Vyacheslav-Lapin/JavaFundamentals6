package com.epam.training.jf.db.common;

import java.util.function.Consumer;

@FunctionalInterface
public interface CheckedConsumer<T> extends io.vavr.CheckedConsumer<T> {

    default Consumer<T> unchecked() {
        return t -> {
            try {
                accept(t);
            } catch (Throwable e) {
                sneakyThrow(e);
            }
        };
    }

    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    private static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
