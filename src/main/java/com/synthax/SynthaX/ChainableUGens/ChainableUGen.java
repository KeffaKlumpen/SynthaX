/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.SynthaX.ChainableUGens;

import net.beadsproject.beads.core.UGen;

public abstract class ChainableUGen {
    protected UGen output;

    public abstract void setup();

    public UGen getOutput(){
        return output;
    }
    public void setInput(UGen input){
        output.clearInputConnections();
        output.addInput(input);
    }
    public abstract void setNote(float hertz);
}
