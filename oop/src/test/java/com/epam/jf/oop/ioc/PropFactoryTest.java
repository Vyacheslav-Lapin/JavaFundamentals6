package com.epam.jf.oop.ioc;

import com.epam.jf.oop.CircleGO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropFactoryTest {

    @Test
    void getObject() {
        CircleGO circleGO = PropFactory.getObject(CircleGO.class);

        assertEquals(circleGO, new CircleGO(15));
    }


}