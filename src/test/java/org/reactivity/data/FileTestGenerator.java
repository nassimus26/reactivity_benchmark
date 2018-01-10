package org.reactivity.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class FileTestGenerator {
    public static int DefaultWordCount = 100_000;

    public static File generateTestFile(String fileName, String wordToRepeat, int nRepeat) throws IOException {
        File file = Paths.get("src/test/resources/"+fileName).toFile();
        try (FileOutputStream fos = new FileOutputStream(file) ) {
            for ( int n=0; n<nRepeat*3; n++ ) {
                if (n % 3 == 0)
                    fos.write((UUID.randomUUID().toString()+" "+wordToRepeat + "\n").getBytes());
                else
                    fos.write((UUID.randomUUID().toString()+" "+UUID.randomUUID().toString() + "\n").getBytes());
            }
        }
        return file;
    }

}
