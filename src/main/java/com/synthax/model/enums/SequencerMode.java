package com.synthax.model.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Enumeration for the different Sequencer modes
 * @author Luke Eales
 * @author Axel Nilsson
 */
public enum SequencerMode {
    loop("Loop"),
    bollen("Ping-Pong"),
    reverse("Reverse"),
    random("Random");

    private final String name;


    SequencerMode(String s) {
        this.name = s;
    }

    public static SequencerMode getMode(String s) {
        SequencerMode[] arr = values();
        for (SequencerMode sequencerMode : arr) {
            if (sequencerMode.getName().equals(s)) {
                return sequencerMode;
            }
        }
        return arr[0];
    }

    public String getName() {
        return name;
    }
    public static ObservableList<String> getNames() {
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < values().length; i++) {
            array.add(values()[i].getName());
        }
        return FXCollections.observableArrayList(array);
    }
}

