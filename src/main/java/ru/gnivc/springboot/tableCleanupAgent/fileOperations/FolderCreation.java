package ru.gnivc.springboot.tableCleanupAgent.fileOperations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gnivc.springboot.tableCleanupAgent.repository.DataAccessRepository;

import java.io.File;

public class FolderCreation {
    private static final Logger logger = LogManager.getLogger(FolderCreation.class);
    public static File folderCreation() {
        File temporaryDir = new File("temporaryDir");
        if(!temporaryDir.exists()) {
            if (temporaryDir.mkdir()) {
                return temporaryDir;
            }
            return null;
        }
        return temporaryDir;
    }


}
