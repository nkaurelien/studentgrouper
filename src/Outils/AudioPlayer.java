/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Outils;

import java.applet.*;
import java.awt.*;
import java.net.*;

/**
 *
 * @author nkaurelien
 */
public class AudioPlayer extends Applet {

    private AudioClip clip;
    private AppletContext context;

    public AudioPlayer(String soundpathname) throws HeadlessException {
        init(soundpathname);
    }
    

    public void init(String filepathname) {
        context = this.getAppletContext();
        String audioURL = filepathname;
        if (audioURL == null) {
            audioURL = "default.au";
        }
        try {
            URL url = new URL(this.getDocumentBase(), audioURL);
            clip = context.getAudioClip(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            context.showStatus("Could not load audio file!");
        }
    }

    public void start() {
        if (clip != null) {
            clip.play();
        }
    }
    public void loop() {
        if (clip != null) {
            clip.loop();
        }
    }
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
