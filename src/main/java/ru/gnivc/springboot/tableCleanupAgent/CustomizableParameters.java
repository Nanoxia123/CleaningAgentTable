package ru.gnivc.springboot.tableCleanupAgent;

public class CustomizableParameters {


    private final int hours;

    //Период выборки строк из таблицы, подлежащих удалению
    private final int limitDelSqlParam;

    //Название таблицы
    private final String tableNameSqlParam;

    //Наименование поля с первичным ключом
    private final String primalKeyColumnDelSqlParam;




    //Наименование поля в таблице в котором, временная метка создания записи
    private final String timeStampOfRecordCreation;



    //Задержка удаления

    public CustomizableParameters(int hours,
                                  int limitDelSqlParam,
                                  String tableNameSqlParam,
                                  String primalKeyColumnDelSqlParam,
                                  String timeStampOfRecordCreation) {
        this.hours = hours;
        this.limitDelSqlParam = limitDelSqlParam;
        this.tableNameSqlParam = tableNameSqlParam;
        this.primalKeyColumnDelSqlParam = primalKeyColumnDelSqlParam;
        this.timeStampOfRecordCreation = timeStampOfRecordCreation;
    }

    public int getHours() {
        return hours;
    }
    public int getLimitDelSqlParam() {
        return limitDelSqlParam;
    }

    public String getTableNameSqlParam() {
        return tableNameSqlParam;
    }

    public String getPrimalKeyColumnDelSqlParam() {
        return primalKeyColumnDelSqlParam;
    }

    public String getTimeStampOfRecordCreation() {
        return timeStampOfRecordCreation;
    }

}
