/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.model;

import com.synthax.util.BasicMath;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.BiquadFilter;

public class EQFilters {
    private static final float HP_DISABLED_FREQ = 50f;      // value where the filter is effectively disabled.
    private static final float LP_DISABLED_FREQ = 22000f;   // value where the filter is effectively disabled.
    private static final int FILTER_COUNT = 16;

    private final BiquadFilter[] highPassFilters;
    private final BiquadFilter filterNotch;
    private final BiquadFilter[] lowPassFilters;

    // High-pass
    private final float hpMinFreq = 400f;
    private final float hpMaxFreq = 2000f;
    private boolean hpActive = false;
    private float savedHPCutoff = hpMinFreq;
    // Low-pass
    private final float lpMinFreq = 100f;
    private final float lpMaxFreq = 1500f;
    private boolean lpActive = false;
    private float savedLPCutoff = lpMinFreq;

    public EQFilters() {
        highPassFilters = new BiquadFilter[FILTER_COUNT];
        for (int i = 0; i < FILTER_COUNT; i++) {
            BiquadFilter filter = new BiquadFilter(1, BiquadFilter.BESSEL_HP);
            filter.setFrequency(HP_DISABLED_FREQ);

            // set up chaining
            if(i > 0) {
                filter.addInput(highPassFilters[i - 1]);
            }
            
            highPassFilters[i] = filter;
        }

        filterNotch = new BiquadFilter(1, BiquadFilter.NOTCH);
        //filterNotch.addInput(filterHP);
        //filterNotch.setGain(1f);

        lowPassFilters = new BiquadFilter[FILTER_COUNT];
        for (int i = 0; i < FILTER_COUNT; i++) {
            BiquadFilter filter = new BiquadFilter(1, BiquadFilter.BESSEL_LP);
            filter.setFrequency(LP_DISABLED_FREQ);

            // set up chaining
            if(i > 0) {
                filter.addInput(lowPassFilters[i - 1]);
            }

            lowPassFilters[i] = filter;
        }
        lowPassFilters[0].addInput(highPassFilters[FILTER_COUNT - 1]);
    }

    public void setHPCutoff(float cutoff) {
        float mapped = BasicMath.map(cutoff, 0f, 1f, hpMinFreq, hpMaxFreq);
        if(hpActive) {
            setHPfreq(mapped);
        } else {
            savedHPCutoff = mapped;
            System.out.println("Saved HP: " + savedHPCutoff);
        }
    }

    public void setHPSlope(float slope) {
        float mapped = BasicMath.map(slope, 0f, 1f, 0.1f, 1f);
        // filterHP.setQ(mapped);
    }

    public void setHPActive(boolean newActive) {
        hpActive = newActive;

        if(hpActive) {
            setHPfreq(savedHPCutoff);
        } else {
            savedHPCutoff = highPassFilters[0].getFrequency();
            System.out.println("Saved HP: " + savedHPCutoff);
            setHPfreq(HP_DISABLED_FREQ);
        }
    }

    public void setNotchFrequency(float frequency) {
        System.err.println("EQFilters.setNotchFrequency() - Not implemented.");
    }
    public void setNotchRange(float q) {
        System.err.println("EQFilters.setNotchRange() - Not implemented.");
    }
    public void setNotchActive(boolean newActive) {
        System.err.println("EQFilters.setNotchActive() - Not implemented.");
    }

    /**
     * Set the frequency for the LP-filter (all frequencies above this frequency will be lowered.
     * @param cutoff
     */
    public void setLPCutoff(float cutoff) {
        float mapped = BasicMath.map(cutoff, 0f, 1f, lpMinFreq, lpMaxFreq);
        if(lpActive) {
            setLPfreq(mapped);
        } else {
            savedLPCutoff = mapped;
            System.out.println("Saved LP: " + savedHPCutoff);
        }
    }

    public void setLPSlope(float slope) {
        float mapped = BasicMath.map(slope, 0f, 1f, 0.1f, 1f);
        //filterLP.setQ(mapped);
    }

    public void setLPActive(boolean newActive) {
        lpActive = newActive;

        if(lpActive) {
            setLPfreq(savedLPCutoff);
        } else {
            savedLPCutoff = lowPassFilters[0].getFrequency();
            System.out.println("Saved LP: " + savedLPCutoff);
            setLPfreq(LP_DISABLED_FREQ);
        }
    }

    private void setHPfreq(float freq) {
        System.out.println("Setting HP freq: " + freq);
        for (int i = 0; i < FILTER_COUNT; i++) {
            highPassFilters[i].setFrequency(freq);
        }
    }

    private void setLPfreq(float freq) {
        System.out.println("Setting LP freq: " + freq);
        for (int i = 0; i < FILTER_COUNT; i++) {
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
        return lowPassFilters[FILTER_COUNT - 1];
    }
}
