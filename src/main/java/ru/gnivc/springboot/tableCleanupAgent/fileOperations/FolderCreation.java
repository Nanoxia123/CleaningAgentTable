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
                logger.info("Папка успешно создана");
                return temporaryDir;
            }
            return null;
        }
        logger.info("Папка уже существует");
        return temporaryDir;
    }


}
