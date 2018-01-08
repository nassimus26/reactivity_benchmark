package org.reactivity;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import lombok.extern.log4j.Log4j;

@Log4j
/*
* Implementation of count in File using Akka Stream framework
*
* @author : Nassim MOUALEK
* */
public class AkkaCountWordInFile {
    private final ActorSystem actorSystem;

    private final Source<Path, NotUsed> newFiles;
    private final String wordToCount;
    private final Source<Long, NotUsed> countWordInFile;
    private final Flow<Path, ByteString, NotUsed> fileBytes =
            Flow.of(Path.class).flatMapConcat(FileIO::fromPath);

    private final Flow<ByteString, ByteString, NotUsed> delimiter =
            Framing.delimiter(ByteString.fromString("\n"), 10000, FramingTruncation.ALLOW);

    private final Flow<ByteString, List<ByteString>, NotUsed> toRows =
            Flow.of(ByteString.class).via(delimiter).grouped(20);

    private final Flow<List<ByteString>, Long, NotUsed> countWord =
            Flow.<List<ByteString>>create()
                    .buffer(50, OverflowStrategy.backpressure()) // the input stream must wait for the processing
                    .mapAsyncUnordered(4, row ->
                            CompletableFuture.supplyAsync(() ->
                                    (Long) row.stream()
                                            .map(ByteString::utf8String)
                                            .filter(a -> a.contains(getWordToCount()))
                                            .count()
                            ));
    private final Sink<Long, CompletionStage<Long>> sumCounts = Sink.<Long, Long> fold(0L, (aggr, next) -> aggr + next );

    public AkkaCountWordInFile(ActorSystem actorSystem, File file, String wordToCount) {
        this.wordToCount = wordToCount;
        this.newFiles = Source.single(file.toPath());
        this.countWordInFile = newFiles
                .via(fileBytes)
                .via(toRows)
                .via(countWord);
        this.actorSystem = actorSystem;
    }

    public long countWordInFile() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> done =
                countWordInFile
                        .runWith(sumCounts,
                                ActorMaterializer.create(actorSystem)).toCompletableFuture();
        long count = done.get();
        return count;
    }

    private String getWordToCount() {
        return wordToCount;
    }

}
