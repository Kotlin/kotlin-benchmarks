package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class VectorizationBenchmarkJava extends VectorSizedBenchmark {
    private double[] vA, vB;
    private int[] vI;
    private boolean[] vP;

    @Setup
    public void setup() {
        int n = getVectorSize();
        Random random = new Random();
        vA = new double[n];
        vB = new double[n];
        vI = new int[n];
        vP = new boolean[n];
        for (int i = 0; i < n; ++i) {
            vA[i] = random.nextDouble();
            vB[i] = random.nextDouble();
            vI[i] = i;
            vP[i] = i % 2 == 0;
        }
    }

    @Benchmark
    public double[] vectorAdd() {
        int n = getVectorSize();
        double[] vX = new double[n];
        for (int i = 0; i < n; ++i) {
            vX[i] = vA[i] + vB[i];
        }
        return vX;
    }

    @Benchmark
    public double[] vectorAddGather() {
        int n = getVectorSize();
        double[] vX = new double[n];
        for (int i = 0; i < n; ++i) {
            vX[i] = vA[i] + vB[vI[i]];
        }
        return vX;
    }

    @Benchmark
    public double vectorReductionSum() {
        int n = getVectorSize();
        double x = 0;
        for (int i = 0; i < n; ++i) {
            x += vA[i];
        }
        return x;
    }

    @Benchmark
    public double nonVectorReductionSum() {
        int n = getVectorSize();
        double x = 0;
        // Trip-counted loop to prevent vectorization.
        for (int i = 0; i < n; i += step()) {
            x += vA[i];
        }
        return x;
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private int step() {
        return 1;
    }

    @Benchmark
    public double vectorReductionSumPredicated() {
        int n = getVectorSize();
        double x = 0;
        for (int i = 0; i < n; ++i) {
            if (vP[i]) {
                x += vA[i];
            }
        }
        return x;
    }

    @Benchmark
    public double vectorDotProduct() {
        int n = getVectorSize();
        double x = 0;
        for (int i = 0; i < n; ++i) {
            x += vA[i] * vB[i];
        }
        return x;
    }

    @Benchmark
    public double vectorReductionMax() {
        int n = getVectorSize();
        double x = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < n; ++i) {
            x = Math.max(x, vA[i]);
        }
        return x;
    }
}
