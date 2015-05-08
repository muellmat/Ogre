public class StimulusLocation {

    private int x = 0;
    private int y = 0;

    public StimulusLocation(int x,int y) {
        this.x = x*10;
        this.y = y*10;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String toString() {
        return String.format("(%+03d,%+03d)",this.x,this.y);
    }

}
