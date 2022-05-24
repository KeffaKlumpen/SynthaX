package com.synthax.controller;

import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.SequencerMode;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.sequencer.SequencerStep;
import com.synthax.util.HelperMath;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsible for saving and loading the variables of a sequencers steps into .dat files (presets).
 * @see Sequencer
 * @see SequencerStep
 * @author Joel Eriksson Sinclair
 */
public class SeqPresetLoader {
    private final static String PRESET_FILE_EXTENSION = ".stx";
    private final static int PRESET_VERSION_ID = 1;
    private final static int PRESET_UID = 133769420;

    private final Sequencer sequencer;
    private File presetRoot = new File("src/main/resources/com/synthax/sequencer_presets");

    public SeqPresetLoader(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public String getPresetRootPath() {
        return presetRoot.getPath();
    }

    // TODO: 2022-05-18 Call this from the settings panel
    // TODO: Move the fileChooser to view class..
    public void setPresetRoot() {
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
                    if(childName.endsWith(PRESET_FILE_EXTENSION)) {
                        childName = childName.substring(0, childName.length() - 4);
                        presetNames.add(childName);
                    }
                }
            }
        }

        return presetNames.toArray(new String[0]);
    }

    public boolean presetExists(String presetName) {
        File presetFile = getFileFromPresetName(presetName);
        boolean fileExists = false;

        File[] children = presetRoot.listFiles();
        if(children != null) {
            for (File child : children) {
                if(child.equals(presetFile)) {
                    fileExists = true;
                    break;
                }
            }
        }

        return fileExists;
    }

    public String getIndexedName(String name) {
        File[] children = presetRoot.listFiles();
        int presetCounter = -1;
        if(children != null) {
            Arrays.sort(children);

            for (File child : children) {
                String childName = child.getName();
                System.out.println(childName);

                if (childName.equals(name + "-" + (presetCounter + 1) + PRESET_FILE_EXTENSION)) {
                    System.out.println("preset file found.");
                    presetCounter++;
                }
            }

            System.out.println("presetCounter: " + presetCounter);
            name = name + "-" + (presetCounter + 1);
        }

        return name;
    }

    /**
     * Save the current sequencer setting to a file.
     * @param presetName Name of the currently selected preset
     */
    public void savePreset(String presetName) {
        if(presetName != null) {
            File saveFile = getFileFromPresetName(presetName);
            savePreset(saveFile);
        }
    }

    /**
     * Attempt to load a preset with the given name, searching in the current presetRoot for a matching .dat file.
     * @param presetName Name of preset to be loaded
     */
    public void loadPreset(String presetName) {
        File presetToLoad = getFileFromPresetName(presetName);
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
            System.err.println("SeqPresetLoader.loadPreset(String): Preset with matching name " + presetName + " not found.");
        }
    }

    public void deleteFile(String presetName) {
        if(presetName != null) {
            File[] children = presetRoot.listFiles();
            if(children != null) {
                File deleteFile = getFileFromPresetName(presetName);

                for (File child : children) {
                    if(child.equals(deleteFile)) {
                        if(deleteFile.delete()) {
                            System.out.println(presetName + " deleted.");
                        } else {
                            System.err.println(presetName + " could not be deleted.");
                        }
                    }
                }
            } else {
                System.err.println("SeqPresetLoader.deletePreset(): Root has no children.");
            }
        }
    }

    private void savePreset(File saveFile) {
        System.out.println("SAVING: " + saveFile);
        SequencerStep[] steps = sequencer.getSteps();

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile))) {
            dos.writeInt(PRESET_UID);
            dos.writeInt(PRESET_VERSION_ID);
            dos.writeInt(steps.length);
            for (SequencerStep step : steps) {
                dos.writeBoolean(step.isOn());
                dos.writeInt(step.getVelocity());
                dos.writeFloat(step.getDetuneCent());
                dos.writeInt(step.getMidiNote().ordinal());
            }
            dos.writeFloat(sequencer.getRate());
            dos.writeInt(sequencer.getNSteps());
            dos.writeInt(sequencer.getSequencerMode().ordinal());
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPreset(File loadFile) {
        System.out.println("LOADING: " + loadFile);
        SequencerStep[] steps = sequencer.getSteps();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(loadFile))){
            int presetUID = dis.readInt();
            if(presetUID != PRESET_UID) {
                System.err.println("Not a valid preset.");
                return;
            }

            int presetVersion = dis.readInt();
            if(presetVersion != PRESET_VERSION_ID) {
                System.err.println("Preset not correct version.");
                return;
            }

            int stepCount = dis.readInt();
            assert stepCount == steps.length;

            for (SequencerStep step : steps) {
                step.setIsOn(dis.readBoolean());
                float velocity = HelperMath.map(dis.readInt(), 0, 127, 0f, 1f);
                step.setVelocity(velocity);
                step.setDetuneCent(dis.readFloat());
                step.setMidiNote(MidiNote.values()[dis.readInt()]);
            }
            sequencer.setBPM(dis.readFloat());
            sequencer.setNSteps(dis.readInt());
            sequencer.setSequencerMode(SequencerMode.values()[dis.readInt()]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFileFromPresetName(String presetName) {
        return new File(presetRoot, presetName + PRESET_FILE_EXTENSION);
    }

}
