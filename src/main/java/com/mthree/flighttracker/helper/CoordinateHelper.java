package com.mthree.flighttracker.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CoordinateHelper {
    public static final int SCALE = 4;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal createCoord(String coord) {
        return new BigDecimal(coord).setScale(SCALE, ROUNDING_MODE);
    }
}
