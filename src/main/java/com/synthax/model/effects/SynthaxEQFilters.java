package com.synthax.model.effects;

import com.synthax.util.HelperMath;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.BiquadFilter;

/**
 * Manages filters that modify the amplitude of specific frequencies.
 * @author Joel Eriksson Sinclair
 */
public class SynthaxEQFilters {
    private static final float HP_MIN_FREQ = 400f;
    private static final float HP_MAX_FREQ = 2000f;
    private static final float LP_MIN_FREQ = 100f;
    private static final float LP_MAX_FREQ = 1500f;
    private static final float HP_DISABLED_FREQ = 50f;      // value where the filter is effectively disabled.
    private static final float LP_DISABLED_FREQ = 22000f;   // value where the filter is effectively disabled.
    private static final int FILTER_STACK_COUNT = 16;
    private static final float EQ_GAIN_MIN = -25f;
    private static final float EQ_GAIN_MAX = 25f;
    private static final float EQ_GAIN_DISABLED = 0f;
    private static final float EQ_FREQ_MIN = 200f;
    private static final float EQ_FREQ_MAX = 1800f;
    private static final float EQ_RANGE_MIN = 10f;          // q value, higher is sharper slope
    private static final float EQ_RANGE_MAX = 1f;           // q value, higher is sharper slope
    private static final int EQ_FILTER_COUNT = 3;           // 3 in GUI..

    private final BiquadFilter[] highPassFilters;
    private final BiquadFilter[] eqFilters = new BiquadFilter[EQ_FILTER_COUNT];
    private final BiquadFilter[] lowPassFilters;

    // High-pass
    private boolean hpActive = false;
    private float savedHPCutoff = HP_MIN_FREQ;
    // Low-pass
    private boolean lpActive = false;
    private float savedLPCutoff = LP_MIN_FREQ;
    // EQ
    private final boolean[] eqActive = new boolean[EQ_FILTER_COUNT];
    private final float[] eqSavedGain = new float[EQ_FILTER_COUNT];

    /**
     * Sets up the internal chain. Send the incoming audio through the HP-, EQ- and finally LP-filters.
     */
    public SynthaxEQFilters() {
        highPassFilters = new BiquadFilter[FILTER_STACK_COUNT];
        for (int i = 0; i < FILTER_STACK_COUNT; i++) {
            BiquadFilter filter = new BiquadFilter(1, BiquadFilter.BESSEL_HP);
            filter.setFrequency(HP_DISABLED_FREQ);

            // set up chaining
            if(i > 0) {
                filter.addInput(highPassFilters[i - 1]);
            }
            
            highPassFilters[i] = filter;
        }

        for (int i = 0; i < EQ_FILTER_COUNT; i++) {
            BiquadFilter eqFilter = new BiquadFilter(1, BiquadFilter.PEAKING_EQ);
            eqFilter.setGain(EQ_GAIN_DISABLED);
            eqFilter.setQ(EQ_RANGE_MIN);
            eqFilter.setFrequency(EQ_FREQ_MIN);

            // set up chaining
            if(i > 0) {
                eqFilter.addInput(eqFilters[i - 1]);
            }

            eqFilters[i] = eqFilter;
        }
        eqFilters[0].addInput(highPassFilters[FILTER_STACK_COUNT - 1]);

        lowPassFilters = new BiquadFilter[FILTER_STACK_COUNT];
        for (int i = 0; i < FILTER_STACK_COUNT; i++) {
            BiquadFilter filter = new BiquadFilter(1, BiquadFilter.BESSEL_LP);
            filter.setFrequency(LP_DISABLED_FREQ);

            // set up chaining
            if(i > 0) {
                filter.addInput(lowPassFilters[i - 1]);
            }

            lowPassFilters[i] = filter;
        }
        lowPassFilters[0].addInput(eqFilters[EQ_FILTER_COUNT - 1]);
    }

    public void setHPCutoff(float cutoff) {
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        if(hpActive) {
            setHPfreq(mapped);
        } else {
            savedHPCutoff = mapped;
        }
    }

    public void setHPActive() {
        hpActive = !hpActive;

        if(hpActive) {
            setHPfreq(savedHPCutoff);
        } else {
            savedHPCutoff = highPassFilters[0].getFrequency();
            setHPfreq(HP_DISABLED_FREQ);
        }
    }

    public void setEQActive(int i) {
        eqActive[i] = !eqActive[i];
        if(eqActive[i]) {
            eqFilters[i].setGain(eqSavedGain[i]);
        }
        else {
            eqSavedGain[i] = eqFilters[i].getGain();
            eqFilters[i].setGain(EQ_GAIN_DISABLED);
        }
    }

    public void setEQGain(int i, float newVal) {
        float gain = HelperMath.map(newVal, -50f, 50f, EQ_GAIN_MIN, EQ_GAIN_MAX);
        if(eqActive[i]) {
            eqFilters[i].setGain(gain);
        } else {
            eqSavedGain[i] = gain;
        }
    }

    public void setEQRange(int i, float newVal) {
        float qVal = HelperMath.map(newVal, 0f, 1f, EQ_RANGE_MIN, EQ_RANGE_MAX);
        eqFilters[i].setQ(qVal);
    }

    public void setEQFrequency(int i, float newVal) {
        float freq = HelperMath.map(newVal, 0f, 1f, EQ_FREQ_MIN, EQ_FREQ_MAX);
        eqFilters[i].setFrequency(freq);
    }

    public void setLPCutoff(float cutoff) {
        float mapped = HelperMath.map(cutoff, 0f, 1f, LP_MIN_FREQ, LP_MAX_FREQ);
        if(lpActive) {
            setLPfreq(mapped);
        } else {
            savedLPCutoff = mapped;
        }
    }

    public void setLPActive() {
        lpActive = !lpActive;

        if(lpActive) {
            setLPfreq(savedLPCutoff);
        } else {
            savedLPCutoff = lowPassFilters[0].getFrequency();
            setLPfreq(LP_DISABLED_FREQ);
        }
    }

    /**
     * Helper method to set frequency of the HighPass filter.
     */
    private void setHPfreq(float freq) {
        for (int i = 0; i < FILTER_STACK_COUNT; i++) {
            highPassFilters[i].setFrequency(freq);
        }
    }

    /**
     * Helper method to set frequency of the LowPass filter.
     */
    private void setLPfreq(float freq) {
        for (int i = 0; i < FILTER_STACK_COUNT; i++) {
            lowPassFilters[i].setFrequency(freq);
        }
    }

    public void addInput(UGen source) {
        if(highPassFilters[0] != null) {
            highPassFilters[0].addInput(source);
        } else {
            System.err.println("EQFilters.addInput(): Cannot add input, no filter found!");
        }
    }

    public UGen getOutput() {
        return lowPassFilters[FILTER_STACK_COUNT - 1];
    }
}
