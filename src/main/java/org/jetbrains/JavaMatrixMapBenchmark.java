package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class emulates matrix behaviour using a hash map as its implementation
 */
class Matrix {

    /**
     * This class stores a pair of something
     */
    private static class Pair<A, B> {
        private A first;
        private B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() { return first; }

        public B getSecond() { return second; }

        public boolean equals(Object o) {
            if (this==o)
                return true;
            if (o instanceof Pair) {
                final Pair other = (Pair)o;
                return first.equals(other.first) && second.equals(other.second);
            }
            return false;
        }

        public int hashCode() {
            int hash = 7;
            hash = 13 * hash + first.hashCode();
            hash = 19 * hash + second.hashCode();
            return hash;
        }
    }

    private final Map<Pair<Integer, Integer>, Double> matrix = new HashMap<Pair<Integer, Integer>, Double>();

    private final int rows, columns;

    static private final Random random = new Random(4242);

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        for (int row=0; row<rows; row++)
            for (int col=0; col<columns; col++) {
                matrix.put(new Pair<Integer, Integer>(row, col), random.nextDouble());
            }
    }

    public double get(int i, int j) { return get(new Pair<Integer, Integer>(i, j)); }

    public double get(Pair<Integer, Integer> key) { return matrix.get(key); }

    public void put(int i, int j, double val) { put(new Pair<Integer, Integer>(i, j), val); }

    public void put(Pair<Integer, Integer> key, double val) { matrix.put(key, val); }

    public Matrix plusAssign(Matrix other) {
        for (Map.Entry<Pair<Integer, Integer>, Double> entry: matrix.entrySet()) {
            put(entry.getKey(), entry.getValue() + other.get(entry.getKey()));
        }
        return this;
    }
}

/**
 * This class tests hash map performance
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JavaMatrixMapBenchmark extends SizedBenchmark {

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Matrix add() {
        int rows = getSize(), cols = 1;
        while (rows > cols) {
            rows /= 2;
            cols *= 2;
        }
        final Matrix a = new Matrix(rows, cols);
        final Matrix b = new Matrix(rows, cols);
        return a.plusAssign(b);
    }
}