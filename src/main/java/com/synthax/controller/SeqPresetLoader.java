package com.synthax.controller;

import com.synthax.model.enums.MidiNote;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.sequencer.SequencerStep;
import com.synthax.util.HelperMath;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Arrays;

/**
 * Responsible for saving and loading the variables of a sequencers steps into .dat files (presets).
 * @see Sequencer
 * @see SequencerStep
 * @author Joel Eriksson Sinclair
 */
public class SeqPresetLoader {
    private static final FileNameExtensionFilter FILE_FILTER = new FileNameExtensionFilter("Presets", "dat");

    private final Sequencer sequencer;
    private File presetRoot = new File("src/main/resources/com/synthax/sequencer_presets");
    private int currentPreset = -1;

    public SeqPresetLoader(Sequencer sequencer) {
        this.sequencer = sequencer;
        File[] children = presetRoot.listFiles();
        if(children != null) {
            int childCount = children.length;
            if(childCount > 0) {
                currentPreset = 0;
            }
        }
    }

    public void browseAndSetPresetRoot() {
        JFileChooser fileChooser = new JFileChooser(presetRoot);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(fileChooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            presetRoot = selectedDirectory;
        }
    }

    public void reloadPreset() {
        if(currentPreset >= 0) {
            loadPreset(currentPreset);
        }
        else {
            System.err.println("SeqPresetLoader.reloadPreset(): currentPreset is < 0.");
        }
    }

    public void loadNextPreset() {
        File[] children = presetRoot.listFiles();
        if(children != null) {
            int childCount = children.length;

            if(childCount > 0) {
                System.out.println("pre: " + currentPreset);

                currentPreset = ++currentPreset % childCount;

                System.out.println("post: " + currentPreset);

                loadPreset(currentPreset);
            }
        }
    }

    public void loadPresetFromFile() {
        JFileChooser fileChooser = new JFileChooser(presetRoot);
        fileChooser.setFileFilter(FILE_FILTER);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if(fileChooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadPreset(selectedFile);
        }
    }

    public void savePreset() {
        File saveFile;
        File[] children = presetRoot.listFiles();
        if(children != null) {
            saveFile = children[currentPreset];
            savePreset(saveFile);
        } else {
            System.err.println("SeqPresetLoader.savePreset(): Root has no children.");
        }
    }

    public void savePresetAsNew(String name) {
        name = getIndexedName(name);

        File saveFile = new File(presetRoot, name + ".dat");
        savePreset(saveFile);
    }

    private void savePreset(File saveFile) {
        System.out.println("SAVING: " + saveFile);
        SequencerStep[] steps = sequencer.getSteps();

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile))) {
            dos.writeInt(steps.length);
            for (SequencerStep step : steps) {
                dos.writeBoolean(step.isOn());
                dos.writeInt(step.getVelocity());
                dos.writeFloat(step.getDetuneCent());
                dos.writeInt(step.getMidiNote().ordinal());
            }
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPreset(int index) {
        File loadFile;
        File[] children = presetRoot.listFiles();
        if(children != null) {
            if(index >= 0 && index < children.length) {
                loadFile = children[index];
                loadPreset(loadFile);
            } else {
                System.err.println("SeqPresetLoader.loadPreset(int): Index is out of bounds.");
            }
        } else {
            System.err.println("SeqPresetLoader.loadPreset(int): Root has no children.");
        }
    }

    private void loadPreset(File loadFile) {
        System.out.println("LOADING: " + loadFile);
        SequencerStep[] steps = sequencer.getSteps();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(loadFile))){
            int stepCount = dis.readInt();
            assert stepCount == steps.length;

            for (SequencerStep step : steps) {
                step.setIsOn(dis.readBoolean());
                float velocity = HelperMath.map(dis.readInt(), 0, 127, 0f, 1f);
                step.setVelocity(velocity);
                step.setDetuneCent(dis.readFloat());
                step.setMidiNote(MidiNote.values()[dis.readInt()]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getIndexedName(String name) {
        File[] children = presetRoot.listFiles();
        int presetCounter = -1;
        if(children != null) {
            Arrays.sort(children);
            int childCount = children.length;

            for (int i = 0; i < childCount; i++) {
                String childName = children[i].getName();
                System.out.println(childName);

                if(childName.equals(name + "-" + (presetCounter + 1) + ".dat")){
                    System.out.println("preset file found.");
                    presetCounter++;
                }
            }

            System.out.println("presetCounter: " + presetCounter);
            name = name + "-" + (presetCounter + 1);
        }

        return name;
    }
}
