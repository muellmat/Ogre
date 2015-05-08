public class StimulusLevel {

    private double level = 10.0;

    public StimulusLevel(double level) {
        this.level = level;
    }

    public double getLevelinDB() {
        return this.level;
    }

    public double getLevelForOPI() {
        return this.level * 10;
    }

    public String toString() {
        return String.format("-%2d dB",(int)this.level);
    }

}
