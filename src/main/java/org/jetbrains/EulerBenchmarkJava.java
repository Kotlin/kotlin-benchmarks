package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A class tests decisions of various Euler problems
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class EulerBenchmarkJava {

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int problem1(SizedBenchmark sb) {
        // Number of 3/5 divisible numbers in 1..size
        int res = 0;
        for (int i=1; i<=sb.getSize(); i++) {
            if (i % 3 == 0 || i % 5 == 0)
                res += i;
        }
        return res;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int problem2(HugeSizedBenchmark sb) {
        // Number of even Fibonacci numbers in 1..size
        int res = 0;
        int prev = 1, curr = 2;
        while (curr <= sb.getHugeSize()) {
            if (curr % 2 == 0)
                res += curr;
            int next = prev + curr;
            prev = curr;
            curr = next;
        }
        return res;
    }

    static private boolean palindromic(long num) {
        final String s = Long.toString(num);
        return s.equals(new StringBuilder(s).reverse().toString());
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long problem4(SmallSizedBenchmark sb) {
        // Largest palindrom which is a production of two [size/10...size-1] numbers
        final long size = sb.getSmallSize();
        final long maxNum = (size-1)*(size-1), minNum = (size / 10) * (size / 10);
        final int maxDiv = sb.getSmallSize()-1, minDiv = sb.getSmallSize() / 10;
        for (long i=maxNum; i>=minNum; i--) {
            if (!palindromic(i))
                continue;
            for (int j=minDiv; j<=maxDiv; j++) {
                if (i % j == 0) {
                    final long res = i / j;
                    if (res >= minDiv && res <= maxDiv) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private String veryLongNumber =
        "73167176531330624919225119674426574742355349194934" +
        "96983520312774506326239578318016984801869478851843" +
        "85861560789112949495459501737958331952853208805511" +
        "12540698747158523863050715693290963295227443043557" +
        "66896648950445244523161731856403098711121722383113" +
        "62229893423380308135336276614282806444486645238749" +
        "30358907296290491560440772390713810515859307960866" +
        "70172427121883998797908792274921901699720888093776" +
        "65727333001053367881220235421809751254540594752243" +
        "52584907711670556013604839586446706324415722155397" +
        "53697817977846174064955149290862569321978468622482" +
        "83972241375657056057490261407972968652414535100474" +
        "82166370484403199890008895243450658541227588666881" +
        "16427171479924442928230863465674813919123162824586" +
        "17866458359124566529476545682848912883142607690042" +
        "24219022671055626321111109370544217506941658960408" +
        "07198403850962455444362981230987879927244284909188" +
        "84580156166097919133875499200524063689912560717606" +
        "05886116467109405077541002256983155200055935729725" +
        "71636269561882670428252483600823257530420752963450";

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long problem8(HugeSizedBenchmark sb) {
        final int productSize;
        if (sb.getHugeSize() <= 10)
            productSize = 4;
        else if (sb.getHugeSize() <= 1000)
            productSize = 8;
        else
            productSize = 13;
        List<Integer> digits = new ArrayList<>();
        for (char digit: veryLongNumber.toCharArray()) {
            if (digit >= '0' && digit <= '9') {
                digits.add(Character.digit(digit, 10));
            }
        }
        long largest = 0L;
        for (int i=0; i<digits.size()-productSize; i++) {
            long product = 1L;
            for (int j=0; j<productSize; j++) {
                product *= digits.get(i+j);
            }
            largest = Math.max(product, largest);
        }
        return largest;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long problem9(SizedBenchmark sb) {
        final int size = sb.getSize();
        for (int c = size / 3; c < size - 2; c++) {
            long c2 = (long) c * (long) c;
            for (int b = (size - c) / 2; b < c; b++) {
                if (b + c >= size)
                    break;
                int a = size - b - c;
                if (a >= b)
                    continue;
                long b2 = (long) b * (long) b;
                long a2 = (long) a * (long) a;
                if (c2 == b2 + a2) {
                    return (long) a * (long) b * (long) c;
                }
            }
        }
        return -1L;
    }

    private static class IntPair {
        int first, second;
        IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    static private List<Integer> dfs(int begin, IntPair[] tree) {
        if (begin==0 || begin >= tree.length)
            return Collections.emptyList();
        final List<Integer> left = dfs(tree[begin].first, tree);
        final List<Integer> right = dfs(tree[begin].second, tree);
        final List<Integer> res = new LinkedList<>();
        res.add(begin);
        res.addAll(left.size() > right.size() ? left : right);
        return res;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public List<Integer> problem14(SizedBenchmark sb) {
        final int size = sb.getSize();
        final IntPair[] tree = new IntPair[size];
        for (int i=1; i<size; i++) {
            tree[i] = new IntPair(i*2, i>4 && (i+2) % 6 == 0 ? (i-1)/3 : 0);
        }
        return dfs(1, tree);
    }

    static private IntPair go(int begin, Map<Integer, IntPair> map) {
        IntPair res = map.get(begin);
        if (res != null)
            return res;
        final int next = (begin % 2 == 0) ? begin/2 : 3*begin + 1;
        IntPair childRes = go(next, map);
        IntPair myRes = new IntPair(childRes.first + 1, next);
        map.put(begin, myRes);
        return myRes;
    }

    static private List<Integer> unroll(int begin, Map<Integer, IntPair> map) {
        if (begin==0)
            return Collections.emptyList();
        final IntPair nextPair = map.get(begin);
        final int next = nextPair != null ? nextPair.second : 0;
        final List<Integer> res = new LinkedList<>();
        res.add(begin);
        res.addAll(unroll(next, map));
        return res;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public List<Integer> problem14full(SizedBenchmark sb) {
        final int size = sb.getSize();
        final Map<Integer, IntPair> map = new HashMap<>();
        map.put(1, new IntPair(0, 0));
        int bestNum = 0, bestLen = 0;
        for (int i=2; i<size; i++) {
            IntPair res = go(i, map);
            if (res.first > bestLen) {
                bestLen = res.first;
                bestNum = i;
            }
        }
        return unroll(bestNum, map);
    }

}
