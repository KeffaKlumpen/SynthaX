package com.synthax.model.enums;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Maps note names to frequencies.
 * Their index is their Midi-number.
 * @author Luke Eales
 * @author Joel Eriksson Sinclair
 */
public enum MidiNote {
    Cx1(8.18f, -1),
    Dbx1(8.66f, -1),
    Dx1(9.18f, -1),
    Ebx1(9.72f, -1),
    Ex1(10.30f, -1),
    Fx1(10.91f, -1),
    Gbx1(11.56f, -1),
    Gx1(12.25f, -1),
    Abx1(12.98f, -1),
    Ax1(13.75f, -1),
    Bbx1(14.57f, -1),
    Bx1(15.43f, -1),
    C0(16.35f, -1),
    Db0(17.32f, -1),
    D0(18.35f, -1),
    Eb0(19.45f, -1),
    E0(20.60f, -1),
    F0(21.83f, -1),
    Gb0(23.12f, -1),
    G0(24.50f, -1),
    Ab0(25.96f, -1),
    A0(27.50f, 210),
    Bb0(29.14f, 213.4),
    B0(30.87f, 216.8),
    C1(32.70f, 220.2),
    Db1(34.65f, 223.6),
    D1(36.71f, 227),
    Eb1(38.89f, 230.4),
    E1(41.20f, 233.8),
    F1(43.65f, 237.2),
    Gb1(46.25f, 240.6),
    G1(49.00f, 244),
    Ab1(51.91f, 247.4),
    A1(55.00f, 250.8),
    Bb1(58.27f, 254.2),
    B1(61.74f, 257.6),
    C2(65.41f, 261),
    Db2(69.30f, 264.4),
    D2(73.42f, 267.8),
    Eb2(77.78f, 271.2),
    E2(82.41f, 274.6),
    F2(87.31f, 278),
    Gb2(92.50f, 281.4),
    G2(98.00f, 284.8),
    Ab2(103.83f, 288.2),
    A2(110.00f, 291.6),
    Bb2(116.54f, 295),
    B2(123.47f, 298.4),
    C3(130.81f,  301.8),
    Db3(138.59f, 305.2),
    D3(146.83f, 308.6),
    Eb3(155.56f, 312),
    E3(164.81f, 315.4),
    F3(174.61f, 318.8),
    Gb3(185.00f, 322.2),
    G3(196.00f, 325.6),
    Ab3(207.65f, 329),
    A3(220.00f, 332.4),
    Bb3(233.08f, 335.8),
    B3(246.94f, 339.2),
    C4(261.63f, 342.6),
    Db4(277.18f, 346),
    D4(293.66f, 349.4),
    Eb4(311.13f, 352.8),
    E4(329.63f, 356.2),
    F4(349.23f, 0),
    Gb4(369.99f,3),
    G4(392.00f, 6.4),
    Ab4(415.30f, 9.8),
    A4(440.00f, 13.2),
    Bb4(466.16f, 16.6),
    B4(493.88f, 20),
    C5(523.25f, 23.4),
    Db5(554.37f, 26.8),
    D5(587.33f, 30.2),
    Eb5(622.25f, 33.6),
    E5(659.26f, 37),
    F5(698.46f, 40.4),
    Gb5(739.99f, 43.8),
    G5(783.99f, 47.2),
    Ab5(830.61f, 50.6),
    A5(880.00f, 54),
    Bb5(932.33f, 57.4),
    B5(987.77f, 60.8),
    C6(1046.50f, 64.2),
    Db6(1108.73f, 67.6),
    D6(1174.66f, 71),
    Eb6(1244.51f, 74.4),
    E6(1318.51f, 77.8),
    F6(1396.91f, 81.2),
    Gb6(1479.98f, 84.6),
    G6(1567.98f, 88),
    Ab6(1661.22f, 91.4),
    A6(1760.00f, 94.8),
    Bb6(1864.66f, 98.2),
    B6(1975.53f, 101.6),
    C7(2093.00f, 105),
    Db7(2217.46f, 108.4),
    D7(2349.32f, 111.8),
    Eb7(2489.02f, 115.2),
    E7(2637.02f, 118.6),
    F7(2793.83f, 122),
    Gb7(2959.96f, 125.4),
    G7(3135.96f, 128.8),
    Ab7(3322.44f, 132.2),
    A7(3520.00f, 135.6),
    Bb7(3729.31f, 139),
    B7(3951.07f, 142.4),
    C8(4186.01f, 150);



    private float frequency;
    private double rotation;

    private static MidiNote[] values;
    public static MidiNote[] getValues(){
        if(values == null){
            values = values();
        }
        return values;
    }

    MidiNote(float frequency, double rotation){
        this.frequency = frequency;
        this.rotation = rotation;
    }

    public float getFrequency() {
        return frequency;
    }

    public double getRotation() {return rotation; }

    public static ArrayList<MidiNote> getArrayList() {
        if(values == null){
            values = values();
        }
        ArrayList<MidiNote> arrayValues = new ArrayList<>();
        Collections.addAll(arrayValues, values);
        return arrayValues;
    }
}
