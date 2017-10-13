package com.epam.training.jf.io.serialize;

import lombok.Value;
import java.io.Serializable;

@Value
public class Point implements Serializable {
    private double x;
    private double y;
}
