package ru.gnivc.springboot.tableCleanupAgent.fileOperations;

import java.io.File;

public class ListOfFiles {

    public static File[] listOfFiles(){
        File folder = new File("temporaryDir");
        return folder.listFiles();

    }
}
