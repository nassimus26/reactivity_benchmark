package org.reactivity.unittests;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import akka.actor.ActorSystem;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactivity.AkkaCountWordInFile;
import org.reactivity.data.FileTestGenerator;


public class AkkaCountWordInFileUnitTest {
    private static final ActorSystem actorSystem = ActorSystem.create();
    private static long nWord = FileTestGenerator.DefaultWordCount;
    private static String wordToCount = UUID.randomUUID().toString();
    private static File fileToTest;

    @BeforeClass
    public static void setUp() throws IOException {
        fileToTest = FileTestGenerator.generateTestFile("data.txt", wordToCount, nWord);
    }

    @Test
    public void should_return_a_correct_count_of_word_in_file() throws ExecutionException, InterruptedException {
        //given
        AkkaCountWordInFile fileWordCount = new AkkaCountWordInFile(actorSystem, fileToTest, wordToCount);

        //when
        long count = fileWordCount.countWordInFile();

        //then
        Assert.assertEquals(nWord, count);
    }

}