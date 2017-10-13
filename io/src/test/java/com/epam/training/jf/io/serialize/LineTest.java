package com.epam.training.jf.io.serialize;

import io.vavr.CheckedConsumer;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

class LineTest {

    Line line = new Line(
            new Point(1.0, 1.0),
            new Point(2.0, 2.0),
            1);

    final static File file = new File("./file");

    @AfterEach
    void tearDown() {
        assert file.delete();
    }

    @SneakyThrows
    private static void withObjectOutputStream(
            CheckedConsumer<ObjectOutputStream> objectOutputStreamCheckedConsumer) {
        try (val fileOutputStream = new FileOutputStream(file);
             val objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStreamCheckedConsumer.accept(objectOutputStream);
        }
    }

    @SneakyThrows
    private static void withObjectInputStream(
            CheckedConsumer<ObjectInputStream> objectInputStreamCheckedConsumer) {
        try (val fileInputStream = new FileInputStream(file);
             val objectInputStream = new ObjectInputStream(fileInputStream)) {
            objectInputStreamCheckedConsumer.accept(objectInputStream);
        }
    }

    @Test
    @DisplayName("Serialization works correctly")
    void serializationWorksCorrectly() {
        // Serialize
        withObjectOutputStream(objectOutputStream -> {
            objectOutputStream.writeObject(line);
            objectOutputStream.writeObject(
                    line.setIndex(3));
        });

        // Deserialize
        withObjectInputStream(objectInputStream -> {
            val line = objectInputStream.readObject();
//          assertThat(line, not(objectInputStream.readObject())); // fail
            assertThat(line, is(objectInputStream.readObject()));
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("Serialization works correctly with mutable objects")
    void serializationWorksCorrectlyWithMutableObjects() {
        // Serialize
        withObjectOutputStream(objectOutputStream -> {
            objectOutputStream.writeObject(line);
            objectOutputStream.reset(); // !!!
            objectOutputStream.writeObject(
                    line.setIndex(3));
        });

        // Deserialize
        withObjectInputStream(objectInputStream -> {
            val line = objectInputStream.readObject();
            assertThat(line, not(objectInputStream.readObject()));
        });
    }
}
