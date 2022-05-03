package com.synthax.model.enums;

/**
 * Maps note names to frequencies.
 * Their index is their Midi-number.
 */
public enum MidiNote {
    Cx1(8.18f),
    Dbx1(8.66f),
    Dx1(9.18f),
    Ebx1(9.72f),
    Ex1(10.30f),
    Fx1(10.91f),
    Gbx1(11.56f),
    Gx1(12.25f),
    Abx1(12.98f),
    Ax1(13.75f),
    Bbx1(14.57f),
    Bx1(15.43f),
    C0(16.35f),
    Db0(17.32f),
    D0(18.35f),
    Eb0(19.45f),
    E0(20.60f),
    F0(21.83f),
    Gb0(23.12f),
    G0(24.50f),
    Ab0(25.96f),
    A0(27.50f),
    Bb0(29.14f),
    B0(30.87f),
    C1(32.70f),
    Db1(34.65f),
    D1(36.71f),
    Eb1(38.89f),
    E1(41.20f),
    F1(43.65f),
    Gb1(46.25f),
    G1(49.00f),
    Ab1(51.91f),
    A1(55.00f),
    Bb1(58.27f),
    B1(61.74f),
    C2(65.41f),
    Db2(69.30f),
    D2(73.42f),
    Eb2(77.78f),
    E2(82.41f),
    F2(87.31f),
    Gb2(92.50f),
    G2(98.00f),
    Ab2(103.83f),
    A2(110.00f),
    Bb2(116.54f),
    B2(123.47f),
    C3(130.81f),
    Db3(138.59f),
    D3(146.83f),
    Eb3(155.56f),
    E3(164.81f),
    F3(174.61f),
    Gb3(185.00f),
    G3(196.00f),
    Ab3(207.65f),
    A3(220.00f),
    Bb3(233.08f),
    B3(246.94f),
    C4(261.63f),
    Db4(277.18f),
    D4(293.66f),
    Eb4(311.13f),
    E4(329.63f),
    F4(349.23f),
    Gb4(369.99f),
    G4(392.00f),
    Ab4(415.30f),
    A4(440.00f),
    Bb4(466.16f),
    B4(493.88f),
    C5(523.25f),
    Dd5(554.37f),
    D5(587.33f),
    Eb5(622.25f),
    E5(659.26f),
    F5(698.46f),
    Gb5(739.99f),
    G5(783.99f),
    Ab5(830.61f),
    A5(880.00f),
    Bb5(932.33f),
    B5(987.77f),
    C6(1046.50f),
    Db6(1108.73f),
    D6(1174.66f),
    Eb6(1244.51f),
    E6(1318.51f),
    F6(1396.91f),
    Gb6(1479.98f),
    G6(1567.98f),
    Ab6(1661.22f),
    A6(1760.00f),
    Bb6(1864.66f),
    B6(1975.53f),
    C7(2093.00f),
    Db7(2217.46f),
    D7(2349.32f),
    Eb7(2489.02f),
    E7(2637.02f),
    F7(2793.83f),
    Gb7(2959.96f),
    G7(3135.96f),
    Ab7(3322.44f),
    A7(3520.00f),
    Bb7(3729.31f),
    B7(3951.07f),
    C8(4186.01f);



    private float frequency;

    private static MidiNote[] values;
    public static MidiNote[] getValues(){
        if(values == null){
            values = values();
        }
        return values;
    }

    MidiNote(float frequency){
        this.frequency = frequency;
    }

    public float getFrequency() {
        return frequency;
    }
}
