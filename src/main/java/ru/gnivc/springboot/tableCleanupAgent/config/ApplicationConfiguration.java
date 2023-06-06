package ru.gnivc.springboot.tableCleanupAgent.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.gnivc.springboot.tableCleanupAgent.CustomizableParameters;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {


    @Bean
    public CustomizableParameters customizableParameters(@Value("${hours}") int hours,
                                                         @Value("${limitDelSqlParam}") int limitDelSqlParam,
                                                         @Value("${tableNameSqlParam}") String tableNameSqlParam,
                                                         @Value("${primalKeyColumnDelSqlParam}") String primalKeyColumnDelSqlParam,
                                                         @Value("${timeStampOfRecordCreation}") String timeStampOfRecordCreation){
        return new CustomizableParameters(hours, limitDelSqlParam, tableNameSqlParam,
                primalKeyColumnDelSqlParam, timeStampOfRecordCreation);

    }
}
