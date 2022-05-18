/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.model;

/**
 * Class that handles setting the ADSR values
 * Static variables to enable usage from anywhere in the application
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 */
public class ADSRValues {
    private static float attackValue = 10;
    private static float decayValue = 10;
    private static float sustainValue = 1f;
    private static float releaseValue = 10;
    private static float peakGain = 0.05f;

    public static float getAttackValue() {
        return attackValue;
    }
    public static float getDecayValue() {
        return decayValue;
    }
    public static float getSustainValue() {
        return sustainValue;
    }
    public static float getReleaseValue() {
        return releaseValue;
    }

    // ADSR.peakGain should always be 1(?) ..Or be handled by individual oscillators gain!
    public static float getPeakGain() {
        return peakGain;
    }

    public static void setAttackValue(float attackValue) {
        ADSRValues.attackValue = attackValue;
    }
    public static void setDecayValue(float decayValue) {
        ADSRValues.decayValue = decayValue;
    }
    public static void setSustainValue(float sustainValue) {
        ADSRValues.sustainValue = sustainValue;
    }
    public static void setReleaseValue(float releaseValue) {
        ADSRValues.releaseValue = releaseValue;
    }

    // This should be synth.setMasterGain(gain) instead of going through ADSR stuff..
    // ADSR.peakGain should always be 1(?) ..Or be handled by individual oscillators gain!
    public static void setPeakGain(float peakGain) {
        //ADSRValues.peakGain = peakGain;
        System.err.println("ADSRValues.setPeakGain(float) - disabled!!");
    }
}
