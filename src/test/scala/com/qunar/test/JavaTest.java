package com.qunar.test;

import com.qunar.spark.diff.api.java.JavaDiffTracer;

public class JavaTest {

    @org.junit.Test
    public void test() {
        JavaDiffTracer.of().isDifferent(new Object(), new Object());
    }

}
