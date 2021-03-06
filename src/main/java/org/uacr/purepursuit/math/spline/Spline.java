package org.uacr.purepursuit.math.spline;

import java.util.Arrays;

public class Spline {

    private double[] x;
    private double[] y;

    private boolean isNaturalSpline;
    private double startingAngle;
    private double endingAngle;

    public Polynomial[] curves;

    private Spline(double[] x, double[] y, boolean isNaturalSpline, double startingAngle, double endingAngle) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Must provide same number of x and y values.");
        }

        this.x = x;
        this.y = y;

        this.startingAngle = startingAngle;
        this.endingAngle = endingAngle;

        this.isNaturalSpline = isNaturalSpline;

        this.curves = generatePolynomials(this.x, this.y);
    }

    public Spline(double[] x, double[] y) {
        this(x, y, true, 0.0, 0.0);
    }

    public Spline(double[] x, double[] y, double startingAngle, double endingAngle) {
        this(x, y, false, startingAngle, endingAngle);
    }

    public Polynomial getCurve(double x) {
        for (Polynomial curve : curves) {
            if (x >= curve.getLowerBound() && x <= curve.getUpperBound()) {
                return curve;
            }
        }

        throw new IllegalArgumentException("Input not in domain of spline");
    }

    public double getLowerBound() {
        return curves[0].getLowerBound();
    }

    public double getUpperBound() {
        return curves[curves.length - 1].getUpperBound();
    }

    public double eval(double input) {
        for (Polynomial curve : curves) {
            if (input >= curve.getLowerBound() && input <= curve.getUpperBound()) {
                return curve.eval(input);
            }
        }

        throw new IllegalArgumentException("Input not in domain of spline");
    }

    public double[] eval(double[] inputs) {
        double[] results = new double[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            results[i] = eval(inputs[i]);
        }
        return results;
    }

    private Polynomial[] generatePolynomials(double[] x, double[] y) {
        final int n = x.length;
        int currentRow = 0;

        double[][] equationsMatrix = getZeros(4 * n - 4, 4 * n - 3);

        // Boundary conditions
        if (isNaturalSpline) {
            // Natural spline - second derivatives are 0
            // c_0 = 0
            equationsMatrix[currentRow++][2] = 1;
            // 0 = 2 c_n-2 + 6 d_n-2 (x_n-1 - x_n-2)
            equationsMatrix[currentRow][4 * (n - 2) + 2] = 2;
            equationsMatrix[currentRow++][4 * (n - 2) + 3] = 6 * (x[n - 1] - x[n - 2]);
        } else {
            // First derivatives are set to angles
            double startingDerivative = Math.tan(Math.toRadians(startingAngle));
            double endingDerivative = Math.tan(Math.toRadians(endingAngle));
            // b_0 = k
            equationsMatrix[currentRow][1] = 1;
            equationsMatrix[currentRow++][4 * n - 4] = startingDerivative;
            // k = b_n-2 + 2 c_n-2 (x_n-1 - x_n-2) + 3 d_n-2 (x_n-1 - x_n-2)^2
            equationsMatrix[currentRow][4 * (n - 2) + 1] = 1;
            equationsMatrix[currentRow][4 * (n - 2) + 2] = 2 * (x[n - 1] - x[n - 2]);
            equationsMatrix[currentRow][4 * (n - 2) + 3] = 3 * Math.pow(x[n - 1] - x[n - 2], 2);
            equationsMatrix[currentRow++][4 * n - 4] = endingDerivative;
        }
        for (int i = 0; i < n - 1; i++) {
            // y_i = a_i
            equationsMatrix[currentRow][4 * i] = 1;
            equationsMatrix[currentRow++][4 * n - 4] = y[i];
            // y_i+1 = a_i + b_i (x_i+1 - x_i) + c_i (x_i+1 - x_i)^2 + d_i (x_i+1 - x_i)^3
            equationsMatrix[currentRow][4 * i] = 1;
            equationsMatrix[currentRow][4 * i + 1] = x[i + 1] - x[i];
            equationsMatrix[currentRow][4 * i + 2] = Math.pow(x[i + 1] - x[i], 2);
            equationsMatrix[currentRow][4 * i + 3] = Math.pow(x[i + 1] - x[i], 3);
            equationsMatrix[currentRow++][4 * n - 4] = y[i + 1];
        }
        for (int i = 0; i < n - 2; i++) {
            // 0 = b_i + 2c_i (x_i+1 - x_i) + 3d_i (x_i+1 - x_i)^2 - b_i+1
            equationsMatrix[currentRow][4 * i + 1] = 1;
            equationsMatrix[currentRow][4 * i + 2] = 2 * (x[i + 1] - x[i]);
            equationsMatrix[currentRow][4 * i + 3] = 3 * Math.pow(x[i + 1] - x[i], 2);
            equationsMatrix[currentRow++][4 * (i + 1) + 1] = -1;
            // 0 = 2c_i + 6d_i (x_i+1 - x_i) - 2c_i+1
            equationsMatrix[currentRow][4 * i + 2] = 2;
            equationsMatrix[currentRow][4 * i + 3] = 6 * (x[i + 1] - x[i]);
            equationsMatrix[currentRow++][4 * (i + 1) + 2] = -2;
        }

        Polynomial[] result = new Polynomial[n - 1];
        double[] coefficients = solveMatrix(equationsMatrix);
        for (int i = 0; i < n - 1; i++) {
            result[i] = new Polynomial(3, x[i], x[i + 1], x[i],
                    coefficients[4 * i], coefficients[4 * i + 1],
                    coefficients[4 * i + 2], coefficients[4 * i + 3]);
        }

        return result;
    }

    private static double[][] getZeros(int rows, int columns) {
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = 0;
            }
        }

        return result;
    }

    private static void printArray(double[][] array) {
        for (double[] doubles : array) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }
            System.out.println();
        }
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

    public static void main(String[] args) {
        double[] x = new double[]{0, 1, 2, 3, 4, 5};
        double[] y = new double[]{0, 2, 4, 6, 4, 0};
        Spline s = new Spline(x, y, 30, 30);
        double[] testX = new double[500];
        for (int i = 0; i < 500; i++) {
            testX[i] = ((double) i) / 100;
        }
        System.out.println(Arrays.toString(testX));
        System.out.println(Arrays.toString(s.eval(testX)));
        System.out.println(Arrays.toString(s.curves));
    }
}
