package com.epam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.epam.Singleton.getInstance;
import static org.junit.jupiter.api.Assertions.*;

class SingletonTest {

    @Test
    @DisplayName("getInstance works correctly")
    void getInstanceWorksCorrectly() {
        assertEquals(getInstance(), getInstance());
        assertNotEquals(getInstance(), new Object());
    }
}