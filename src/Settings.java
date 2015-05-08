import java.text.*;
import java.util.*;

public class Settings {

    public int[] default_levels    = {10,20,30};
    public int[] default_locations = {0,0};

    public double  catchTrials            = 0.05;
    public int     presentations          =    5;
    public int     stimulusSize           =    3;
    public int     stimulusDuration       =  200;
    public int     stimulusResponseWindow = 1500;
    public int     background             =   10;
    public boolean audioSignalLowCT       = true;

    public Vector<StimulusLevel>    levels    = new Vector<StimulusLevel>();
    public Vector<StimulusLocation> locations = new Vector<StimulusLocation>();

    public Settings() {
        addDefaultLevels();
        addDefaultLocations();
    }

    public void addDefaultLevels() {
        for (int i=0; i<default_levels.length; i++)
            levels.add(new StimulusLevel(default_levels[i]));
    }

    public void addDefaultLocations() {
        for (int i=0; i<default_locations.length; i++)
            if (i%2==0)
                locations.add(new StimulusLocation(default_locations[i],default_locations[i+1]));
    }

    public void addLevel(int n) {
        levels.add(new StimulusLevel(n));
    }

    public void addLocation(int x, int y) {
        locations.add(new StimulusLocation(x,y));
    }

    public MoCS toMoCS() {
        MoCS mocs = new MoCS();

        mocs.setLevels(levels);
        mocs.setLocations(locations);
        mocs.setPresentations(presentations);

        mocs.setStimulusDuration(stimulusDuration);
        mocs.setStimulusResponsewindow(stimulusResponseWindow);
        mocs.setStimulusSize(stimulusSize);
        mocs.setCatchTrials(catchTrials);

        mocs.generate();

        return mocs;
    }

}
