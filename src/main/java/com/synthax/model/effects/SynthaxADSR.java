package com.synthax.model.effects;

/**
 * Class that handles setting the ADSR values
 * Static variables to enable usage from anywhere in the application
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 */
public class SynthaxADSR {
    public static final int ATTACK_MAX = 3000;
    public static final int DECAY_MAX = 1500;
    public static final int RELEASE_MAX = 2000;
    public static final int SUSTAIN_MAX = 1;
    public static final int ATTACK_MIN = 10;
    public static final int DECAY_MIN = 10;
    public static final int RELEASE_MIN = 10;
    public static final int SUSTAIN_MIN = 0;
    private static float attackValue = ATTACK_MIN;
    private static float decayValue = DECAY_MIN;
    private static float sustainValue = SUSTAIN_MAX;
    private static float releaseValue = RELEASE_MIN;

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
