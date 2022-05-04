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
    private final BiquadFilter filterHP;
    private final BiquadFilter filterHP2;
    private final BiquadFilter filterNotch;
    private final BiquadFilter filterLP;

    // High-pass
    private final float hpMinFreq = 800f;
    private final float hpMaxFreq = 2500f;
    private final float HPdisableFreq = 0.001f;     // value where the filter is effectively disabled.
    private boolean hpActive = false;
    private float savedHPCutoff = hpMinFreq;
    // Low-pass
    private final float lpMinFreq = 50f;
    private final float lpMaxFreq = 1500f;
    private final float LPdisableFreq = 22000f; // value where the filter is effectively disabled.
    private boolean lpActive = false;
    private float savedLPCutoff = lpMinFreq;

    public EQFilters() {
        filterHP = new BiquadFilter(1, BiquadFilter.BUTTERWORTH_HP);
        filterHP.setFrequency(HPdisableFreq);
        filterHP2 = new BiquadFilter(1, BiquadFilter.BUTTERWORTH_HP);
        filterHP2.setFrequency(HPdisableFreq);

        filterNotch = new BiquadFilter(1, BiquadFilter.NOTCH);
        //filterNotch.addInput(filterHP);
        //filterNotch.setGain(1f);

        filterLP = new BiquadFilter(1, BiquadFilter.LP);
        filterLP.addInput(filterHP);
        filterLP.setFrequency(LPdisableFreq);
    }

    public void setHPCutoff(float cutoff) {
        float mapped = BasicMath.map(cutoff, 0f, 1f, hpMinFreq, hpMaxFreq);
        if(hpActive) {
            filterHP.setFrequency(mapped);
            filterHP2.setFrequency(mapped);
        } else {
            savedHPCutoff = mapped;
            System.out.println("Saved: " + savedHPCutoff);
        }
    }

    boolean once = true;
    public void setHPSlope(float slope) {
        float mapped = BasicMath.map(slope, 0f, 1f, 0.1f, 1f);
        filterHP.setQ(mapped);

        if(once){
            filterHP2.addInput(filterHP);
            filterLP.clearInputConnections();
            filterLP.addInput(filterHP2);
            once = false;
        }
    }

    public void setHPActive(boolean newActive) {
        hpActive = newActive;

        if(hpActive) {
            filterHP.setFrequency(savedHPCutoff);
            filterHP2.setFrequency(savedHPCutoff);
        } else {
            savedHPCutoff = filterHP.getFrequency();
            System.out.println("Saved: " + savedHPCutoff);
            filterHP.setFrequency(HPdisableFreq);
            filterHP.reset();
            filterHP2.setFrequency(HPdisableFreq);
            filterHP2.reset();
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
            filterLP.setFrequency(mapped);
        } else {
            savedLPCutoff = mapped;
        }
    }

    public void setLPSlope(float slope) {
        float mapped = BasicMath.map(slope, 0f, 1f, 0.1f, 1f);
        filterLP.setQ(mapped);
    }

    public void setLPActive(boolean newActive) {
        lpActive = newActive;

        if(lpActive) {
            filterLP.setFrequency(savedLPCutoff);
        } else {
            savedLPCutoff = filterLP.getFrequency();
            filterLP.setFrequency(LPdisableFreq);
            filterLP.reset();
        }
    }

    public void addInput(UGen source) {
        filterHP.addInput(source);
    }

    public UGen getOutput() {
        return filterLP;
    }
}
