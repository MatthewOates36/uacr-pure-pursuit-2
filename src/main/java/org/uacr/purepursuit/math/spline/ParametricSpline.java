package org.uacr.purepursuit.math.spline;

import org.uacr.purepursuit.math.Vector;

import java.util.Arrays;

public class ParametricSpline {

    public ParametricSpline() {
        double[] t = {0, 1, 2, 3, 4};
        double[] x = {0, 10, 15, 5, 0};
        double[] y = {0, 5, 15, 10, 0};

        Vector startSlope = new Vector(2, 1).normalize();

        System.out.println(startSlope.getX());
        System.out.println(startSlope.getY());

        Spline sX = new Spline(t, x, startSlope);
        Spline sY = new Spline(t, y, startSlope);

        double min = Arrays.stream(t).min().orElse(0);
        double max = Arrays.stream(t).max().orElse(0);

        double delta = max - min;
        double step = delta / 100;

        for (int i = 0; i < 101; i++) {
            System.out.print("(" + sX.eval(min + i * step) + "," + sY.eval(min + i * step) + "),");
        }
        System.out.print("\b");
    }
}
