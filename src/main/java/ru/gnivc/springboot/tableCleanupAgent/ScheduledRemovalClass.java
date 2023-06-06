package ru.gnivc.springboot.tableCleanupAgent;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gnivc.springboot.tableCleanupAgent.repository.DataAccessRepository;

import java.io.IOException;


@Component
public class ScheduledRemovalClass {

    private static final Logger logger = LogManager.getLogger(ScheduledRemovalClass.class);
    private final DataAccessRepository dataAccessRepository;


    @Autowired
    public ScheduledRemovalClass(DataAccessRepository dataAccessRepository) {
        this.dataAccessRepository = dataAccessRepository;
    }


    @Scheduled(cron = "${deletionPeriodArgument}")
    public void functionToStartDeletingInformation() throws IOException {
        logger.info("Процесс удаления строк начался");
        dataAccessRepository.deleteData();
        logger.info("Процесс удаления строк завершился");
    }

    @Scheduled(cron = "${samplingPeriodArgument}")
    public void functionToStartSelectionOfRecords() {
        logger.info("Процесс выборки строк подлежащих удаления начался");
        dataAccessRepository.selectionOfRecords();
        logger.info("Процесс выборки строк подлежащих удаления завершён");
    }

}

