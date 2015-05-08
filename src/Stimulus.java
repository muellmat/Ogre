import opi.*;

public class Stimulus {

    public final static int STIM_NORMAL     = 0;
    public final static int STIM_CATCH_LOW  = 1;
    public final static int STIM_CATCH_HIGH = 2;

    private OpiStaticStimulus stim;
    private int type;

    public Stimulus(OpiStaticStimulus stim, int type) {
        this.stim = stim;
        this.type = type;
    }

    public OpiStaticStimulus getStimulus() {
        return stim;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        String typeStr = "N    ";
        if (type == STIM_CATCH_LOW)
            typeStr = "  L  ";
        if (type == STIM_CATCH_HIGH)
            typeStr = "    H";
        return String.format("(%+2d,%+2d)   -%2d dB    %3d ms    %4d ms    %d    %d    [%s]",
                (int)stim.getX()/10,(int)stim.getY()/10,(int)stim.getLevel()/10,
                (int)stim.getDuration(),(int)stim.getResponseWindow(),
                (int)stim.getSize(),(int)stim.getColor(),typeStr);
    }

}
