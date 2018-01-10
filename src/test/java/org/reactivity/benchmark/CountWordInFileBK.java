package org.reactivity.benchmark;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorSystem;
import org.flowcontrol.FlowControlCountWordInFile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.reactivity.AkkaCountWordInFile;
import org.reactivity.data.FileTestGenerator;

/*
* Benchmark of count word in File using FlowControl vs Akka Framework
*
* @author : Nassim MOUALEK
* */
public class CountWordInFileBK {
    private static final ActorSystem actorSystem = ActorSystem.create();
    private static int nWord = FileTestGenerator.DefaultWordCount;
    private static String wordToCount = UUID.randomUUID().toString();
    private static File fileToTest;

    @BeforeClass
    public static void setUp() throws IOException {
        fileToTest = FileTestGenerator.generateTestFile("data.txt", wordToCount, nWord);
    }

    @Benchmark
    public void count_using_Akka_Stream_Library() throws ExecutionException, InterruptedException {
        //given
        AkkaCountWordInFile fileWordCount = new AkkaCountWordInFile(actorSystem, fileToTest, wordToCount);

        //when
        long count = fileWordCount.countWordInFile();

        //then
        Assert.assertEquals(nWord, count);
    }

    @Benchmark
    public void count_using_MultiThread_With_A_Blocking_BackPressure_Library() throws Throwable {
        //given
        FlowControlCountWordInFile fileWordCount = new FlowControlCountWordInFile(fileToTest, wordToCount);

        //when
        long count = fileWordCount.countWordInFile();

        //then
        Assert.assertEquals(nWord, count);
    }

    /**
     * Benchmark run with Junit
     * @throws Exception
     */
    @Test
    public void benchmark() throws Exception {
        Options opt = initBench();
        runBench(opt);
    }

    private Options initBench() {
        return new OptionsBuilder()
                .include(CountWordInFileBK.class.getSimpleName() + ".*")
                .mode(Mode.AverageTime)
                .verbosity(VerboseMode.NORMAL)
                .timeUnit(TimeUnit.MILLISECONDS)
                .warmupTime(TimeValue.milliseconds(10))
                .measurementTime(TimeValue.milliseconds(10))
                .threads(1)
                .warmupIterations(2)
                .measurementIterations(6)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .forks(0)
                .build();
    }

    /**
     * @param opt
     * @return
     * @throws RunnerException
     */
    private Collection<RunResult> runBench(Options opt) throws RunnerException {
        return new Runner(opt).run();
    }

}