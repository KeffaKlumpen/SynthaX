package com.synthax.util;

/**
 * A collection of useful math functions.
 * @author Joel Eriksson Sinclair
 */
public class HelperMath {
    /**
     * Helper function to map a value from one range to another range.
     */
    public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static int clamp(int v, int min, int max) {
        return Math.min(max, Math.max(min, v));
    }
}
