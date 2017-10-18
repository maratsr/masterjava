package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int[] matrA = new int[matrixSize*matrixSize]; // using 1d array instead of 2d gives us +10% speed

        final int[] matrB = new int[matrixSize*matrixSize];
        final int[] matrC = new int[matrixSize*matrixSize];

        IntStream.range(0, matrixSize).parallel().forEach(i -> { // convert 2d array to 1d
            System.arraycopy(matrixA[i], 0, matrA, i * matrixSize, matrixSize);
            System.arraycopy(matrixB[i], 0, matrB, i * matrixSize, matrixSize);
        });

        IntStream.range(0, matrixSize).parallel().forEach(i -> {
            final int ii = i*matrixSize;
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrA[ii + k] * matrB[k*matrixSize+j];
                }
                matrC[ii+j] = sum;
            }
        });

        IntStream.range(0, matrixSize).parallel().forEach(i -> { // convert 1d result array to 2d
            System.arraycopy(matrC, i * matrixSize, matrixC[i], 0, matrixSize);
        });

//        IntStream.range(0, matrixSize).parallel().forEach(i -> {
//            for (int j = 0; j < matrixSize; j++) {
//                int sum = 0;
//                for (int k = 0; k < matrixSize; k++) {
//                    sum += matrixA[i][k] * matrixB[k][j];
//                }
//                matrixC[i][j] = sum;
//            }
//        });

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
