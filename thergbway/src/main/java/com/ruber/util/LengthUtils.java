package com.ruber.util;

/**
 * Created by thergbway on 27.11.16.
 */
public class LengthUtils {
    public static Double findBetween(Double lat1, Double lon1, Double lat2, Double lon2) {
        return Math.sqrt((lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1));
    }
}
