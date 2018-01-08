package org.reactivity.unittests;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.flowcontrol.FlowControlCountWordInFile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reactivity.data.FileTestGenerator;


public class FlowControlCountWordInFileUnitTest {
    private static int nWord = FileTestGenerator.DefaultWordCount;
    private static String wordToCount = UUID.randomUUID().toString();
    private static File fileToTest;

    @BeforeClass
    public static void setUp() throws IOException {
        fileToTest = FileTestGenerator.generateTestFile("data.txt", wordToCount, nWord);
    }

    @Test
    public void should_return_a_correct_count_of_word_in_file() throws Throwable {
        //given
        FlowControlCountWordInFile fileWordCount = new FlowControlCountWordInFile(fileToTest, wordToCount);

        //when
        long count = fileWordCount.countWordInFile();

        //then
        Assert.assertEquals(nWord, count);
    }

}