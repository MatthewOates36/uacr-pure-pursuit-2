package org.uacr.purepursuit;

import java.util.Arrays;

public class Spline {

    private double[] x;
    private double[] y;
    private double[] dy;

    public Spline(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        dy = getNaturalDerivatives(x, y);
    }

    public double eval(double xVal) {
        int i = 1;
        while (x[i] < xVal) {
            i++;
        }

        double t = (xVal - x[i - 1]) / (x[i] - x[i - 1]);

        double a = dy[i - 1] * (x[i] - x[i - 1]) - (y[i] - y[i - 1]);
        double b = -dy[i] * (x[i] - x[i - 1]) + (y[i] - y[i - 1]);

        return (1 - t) * y[i - 1] + t * y[i] + t * (1 - t) * (a * (1 - t) + b * t);
    }

    private static double[] solveMatrix(double[][] matrix) {
        for (int k = 0; k < matrix.length; k++) {
            // Find row with greatest value after current row
            int iMax = 0;
            double maxVal = Integer.MIN_VALUE;
            for (int i = k; i < matrix.length; i++) {
                if (Math.abs(matrix[i][k]) > maxVal) {
                    maxVal = Math.abs(matrix[i][k]);
                    iMax = i;
                }
            }
            // Swap the current row and greatest row (pivot row)
            swapRows(matrix, k, iMax);

            // For all rows below the pivot row
            for (int i = k + 1; i < matrix.length; i++) {
                double cf = matrix[i][k] / matrix[k][k];
                for (int j = k; j < matrix.length + 1; j++) {
                    matrix[i][j] -= matrix[k][j] * cf;
                }
            }
        }

        double[] solution = new double[matrix.length];

        for (int i = matrix.length - 1; i > -1; i--) {
            double val = matrix[i][matrix.length] / matrix[i][i];
            solution[i] = val;
            for (int j = i - 1; j > -1; j--) {
                matrix[j][matrix.length] -= matrix[j][i] * val;
                matrix[j][i] = 0;
            }
        }

        return solution;
    }

    private static void swapRows(double[][] matrix, int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    private double[] getNaturalDerivatives(double[] x, double[] y) {
        double[][] matrix = new double[x.length][x.length + 1];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length + 1; j++) {
                matrix[i][j] = 0;
            }
        }

        for (int i = 1; i < x.length - 1; i++) {
            matrix[i][i - 1] = 1 / (x[i] - x[i - 1]);
            matrix[i][i] = 2 * (1 / (x[i] - x[i - 1]) + 1 / (x[i + 1] - x[i]));
            matrix[i][i + 1] = 1 / (x[i + 1] - x[i]);
            matrix[i][x.length] = 3 * ((y[i] - y[i - 1]) / ((x[i] - x[i - 1]) * (x[i] - x[i - 1])) + (y[i + 1] - y[i]) / ((x[i + 1] - x[i]) * (x[i + 1] - x[i])));
        }

        matrix[0][0] = 2 / (x[1] - x[0]);
        matrix[0][1] = 1 / (x[1] - x[0]);
        matrix[0][x.length] = 3 * (y[1] - y[0]) / ((x[1] - x[0]) * (x[1] - x[0]));

        matrix[x.length - 1][x.length - 2] = 1 / (x[x.length - 1] - x[x.length - 2]);
        matrix[x.length - 1][x.length - 1] = 2 / (x[x.length - 1] - x[x.length - 2]);
        matrix[x.length - 1][x.length] = 3 * (y[x.length - 1] - y[x.length - 2]) / ((x[x.length - 1] - x[x.length - 2]) * (x[x.length - 1] - x[x.length - 2]));

        return solveMatrix(matrix);
    }

    public static void main(String[] args) {
        double[] x = {0, 1, 2, 3, 4, 5};
        double[] y = {0, 2, 4, 6, 4, 0};
        Spline s = new Spline(x, y);
        double[] res = new double[500];
        for (int i = 0; i < res.length; i ++) {
            res[i] = s.eval((((double) i) / 100));
        }
        System.out.println(Arrays.toString(res));
//        System.out.print("[");
//        for (int i = 0; i < res.length; i ++) {
//            double xVal = ((double) i) / 100;
//            System.out.print("(" + xVal + ", " + res[i] + "), ");
//        }
//        System.out.println("\b\b]");
    }
}
