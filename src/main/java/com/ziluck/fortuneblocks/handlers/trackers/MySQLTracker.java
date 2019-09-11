package com.ziluck.fortuneblocks.handlers.trackers;

import java.sql.Driver;

public class MySQLTracker extends SqlTracker {
    @Override
    protected String getJdbcType() {
        return "mysql";
    }

    @Override
    protected Class<? extends Driver> getDriverClass() {
        return com.mysql.jdbc.Driver.class;
    }
}
