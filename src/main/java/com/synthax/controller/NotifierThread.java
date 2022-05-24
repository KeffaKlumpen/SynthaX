package com.synthax.controller;

class NotifierThread extends Thread {
    private final VoiceController controller;
    private final int voiceIndex;
    private final float delay;

    private boolean canceled = false;

    public NotifierThread(VoiceController controller, int voiceIndex, float delay) {
        this.controller = controller;
        this.voiceIndex = voiceIndex;
        this.delay = delay;
    }

    public void cancelNotification() {
        canceled = true;
    }

    @Override
    public void run() {
        try {
            sleep((long)delay);
            if(!canceled) {
                controller.setVoiceAvailable(voiceIndex);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}