import java.util.*;

import opi.*;

public class MoCS {

    private Vector<Stimulus>         stimuli;
    private Vector<StimulusLocation> locations;
    private Vector<StimulusLevel>    levels;

    private int presentations  = 0;
    private double catchTrials = 0.40;

    private int totalStimuli     = 0;
    private int totalCatchTrials = 0;
    private int nqa              = 0;

    private int stimulusSize           =    3;
    private int stimulusDuration       =  200;
    private int stimulusResponsewindow = 1500;

    public MoCS() {
        stimuli   = new Vector<Stimulus>();
        locations = new Vector<StimulusLocation>();
        levels    = new Vector<StimulusLevel>();
    }

    public void setLocations(Vector<StimulusLocation> locations) {
        this.locations = locations;
    }

    public void setLevels(Vector<StimulusLevel> levels) {
        this.levels = levels;
    }

    public void setPresentations(int presentations) {
        this.presentations = presentations;
    }

    public void setCatchTrials(double catchTrials) {
        this.catchTrials = catchTrials;
    }

    public void setStimulusSize(int stimulusSize) {
        this.stimulusSize = stimulusSize;
    }

    public void setStimulusDuration(int stimulusDuration) {
        this.stimulusDuration = stimulusDuration;
    }

    public void setStimulusResponsewindow(int stimulusResponsewindow) {
        this.stimulusResponsewindow = stimulusResponsewindow;
    }

    public void generate() {
        //totalStimuli     = presentations * locations.size() * levels.size();
        //totalCatchTrials = 2 * (int)Math.ceil(totalStimuli*catchTrials);

        totalStimuli = presentations * locations.size() * levels.size();
        nqa = (int) (totalStimuli/(1.0-2*catchTrials));
        totalCatchTrials = 2 * (int)Math.ceil(nqa*catchTrials);

        stimuli = new Vector<Stimulus>();

        for (int i=0; i<presentations; i++) {
            for (int j=0; j<locations.size(); j++) {
                for (int k=0; k<levels.size(); k++) {
                    int x = locations.get(j).getX();
                    int y = locations.get(j).getY();
                    double l = levels.get(k).getLevelForOPI();

                    OpiStaticStimulus staticStim = new OpiStaticStimulus(x,y,l);
                    staticStim.setSize(stimulusSize);
                    staticStim.setDuration(stimulusDuration);
                    staticStim.setResponseWindow(stimulusResponsewindow);

                    Stimulus stimulus = new Stimulus(staticStim,Stimulus.STIM_NORMAL);

                    stimuli.add(stimulus);
                }
            }
        }

        StimulusLevel levelLow  = new StimulusLevel(40);
        StimulusLevel levelHigh = new StimulusLevel(10);
        double l = 0.0;
        int type = 0;
        for (int i=0; i<totalCatchTrials; i++) {
            type = i%2+1;

            if (type==Stimulus.STIM_CATCH_LOW)
                l = levelLow.getLevelForOPI();
            if (type==Stimulus.STIM_CATCH_HIGH)
                l = levelHigh.getLevelForOPI();

            OpiStaticStimulus staticStim = new OpiStaticStimulus(0,0,l);
            staticStim.setSize(stimulusSize);
            staticStim.setDuration(stimulusDuration);
            staticStim.setResponseWindow(stimulusResponsewindow);

            Stimulus stimulus = new Stimulus(staticStim,type);

            stimuli.add(stimulus);
        }

        Collections.shuffle(stimuli);
        Collections.shuffle(stimuli);
        Collections.shuffle(stimuli);
    }

    public int numberOfStimuli() {
        return totalStimuli;
    }

    public int numberOfCatchTrials() {
        return totalCatchTrials;
    }

    public int numberQuestionsAsked() {
        return nqa;
    }

    public Vector<Stimulus> getStimuli() {
        return stimuli;
    }

    public String toString() {
        String str = String.format("  %4s    %7s    %5s    %6s    %7s %4s  %3s    %7s\r\n",
                "Nr","Loc","Lvl","Dur","Resp","Size","Col","Type");
        for (int i=0; i<stimuli.size(); i++) {
            str = str.concat(String.format("  %4d    %s\r\n",i+1,stimuli.get(i).toString()));
        }
        return str;
    }

    public String toCSV() {
        String str = "Nr;X;Y;Lvl;Dur;Resp;Size;Col;Type\r\n";
        for (int i=0; i<stimuli.size(); i++) {
            str = str.concat(String.format(
                    "%d;%d;%d;%d;%d;%d;%d;%d;%d\r\n",
                    i+1,
                    (int)stimuli.get(i).getStimulus().getX()/10,
                    (int)stimuli.get(i).getStimulus().getY()/10,
                    (int)stimuli.get(i).getStimulus().getLevel()/10,
                    (int)stimuli.get(i).getStimulus().getDuration(),
                    (int)stimuli.get(i).getStimulus().getResponseWindow(),
                    (int)stimuli.get(i).getStimulus().getSize(),
                    (int)stimuli.get(i).getStimulus().getColor(),
                    (int)stimuli.get(i).getType()
            ));
        }
        return str;
    }
}
