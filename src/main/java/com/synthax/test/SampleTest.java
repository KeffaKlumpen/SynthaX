package com.synthax.test;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.SamplePlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class SampleTest {

    SamplePlayer kick;
    SamplePlayer snare;
    SamplePlayer hiHat;
    SamplePlayer clap;
    Gain master;
    final AudioContext ac;

    public SampleTest(){
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        ac = new AudioContext(jsaio);
        init();
        DrumPanel drumPanel = new DrumPanel(this);
        JFrame frame = new JFrame("Drum Machine");
        frame.add(drumPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void init() {
        String sourceKick = "src/main/resources/com/synthax/samples/kick.wav";
        String sourceSnare = "src/main/resources/com/synthax/samples/snare.wav";
        String sourceHiHat = "src/main/resources/com/synthax/samples/ClHat.wav";
        String sourceClap = "src/main/resources/com/synthax/samples/clap.wav";
        Sample kickSample;
        Sample snareSample;
        Sample hiHatSample;
        Sample clapSample;
        try {
            kickSample = new Sample(sourceKick);
            snareSample = new Sample(sourceSnare);
            hiHatSample = new Sample(sourceHiHat);
            clapSample = new Sample(sourceClap);
        } catch (IOException | OperationUnsupportedException | FileFormatException e) {
            throw new RuntimeException(e);
        }
        kick = new SamplePlayer(ac, kickSample);
        Glide kickGlide = new Glide(ac, 1.0f, 20f);
        Gain kickGain = new Gain(ac, 1, kickGlide);
        kick.setKillOnEnd(false);
        kickGain.addInput(kick);

        snare = new SamplePlayer(ac, snareSample);
        Glide snareGlide = new Glide(ac, 1.0f, 20f);
        Gain snareGain = new Gain(ac, 1, snareGlide);
        snare.setKillOnEnd(false);
        snareGain.addInput(snare);

        hiHat = new SamplePlayer(ac, hiHatSample);
        Glide hiHatGlide = new Glide(ac, 1.0f, 20f);
        Gain hiHatGain = new Gain(ac, 1, hiHatGlide);
        hiHat.setKillOnEnd(false);
        hiHatGain.addInput(hiHat);

        clap = new SamplePlayer(ac, clapSample);
        Glide clapGlide = new Glide(ac, 1.0f, 20f);
        Gain clapGain = new Gain(ac, 1, clapGlide);
        clap.setKillOnEnd(false);
        clapGain.addInput(clap);

        master = new Gain(ac, 1, 0.0f);
        master.addInput(kickGain);
        master.addInput(snareGain);
        master.addInput(hiHatGain);
        master.addInput(clapGain);

        ac.out.addInput(master);
        ac.start();
    }


    public static void main(String[] args) {
        new SampleTest();
    }

    public void playKick() {
        master.setGain(1.0f);
        kick.setToLoopStart();
        kick.start();
    }

    public void playSnare() {
        master.setGain(1.0f);
        snare.setToLoopStart();
        snare.start();
    }

    public void playHiHat() {
        master.setGain(1.0f);
        hiHat.setToLoopStart();
        hiHat.start();
    }

    public void playClap() {
        master.setGain(1.0f);
        clap.setToLoopStart();
        clap.start();
    }
}

class DrumPanel extends JPanel {

    SampleTest sampleTest;
    Listeners listeners;
    JButton kick = new JButton("Kick - Press A");
    JButton snare = new JButton("Snare - Press S");
    JButton hihat = new JButton("Hi-Hat - Press F");
    JButton clap = new JButton("Clap - Press D");


    public DrumPanel(SampleTest sampleTest) {
        this.sampleTest = sampleTest;
        listeners = new Listeners(sampleTest, this);

        setPreferredSize(new Dimension(400, 400));
        setLayout(new GridLayout(2, 2));

        addListeners();
        add(kick);
        add(snare);
        add(hihat);
        add(clap);
    }

    private void addListeners() {
        kick.addActionListener(l -> sampleTest.playKick());
        kick.addKeyListener(listeners);
        snare.addActionListener(l -> sampleTest.playSnare());
        snare.addKeyListener(listeners);
        hihat.addActionListener(l -> sampleTest.playHiHat());
        hihat.addKeyListener(listeners);
        clap.addActionListener(l -> sampleTest.playClap());
        clap.addKeyListener(listeners);
    }

    public void setFocus(char c) {

        switch (c) {
            case 'A' -> kick.requestFocus(true);
            case 'S' -> snare.requestFocus(true);
            case 'D' -> clap.requestFocus(true);
            case 'F' -> hihat.requestFocus(true);
        }
    }
}

class Listeners implements KeyListener {

    SampleTest sampleTest;
    DrumPanel drumPanel;

    public Listeners(SampleTest sampleTest, DrumPanel drumPanel) {
        this.drumPanel = drumPanel;
        this.sampleTest = sampleTest;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            sampleTest.playKick();
            drumPanel.setFocus('A');
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            sampleTest.playSnare();
            drumPanel.setFocus('S');
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            sampleTest.playClap();
            drumPanel.setFocus('D');
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            sampleTest.playHiHat();
            drumPanel.setFocus('F');
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
