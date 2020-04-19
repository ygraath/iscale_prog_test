package com.cjbmen.core;

public class Duration {

    public enum UNIT {
        DAYS,
        HOURS
    }

    public Duration (Duration.UNIT unit, int value) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public UNIT getUnit() {
        return unit;
    }

    // ----- INTERNALS ----- //
    private int value;
    private UNIT unit;
}
