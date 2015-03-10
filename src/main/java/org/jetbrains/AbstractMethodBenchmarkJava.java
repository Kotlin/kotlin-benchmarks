package org.jetbrains;

import org.jetbrains.SizedBenchmark;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mikhail.Glukhikh on 06/03/2015.
 *
 * A benchmark for a single abstract method based on a string comparison
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class AbstractMethodBenchmarkJava extends SizedBenchmark {

    static private List<String> arr;
    static {
        try {
            arr = Files.readAllLines(Paths.get("zdf-win.txt"), Charset.forName("CP1251"));
        } catch (IOException e) {
            arr = Arrays.asList("ёлка", "автобус", "beta", "жизнь", "удод", "колбаса");
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public SortedSet<String> sortStrings() {
        return new TreeSet<String>(arr.subList(0, getSize() < arr.size() ? getSize() : arr.size()));
    }

    static class RussianStringComparator implements Comparator<String> {

        static private final String sequence = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

        static private final Map<Character, Integer> sequenceMap = new HashMap<Character, Integer>();
        static {
            int i = 0;
            for (char ch: sequence.toCharArray()) {
                sequenceMap.put(ch, i++);
            }
        }

        @Override
        public int compare(@NotNull String a, @NotNull String b) {
            final String al = a.toLowerCase(), bl = b.toLowerCase();
            for (int i=0; i<al.length(); i++) {
                if (i >= bl.length())
                    return 1;
                final Integer ai = sequenceMap.get(al.charAt(i));
                final Integer bi = sequenceMap.get(bl.charAt(i));
                if (ai==null) {
                    if (bi != null)
                        return 1;
                    else
                        continue;
                }
                if (bi == null || ai < bi)
                    return -1;
                if (ai > bi)
                    return 1;
            }
            if (al.length() < bl.length())
                return -1;
            return 0;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public SortedSet<String> sortStringsWithComparator() {
        SortedSet<String> res = new TreeSet<String>(new RussianStringComparator());
        res.addAll(arr.subList(0, getSize() < arr.size() ? getSize() : arr.size()));
        return res;
    }
}
