package ru.gnivc.springboot.tableCleanupAgent.fileOperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileCreation {
    public static BufferedWriter createFile(File temporaryDir) throws IOException {
        File temporaryFile = File.createTempFile("text", ".txt", temporaryDir);
        return new BufferedWriter(new FileWriter(temporaryFile, StandardCharsets.UTF_8, true));
    }

}
