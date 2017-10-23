package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB in Parallel streams
    public static int[][] concurrentMultiply2(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        IntStream.range(0, matrixSize).parallel().forEach(j -> {
            final int thatColumn[] = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                thatColumn[k] = matrixB[k][j];
            }
            for (int i = 0; i < matrixSize; i++) {
                final int thisRow[] = matrixA[i];
                int summand = 0;
                for (int k = 0; k < matrixSize; k++) {
                    summand += thisRow[k] * thatColumn[k];
                }
                matrixC[i][j] = summand;
            }
        });
        return matrixC;
    }

    // TODO implement parallel multiplication matrixA*matrixB using executor
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        List<Callable<Void>> tasks = new ArrayList<>();
                for (int j=0;j<matrixSize;j++) {
                    int finalJ = j;
                    tasks.add(() -> {
                    final int thatColumn[] = new int[matrixSize];
                    for (int k = 0; k < matrixSize; k++) {
                        thatColumn[k] = matrixB[k][finalJ];
                    }
                    for (int i = 0; i < matrixSize; i++) {
                        final int thisRow[] = matrixA[i];
                        int summand = 0;
                        for (int k = 0; k < matrixSize; k++) {
                            summand += thisRow[k] * thatColumn[k];
                        }
                        matrixC[i][finalJ] = summand;
                    }
                    return null;
                });
            }
        List<Future<Void>> invokeAll = executor.invokeAll(tasks);
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int thatColumn[] = new int[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                thatColumn[k] = matrixB[k][j];
            }

            for (int i = 0; i < matrixSize; i++) {
                int thisRow[] = matrixA[i];
                int summand = 0;
                for (int k = 0; k < matrixSize; k++) {
                    summand += thisRow[k] * thatColumn[k];
                }
                matrixC[i][j] = summand;
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
