package com.epam;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class PropFactoryTest {

    @Test
    void getObject() {
        CircleGO circleGO = PropFactory.getObject(CircleGO.class);
        CircleGO circleGO1 = new CircleGO(15.0);

        CircleGO circleGO2 = new CircleGO();

        assertThat(circleGO, is(circleGO1));
    }
}