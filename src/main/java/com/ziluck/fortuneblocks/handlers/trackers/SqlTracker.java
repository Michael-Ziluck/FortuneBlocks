package com.ziluck.fortuneblocks.handlers.trackers;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.handlers.BlockWrapper;
import com.ziluck.fortuneblocks.mappers.BlockWrapperMapper;
import com.ziluck.fortuneblocks.utils.PropertiesBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bukkit.Bukkit;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.io.IOException;
import java.io.Reader;
import java.sql.Driver;
import java.util.Properties;

import static com.ziluck.fortuneblocks.configuration.Config.*;
import static com.ziluck.fortuneblocks.configuration.Config.TRACKING_ENABLED;

public abstract class SqlTracker extends BlockTracker {
    private SqlSessionFactory sqlSessionFactory;

    private BlockWrapperMapper mapper;

    protected abstract String getJdbcType();

    protected abstract Class<? extends Driver> getDriverClass();

    protected String getJdbcString() {
        return "jdbc:" + getJdbcType() + "://" + TRACKER_DATABASE_HOSTNAME + ":" + TRACKER_DATABASE_PORT + "/" + TRACKER_DATABASE_DATABASE;
    }

    @Override
    public boolean initialize() {
        String jdbcUrl = getJdbcString();

        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(jdbcUrl, TRACKER_DATABASE_USERNAME.getValue(), TRACKER_DATABASE_PASSWORD.getValue())
                    .load();
            flyway.migrate();
        } catch (FlywayException ex) {
            FortuneBlocks.getInstance().getLogger().severe("Failed to set up database schema.");
            return false;
        }

        try {
            String resource = "com/ziluck/fortuneblocks/config.xml";
            Reader reader = Resources.getResourceAsReader(resource);

            Properties properties = PropertiesBuilder.builder()
                    .addProperty("DB_URL", jdbcUrl)
                    .addProperty("DB_USERNAME", TRACKER_DATABASE_USERNAME.getValue())
                    .addProperty("DB_PASSWORD", TRACKER_DATABASE_PASSWORD.getValue())
                    .addProperty("DB_TYPE", getDriverClass().getName())
                    .toProperties();

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
        } catch (IOException ex) {
            ex.printStackTrace();
            FortuneBlocks.getInstance().getLogger().severe("Failed to open database connection.");
            return false;
        }

        if (TRACKING_ENABLED.booleanValue()) {
            try {
                mapper = sqlSessionFactory.openSession(true).getMapper(BlockWrapperMapper.class);
                placedBlocks.addAll(mapper.selectAll());
            } catch (Exception ex) {
                ex.printStackTrace();
                FortuneBlocks.getInstance().getLogger().severe("Failed to load placed blocks.");
            }
        }

        return true;
    }

    @Override
    public void writeBlock(BlockWrapper blockWrapper) {
        Bukkit.getScheduler().runTaskAsynchronously(FortuneBlocks.getInstance(), () -> {
            try {
                if (mapper == null) {
                    mapper = sqlSessionFactory.openSession(true).getMapper(BlockWrapperMapper.class);
                }

                mapper.insert(blockWrapper);
            } catch (Exception ex) {
                FortuneBlocks.getInstance().getLogger().severe("Failed to write block " + blockWrapper + ".");
            }
        });
    }

    @Override
    public void clearBlock(BlockWrapper blockWrapper) {
        Bukkit.getScheduler().runTaskAsynchronously(FortuneBlocks.getInstance(), () -> {
            try {
                if (mapper == null) {
                    mapper = sqlSessionFactory.openSession(true).getMapper(BlockWrapperMapper.class);
                }

                mapper.deleteByAll(blockWrapper);
            } catch (Exception ex) {
                FortuneBlocks.getInstance().getLogger().severe("Failed to clear block " + blockWrapper + ".");
            }
        });
    }
}
