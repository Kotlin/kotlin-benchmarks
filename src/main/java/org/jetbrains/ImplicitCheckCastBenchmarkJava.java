package org.jetbrains;

import kotlin.random.Random;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ImplicitCheckCastBenchmarkJava extends SizedBenchmark {

    public interface JBase {
        JBase test();
    }

    public interface JDerived extends JBase {}

    public static class  JClass1 implements JDerived{
        public JClass1 testSame() {
            return this;
        }

        @Override
        public JBase test() {
            return this;
        }
    }
    public static class JClass2 implements JDerived{
        @Override
        public JBase test() {
            return this;
        }
    }

    public final JClass1 class1 = new JClass1();

    public final JClass2 class2 = new JClass2();

    public  JBase base = null;

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public JClass1 returnBase() {
        return class1;
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public JBase returnCheckCast() {
        if (Random.Default.nextBoolean()) {
            return class2;
        }
        else {
            return class1;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void testReturnBase(Blackhole bh) {
        JClass1 jClass1 = new JClass1();
        int i = getSize();
        while (i > 0) {
            bh.consume(returnBase().testSame());
            i--;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void testReturnCheckCast(Blackhole bh) {
        int i = getSize();
        while (i > 0) {
            bh.consume(returnCheckCast().test());
            i--;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public JBase[] testArrayInit() {
        JBase[] array = new JBase[getSize()];
        int i = getSize()-1;
        while (i >= 0) {
            if (Random.Default.nextBoolean()) {
                array[i] = class1;
            }
            else {
                array[i] = class2;
            }
            i--;
        }
        return array;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void  fieldWrite(Blackhole bh) {
        int i = getSize() -1;
        while (i >= 0) {
            if (Random.Default.nextBoolean()) {
                base = class1;
            }
            else {
                base = class2;
            }
            bh.consume(base);
            i--;
        }
    }
}