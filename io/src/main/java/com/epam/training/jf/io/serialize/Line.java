package com.epam.training.jf.io.serialize;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Line implements Serializable {
    private Point point1;
    private Point point2;
    private int index;
}
