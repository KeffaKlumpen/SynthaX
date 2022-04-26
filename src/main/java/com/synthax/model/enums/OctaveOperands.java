package com.synthax.model.enums;

public enum OctaveOperands {
    TWO("2'", 4),
    FOUR("4'", 2),
    EIGHT("8'", 1),
    SIXTEEN("16'", 2),
    THIRTYTWO("32'", 4);

    OctaveOperands(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

    public int getValue() {
        return value;
    }

    public static String[] getNames() {
        OctaveOperands[] operands = OctaveOperands.values();
        String[] names = new String[operands.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = operands[i].toString();
        }

        return names;
    }

    @Override
    public String toString() {
        return name;
    }

}
