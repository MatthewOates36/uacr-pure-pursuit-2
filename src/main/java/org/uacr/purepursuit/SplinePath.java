package org.uacr.purepursuit;

import org.uacr.purepursuit.math.Point;
import org.uacr.purepursuit.math.spline.ParametricSpline;

import java.util.ArrayList;
import java.util.Arrays;

public class SplinePath extends Path {

    /**
     * Pass in an ArrayList of waypoints
     */
    public SplinePath(ArrayList<Point> points) {
        mPoints = points;
    }

    /**
     * Pass in a comma separated list or array of waypoints
     */
    public SplinePath(Point... points) {
        this(new ArrayList<>(Arrays.asList(points)));
    }


    @Override
    protected void fill() {
        ParametricSpline spline = new ParametricSpline(mPoints);
        mPoints = spline.getPoints(getPointSpacing());
    }

    @Override
    protected void smooth() {
        // No smoothing needed with spline path
    }
}
