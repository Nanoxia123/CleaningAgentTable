package ru.gnivc.springboot.tableCleanupAgent.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import ru.gnivc.springboot.tableCleanupAgent.CustomizableParameters;
import ru.gnivc.springboot.tableCleanupAgent.fileOperations.FileCreation;
import ru.gnivc.springboot.tableCleanupAgent.fileOperations.FileDeletion;
import ru.gnivc.springboot.tableCleanupAgent.fileOperations.FolderCreation;
import ru.gnivc.springboot.tableCleanupAgent.fileOperations.ListOfFiles;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataAccessRepository {
    private static final Logger logger = LogManager.getLogger(DataAccessRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private final CustomizableParameters customizableParameters;


    @Autowired
    public DataAccessRepository(JdbcTemplate jdbcTemplate, CustomizableParameters customizableParameters) {
        this.jdbcTemplate = jdbcTemplate;
        this.customizableParameters = customizableParameters;
    }

    public void selectionOfRecords() {
        if (ListOfFiles.listOfFiles() == null) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime pastTime = currentTime.minusHours(customizableParameters.getHours()); // Может ли тут быть исключение?


            if (currentTime == pastTime) {
                logger.error("Произошла ошибка при вычислении интервала");
                return;
            }
            String numberOfMatchingLines = String.format("SELECT %s FROM %s WHERE %s >= ? AND %s <= ?",
                    customizableParameters.getPrimalKeyColumnDelSqlParam(),
                    customizableParameters.getTableNameSqlParam(),
                    customizableParameters.getTimeStampOfRecordCreation(),
                    customizableParameters.getTimeStampOfRecordCreation()
            );

            rollCallBack(numberOfMatchingLines, currentTime, pastTime);

        }
        else{
            logger.info("Операция выборки пропущена, так как в папке есть файлы");
        }
    }
    private void writingFile(List<String> idList) throws IOException {
        int count = 0;
        BufferedWriter file = null;
        File temporaryDir = FolderCreation.folderCreation();
        if (temporaryDir != null) {
            if (idList != null) {
                for (String id : idList) {
                    if (count == 0) {
                        file = FileCreation.createFile(temporaryDir);
                        logger.info("Файл успешно создан");
                    }
                    file.write(id);
                    file.newLine();
                    file.flush();
                    count++;
                    if (count == customizableParameters.getLimitDelSqlParam()) {
                        file.close();
                        count = 0;
                    }
                }
                if (file != null) {
                    file.close();
                    logger.info("Все файлы были успешно созданы и заполнены");
                }
            }
            else {
                logger.error("В базе данных нет подходящий строк");
            }
        }
        else {
            logger.error("Не удалось создать папку");
        }
    }

    private void rollCallBack(String numberOfMatchingLines, LocalDateTime currentTime, LocalDateTime pastTime) {
        try {
            List<String> list = new ArrayList<>();
            final RowCallbackHandler rowCallbackHandler = new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    int id = rs.getInt("id");
                    list.add(String.valueOf(id));
                }
            };
            logger.info("Процесс выборки строк подлежащих удаления начался");
            jdbcTemplate.query(numberOfMatchingLines, rowCallbackHandler, pastTime, currentTime);
            try {
                writingFile(list);
            } catch (IOException e) {
                logger.error("error write in file: ", e);
            }
        } catch (DataAccessException exception) {
            logger.error("Произошла ошибка при выполнении запроса. Параметры были заданы не верно. sql запрос " +
                    "{}", numberOfMatchingLines);
        }
    }


    private List<String> fileReader(File file) throws IOException {
        List<String> idList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            idList.add(line);
        }
        reader.close();
        return idList;
    }

    //Удаление
    public void deleteData() {
            File[] files = ListOfFiles.listOfFiles();
            if (files.length > 0) {
                    try {
                        String tmp = String.join(",", fileReader(files[0]));
                        String limitedDeletionRequest = String.format("DELETE FROM %s WHERE %s IN (%s)",
                                customizableParameters.getTableNameSqlParam(),
                                customizableParameters.getPrimalKeyColumnDelSqlParam(),
                                tmp);
                        logger.info("sql: {}", limitedDeletionRequest);
                        jdbcTemplate.update(limitedDeletionRequest);

                        if (!FileDeletion.fileDeletion(files[0])) {
                            logger.error("Не удалось удалить файл");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
            else{
                logger.error("В папке нет файлов");
            }
    }
}
