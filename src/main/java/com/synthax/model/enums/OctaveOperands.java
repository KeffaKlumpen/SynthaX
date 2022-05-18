package com.synthax.model.enums;

/**
 * Enumeration for the octave operands
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 */

public enum OctaveOperands {
    TWO("2'", 0.25f, 2),
    FOUR("4'", 0.5f, 4),
    EIGHT("8'", 1, 8),
    SIXTEEN("16'", 2, 16),
    THIRTYTWO("32'", 4, 32);

    OctaveOperands(String name, float value, int operandValue) {
        this.name = name;
        this.value = value;
        this.operandValue = operandValue;
    }

    private String name;
    private float value;
    private int operandValue;

    public float getValue() {
        return value;
    }

    public int getOperandValue() {
        return operandValue;
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
