package com.viettel.vtnet.os_compare_service.service;

public class FibonacciMatrix {
    public static long fib(int n) {
        if (n <= 1) return n;
        long[][] result = {{1, 0}, {0, 1}}; // identity matrix
        long[][] fibMatrix = {{1, 1}, {1, 0}};
        power(fibMatrix, n - 1, result);
        return result[0][0];
    }

    private static void power(long[][] matrix, int n, long[][] result) {
        while (n > 0) {
            if ((n & 1) == 1) multiply(result, matrix);
            multiply(matrix, matrix);
            n >>= 1;
        }
    }

    private static void multiply(long[][] a, long[][] b) {
        long x = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        long y = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        long z = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        long w = a[1][0] * b[0][1] + a[1][1] * b[1][1];

        a[0][0] = x;
        a[0][1] = y;
        a[1][0] = z;
        a[1][1] = w;
    }
}
