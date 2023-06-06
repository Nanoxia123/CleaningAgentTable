package ru.gnivc.springboot.tableCleanupAgent.fileOperations;

import java.io.File;

public class FileDeletion {

    public static boolean fileDeletion(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
