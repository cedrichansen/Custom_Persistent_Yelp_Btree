public class Point {

    double lattitude, longitude;
    String name;
    int cluster;

    public Point(double x, double y) {
        longitude = x;
        lattitude = y;
    }

    public Point (double x, double y, String n){
        lattitude = x;
        longitude = y;
        name = n;
    }

    public double getDistance(Point center){
        return Math.sqrt((Math.pow((this.lattitude- center.lattitude),2)) +
                (Math.pow((this.longitude- center.longitude),2)));
    }

    public String toString() {
        return "[" + lattitude + "]" + "[" + longitude + "]"  + name;
    }

}
