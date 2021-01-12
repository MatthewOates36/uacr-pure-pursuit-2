package org.uacr.purepursuit;

public class WayPathPoint extends PathPoint {

    private final boolean fHasHeading;

    private final double fHeading;

    public WayPathPoint(double x, double y, double distance, double curvature, double velocity) {
        super(x, y, distance, curvature, velocity);
        fHasHeading = false;
        fHeading = 0.0;
    }

    public WayPathPoint(Point point, double distance, double curvature, double velocity) {
        this(point.getX(), point.getY(), distance, curvature, velocity);
    }

    public WayPathPoint(double x, double y, double distance, double curvature, double velocity, double heading) {
        super(x, y, distance, curvature, velocity);
        fHasHeading = true;
        fHeading = heading;
    }

    public WayPathPoint(Point point, double distance, double curvature, double velocity, double heading) {
        this(point.getX(), point.getY(), distance, curvature, velocity, heading);
    }

    public boolean hasHeading() {
        return fHasHeading;
    }

    public double getHeading() {
        return fHeading;
    }
}
