import opi.Opi;
import opi.OpiPresentReturn;
import opi.OpiStaticStimulus;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public String pathSettings = "C:\\ProgramData\\Haag-Streit\\EyeSuite";
    public String pathBase = "D:\\Data\\Ogre";
    public String pathExamination = "";

    public File fdBase;
    public File fdExamination;

    public int examinationID = 0;

    public Opi opi;

    public long start   = System.currentTimeMillis();
    public long current = System.currentTimeMillis();
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public Main(String eye, int settingNumber) throws IOException {
        System.out.println(">>> Ogre started: "+eye+" eye");

        // create base directory
        fdBase = new File(pathBase);
        if (!fdBase.exists()) {
            fdBase.mkdirs();
        }

        // get next examination ID and create directory
        FileFilter fileFilter = new WildcardFileFilter("[0-9]+");
        File[] files = fdBase.listFiles(fileFilter);
        Arrays.sort(files, Collections.reverseOrder());
        for (File f : files) {
            if (f.isDirectory()) {
                examinationID = Integer.parseInt(f.getName());
                break;
            }
        }
        examinationID += 1;
        pathExamination = String.format("%s\\%03d",pathBase,examinationID);
        fdExamination = new File(pathExamination);
        if (!fdExamination.exists())
            fdExamination.mkdirs();

        // settings and mocs
        Settings setting = new Settings();
        switch (settingNumber) {
            case 1:
                int[] locations1_left = {
                        +5,+5,
                        -3,-6
                };
                int[] locations1_right = {
                        -5,+5,
                        +3,-6
                };
                int[] levels1                  = {3,6,9,12,15,18,21,24,27,30,33,36,39};
                setting.catchTrials            =  0.40;
                setting.presentations          =    30;
                setting.stimulusSize           =     3;
                setting.stimulusDuration       =   200;
                setting.stimulusResponseWindow =  1500;
                setting.background             =    10;
                setting.audioSignalLowCT       =  true;
                setting.levels.clear();
                setting.locations.clear();
                setting.default_levels = levels1;
                if (eye.equals("left"))  setting.default_locations = locations1_left;
                if (eye.equals("right")) setting.default_locations = locations1_right;
                setting.addDefaultLevels();
                setting.addDefaultLocations();
                break;
            case 2:
                int[] locations2_left = {
                        +5,+5,
                        -3,-6,
                        +7,-3,
                        -2,+7
                };
                int[] locations2_right = {
                        -5,+5,
                        +3,-6,
                        -7,-3,
                        +2,+7
                };
                int[] levels2                  = {5,10,15,20,25,30,35};
                setting.catchTrials            =  0.40;
                setting.presentations          =    30;
                setting.stimulusSize           =     3;
                setting.stimulusDuration       =   200;
                setting.stimulusResponseWindow =  1500;
                setting.background             =    10;
                setting.audioSignalLowCT       =  true;
                setting.levels.clear();
                setting.locations.clear();
                setting.default_levels = levels2;
                if (eye.equals("left"))  setting.default_locations = locations2_left;
                if (eye.equals("right")) setting.default_locations = locations2_right;
                setting.addDefaultLevels();
                setting.addDefaultLocations();
                break;
        }
        MoCS mocs = setting.toMoCS();

        // init perimeter
        String[] secretParameters = {"-QuickStimulusOff"};
        opi = new Opi(pathSettings,eye,secretParameters);
        int status = 0;
        switch (setting.background) {
            case   0: status = opi.opiInitialize(opi.BG_OFF,2); break;
            case   1: status = opi.opiInitialize(opi.BG_1,  2); break;
            case  10: status = opi.opiInitialize(opi.BG_10, 2); break;
            case 100: status = opi.opiInitialize(opi.BG_100,2); break;
        }
        switch (status) {
            case 0:
                System.out.println("opiInitialize: 0 success");
                break;
            case 1:
                System.out.println("opiInitialize: 1 alread initialized");
                break;
            case 2:
                System.err.println("opiInitialize: 2 initialization failed");
                System.exit(2);
                break;
        }

        // start gaze tracker capture
        opi.getGazeTracker().startRawFrameCapture(pathExamination,String.format("%03d.csv",examinationID));

        // examination
        Stimulus stim1 = null;
        Stimulus stim2 = null;
        OpiPresentReturn ret  = null;
        OpiStaticStimulus stim = null;
        OpiStaticStimulus next = null;
        for (int i=0; i<mocs.numberTotal(); i++) {
            stim1 = mocs.getStimuli().get(mod(i+0,mocs.numberOfStimuli()));
            stim2 = mocs.getStimuli().get(mod(i+1,mocs.numberOfStimuli()));
            stim  = stim1.getStimulus();
            next  = stim2.getStimulus();

            if (setting.audioSignalLowCT && stim1.getType()==Stimulus.STIM_CATCH_LOW) {
                try {
                    SoundUtils.tone(1000,100);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }

            ret = opi.opiPresent(stim,next);

            System.out.printf("%04d stim 1: %s\n%04d stim 2: %s\n\n",i,stim,i,next);
        }

        // stop gaze tracker capture
        opi.getGazeTracker().stopRawFrameCapture();
        System.exit(0);

        /*
        Signal.handle(new Signal("INT"), new SignalHandler() {
            // Signal handler method
            public void handle(Signal signal) {
                opi.getGazeTracker().stopRawFrameCapture();
                //System.out.println("Got signal" + signal);
                System.exit(0);
            }
        });
        */
    }

    public static int mod(int n, int m) {
        // http://stackoverflow.com/questions/5385024/mod-in-java-produces-negative-numbers
        // http://stackoverflow.com/questions/4412179/best-way-to-make-javas-modulus-behave-like-it-should-with-negative-numbers
        return (((n%m)+m)%m);
    }

    public static void main(String[] args) {
        if (args.length!=2) {
            System.err.println("two parameters needed");
            System.exit(0);
        }
        String eye = args[0];
        int settingNumber = Integer.parseInt(args[1]);
        //System.out.println(StringUtils.join(args,"|"));
        //System.out.println(settingNumber);
        if (!(eye.equals("left")||eye.equals("right"))) {
            System.err.println("left or right eye");
            System.exit(0);
        }
        if (settingNumber<1 && settingNumber>2) {
            System.err.println("setting number range: [1,2]");
            System.exit(0);
        }
        try {
            new Main(eye,settingNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
