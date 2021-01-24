package org.uacr;

import org.uacr.purepursuit.Path;
import org.uacr.purepursuit.SplinePath;
import org.uacr.purepursuit.math.Point;

public class Main {

    public static void main(String[] args) {
        Path p = new SplinePath(
                new Point(0, 0),
                new Point(102, 0),
                new Point(138, -36),
                new Point(102, -72),
                new Point(66, -36),
                new Point(102, 0),
                new Point(192, -12),
                new Point(228, 24),
                new Point(192, 60),
                new Point(156, 24),
                new Point(220, -72),
                new Point(288, -36),
                new Point(240, 0),
                new Point(192, -12),
                new Point(102, 0),
                new Point(0, 0)
        );
        System.out.println(p.getPoints());
    }
}