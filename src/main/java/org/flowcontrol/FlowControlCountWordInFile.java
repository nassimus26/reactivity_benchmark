package org.flowcontrol;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.log4j.Log4j;
import org.nassimus.thread.BufferedBatchCallable;
import org.nassimus.thread.BufferedBatchFlowControlExecutor;

/*
* Implementation of count in File using FlowControl framework
*
* @author : Nassim MOUALEK
* */
@Log4j
public class FlowControlCountWordInFile {
    private AtomicLong wordCount = new AtomicLong();
    private final BufferedBatchCallable<String> countWordsCallable;
    private final File file;
    public FlowControlCountWordInFile(File file, String wordToCount) {
        this.file = file;
        this.countWordsCallable = new BufferedBatchCallable<String>() {
            @Override
            public void call(List<String> batchValues) {
                for (String row : batchValues)
                    if (new String(row).contains(wordToCount))
                        wordCount.addAndGet(1);
            }
        };
    }
    public long countWordInFile() throws Throwable {
        AtomicBoolean submitEnd = new AtomicBoolean();
        BufferedBatchFlowControlExecutor<String> bufferedBatchFlowControlExecutor =
                new BufferedBatchFlowControlExecutor<String>(
                        countWordsCallable, 100, 4, 50, "CountWordInFile" ) {
                    @Override
                    public boolean isSubmitsEnds() {
                        return submitEnd.get();
                    }

                    @Override
                    public void handleException(Exception e) {
                        log.error(e);
                    }
                };
        Files.newBufferedReader(file.toPath()).lines().forEach(line -> {
            try {
                bufferedBatchFlowControlExecutor.submit(line);
            } catch (InterruptedException e) {
                log.error(e);
            }
        });
        submitEnd.set(true);
        bufferedBatchFlowControlExecutor.waitAndFlushWithException(true);
        return wordCount.get();
    }
}
