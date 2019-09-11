package com.ziluck.fortuneblocks.handlers.trackers;

import com.ziluck.fortuneblocks.configuration.Config;
import org.sqlite.JDBC;

import java.sql.Driver;

public class SqLiteTracker extends SqlTracker {
    @Override
    protected String getJdbcType() {
        return "sqlite";
    }

    @Override
    protected Class<? extends Driver> getDriverClass() {
        return JDBC.class;
    }

    @Override
    protected String getJdbcString() {
        return "jdbc:sqlite:" + Config.TRACKER_FILE_NAME;
    }
}
