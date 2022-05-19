package com.synthax.model;

/**
 * Class that handles setting the ADSR values
 * Static variables to enable usage from anywhere in the application
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 */
public class SynthaxADSR {
    private static float attackValue = 10;
    private static float decayValue = 10;
    private static float sustainValue = 1f;
    private static float releaseValue = 10;

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

    public static void setAttackValue(float attackValue) {
        SynthaxADSR.attackValue = attackValue;
    }

    public static void setDecayValue(float decayValue) {
        SynthaxADSR.decayValue = decayValue;
    }

    public static void setSustainValue(float sustainValue) {
        SynthaxADSR.sustainValue = sustainValue;
    }

    public static void setReleaseValue(float releaseValue) {
        SynthaxADSR.releaseValue = releaseValue;
    }
}
