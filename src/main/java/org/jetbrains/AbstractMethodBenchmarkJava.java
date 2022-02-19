package org.jetbrains;

import org.jetbrains.SizedBenchmark;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mikhail.Glukhikh on 06/03/2015.
 *
 * A benchmark for a single abstract method based on a string comparison
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class AbstractMethodBenchmarkJava extends SizedBenchmark {

    static private List<String> arr;
    static {
        try {
            final InputStream fin = new FileInputStream("zdf-win.txt");
            final InputStreamReader reader = new InputStreamReader(fin, StandardCharsets.ISO_8859_1);
            final BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();
            arr = new ArrayList<>();
            while (line != null) {
                arr.add(line);
                line = buffered.readLine();
            }
        } catch (IOException e) {
            arr = Arrays.asList("ёлка", "автобус", "beta", "жизнь", "удод", "колбаса");
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public SortedSet<String> sortStrings() {
        return new TreeSet<>(arr.subList(0, Math.min(getSize(), arr.size())));
    }

    static class RussianStringComparator implements Comparator<String> {

        static private final String sequence = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

        static private final Map<Character, Integer> sequenceMap = new HashMap<>();
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
        SortedSet<String> res = new TreeSet<>(new RussianStringComparator());
        res.addAll(arr.subList(0, Math.min(getSize(), arr.size())));
        return res;
    }
}
