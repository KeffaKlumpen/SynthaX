package com.synthax.model.enums;

/**
 * Enumeration for the octave operands
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 */

public enum OctaveOperands {
    THIRTYTWO("32'", 0.25f, 32),
    SIXTEEN("16'", 0.5f, 16),
    EIGHT("8'", 1f, 8),
    FOUR("4'", 2f, 4),
    TWO("2'", 4f, 2);

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
