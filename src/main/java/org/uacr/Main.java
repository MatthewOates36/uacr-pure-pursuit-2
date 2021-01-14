package org.uacr;

import org.uacr.purepursuit.Path;
import org.uacr.purepursuit.math.Point;
import org.uacr.purepursuit.math.Vector;
import org.uacr.purepursuit.math.spline.Spline;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
//        Path p = new Path(
//                new Point(0, 0),
//                new Point(40, 20),
//                new Point(60, 40),
//                new Point(40, 60),
//                new Point(20, 40),
//                new Point(0, 0)
//        );
//        p.setPathSmoothing(0.9);
//        System.out.println(p.getPoints());

        double[] t = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        double[] x = {0, 10, 10, 20, 20, 10, 10, 0, 0};
        double[] y = {0, 0, 10, 10, 0, 0, 10, 10, 0};

        Vector startSlope = new Vector(2, 1).normalize();

        System.out.println(startSlope.getX());
        System.out.println(startSlope.getY());

        Spline sX = new Spline(t, x, new Vector(new Point(1, 11)));
        Spline sY = new Spline(t, y, new Vector(new Point(1, 0)));

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
