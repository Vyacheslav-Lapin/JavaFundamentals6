package com.epam;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PropFactoryTest {

    @Test
    void getObject() {
        CircleGO circleGO = PropFactory.getObject(CircleGO.class);
        CircleGO circleGO1 = new CircleGO(15.0);

//        CircleGO circleGO2 = new CircleGO().setRadius(155);

        assertThat(circleGO, is(circleGO1));
    }
}