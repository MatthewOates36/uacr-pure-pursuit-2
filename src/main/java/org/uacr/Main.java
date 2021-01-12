package org.uacr;

import org.uacr.purepursuit.Path;
import org.uacr.purepursuit.Point;

public class Main {

    public static void main(String[] args) {
        Path p = new Path(
                new Point(0, 0),
                new Point(40, 20),
                new Point(60, 40),
                new Point(40, 60),
                new Point(20, 40),
                new Point(0, 0)
        );
        p.setPathSmoothing(0.9);
        System.out.println(p.getPoints());
    }
}
