// http://stackoverflow.com/questions/3780406/how-to-play-a-sound-alert-in-a-java-application
// http://en.wikipedia.org/wiki/Charge_(fanfare)
// http://de.wikipedia.org/wiki/Frequenzen_der_gleichstufigen_Stimmung
// http://hofa-college.de/online_campus/pages/tipps-amp-tricks/hofa-tools/bpm-in-msec.php

import javax.sound.sampled.*;

public class SoundUtils {

    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs) throws LineUnavailableException {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol)
            throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(SAMPLE_RATE, // sampleRate
                8, // sampleSizeInBits
                1, // channels
                true, // signed
                false); // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public static void main(String[] args) throws Exception {
		/*
	    SoundUtils.tone(1000,100);
	    Thread.sleep(1000);
	    SoundUtils.tone(100,1000);
	    Thread.sleep(1000);
	    SoundUtils.tone(5000,100);
	    Thread.sleep(1000);
	    SoundUtils.tone(400,500);
	    Thread.sleep(1000);
	    SoundUtils.tone(400,500, 0.2);
		SoundUtils.tone( 783,100);
		SoundUtils.tone(1046,100);
		SoundUtils.tone(1318,100);
		SoundUtils.tone(1567,150);
		Thread.sleep(25);
		SoundUtils.tone(1318,100);
		SoundUtils.tone(1567,400);
		Thread.sleep(200);
		SoundUtils.tone(1567,100);
		SoundUtils.tone(2093,100);
		SoundUtils.tone(2637,100);
		SoundUtils.tone(3135,150);
		Thread.sleep(25);
		SoundUtils.tone(2637,100);
		SoundUtils.tone(3135,400);
		*/
    }
}