package com.synthax.controller;

import com.synthax.model.enums.MidiNote;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.sequencer.SequencerStep;
import com.synthax.util.HelperMath;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsible for saving and loading the variables of a sequencers steps into .dat files (presets).
 * @see Sequencer
 * @see SequencerStep
 * @author Joel Eriksson Sinclair
 */
public class SeqPresetLoader {
    private final Sequencer sequencer;
    private File presetRoot = new File("src/main/resources/com/synthax/sequencer_presets");

    public SeqPresetLoader(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public String getPresetRootPath() {
        return presetRoot.getPath();
    }

    // TODO: 2022-05-18 Call this from the settings panel
    public void browseAndSetPresetRoot() {
        JFileChooser fileChooser = new JFileChooser(presetRoot);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(fileChooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
            presetRoot = fileChooser.getSelectedFile();
        }
    }

    /**
     * Iterates through the presetRoot to find .dat files.
     * @return The names of found .dat files, without suffix.
     */
    public String[] getPresetNames() {
        ArrayList<String> presetNames = new ArrayList<>();

        File[] children = presetRoot.listFiles();
        if(children != null) {
            for(File child : children) {
                if(child.isFile()) {
                    String childName = child.getName();
                    if(childName.endsWith(".dat")) {
                        childName = childName.substring(0, childName.length() - 4);
                        presetNames.add(childName);
                    }
                }
            }
        }

        return presetNames.toArray(new String[0]);
    }

    /**
     * Attempt to load a preset with the given name, searching in the current presetRoot for a matching .dat file.
     * @param presetName Name of preset to be loaded
     */
    public void loadPreset(String presetName) {
        File presetToLoad = new File(presetRoot.getPath(), presetName + ".dat");
        boolean loadOk = false;

        File[] children = presetRoot.listFiles();
        if(children != null) {
            for(File child : children) {
                if(child.equals(presetToLoad)) {
                    loadPreset(presetToLoad);
                    loadOk = true;
                    break;
                }
            }
        }

        if(!loadOk) {
            System.err.println("SeqPresetLoader.loadPreset(String): Preset with matching name not found.");
        }
    }

    /**
     * Save the current sequencer setting, either with a new name or overwriting.
     * @param currentPresetName Name of the currently selected preset
     */
    public void savePreset(String currentPresetName) {
        String presetName = JOptionPane.showInputDialog(null, "Preset Name:", currentPresetName);
        if(presetName != null) {
            File[] children = presetRoot.listFiles();
            if(children != null) {
                File saveFile = new File(presetRoot.getPath(), presetName + ".dat");
                boolean fileExist = false;
                for (File child : children) {
                    if(child.equals(saveFile)) {
                        fileExist = true;
                        int answer = JOptionPane.showConfirmDialog(null, "Preset already exists, do you want to overwrite?");
                        if(answer == JOptionPane.YES_OPTION) {
                            savePreset(saveFile);
                        }
                        else if(answer == JOptionPane.NO_OPTION) {
                            savePresetAsNew(currentPresetName);
                        }
                    }
                }
                if (!fileExist) {
                    savePreset(saveFile);
                }
            } else {
                System.err.println("SeqPresetLoader.savePreset(): Root has no children.");
            }
        }
    }

    private void savePresetAsNew(String name) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getIndexedName(String name) {
        File[] children = presetRoot.listFiles();
        int presetCounter = -1;
        if(children != null) {
            Arrays.sort(children);

            for (File child : children) {
                String childName = child.getName();
                System.out.println(childName);

                if (childName.equals(name + "-" + (presetCounter + 1) + ".dat")) {
                    System.out.println("preset file found.");
                    presetCounter++;
                }
            }

            System.out.println("presetCounter: " + presetCounter);
            name = name + "-" + (presetCounter + 1);
        }

        return name;
    }

    public void deleteFile(String presetName) {
        if(presetName != null) {
            File[] children = presetRoot.listFiles();
            if(children != null) {
                File deleteFile = new File(presetRoot.getPath(), presetName + ".dat");

                for (File child : children) {
                    if(child.equals(deleteFile)) {
                        deleteFile.delete();
                    }
                }
            } else {
                System.err.println("SeqPresetLoader.savePreset(): Root has no children.");
            }
        }

    }
}
