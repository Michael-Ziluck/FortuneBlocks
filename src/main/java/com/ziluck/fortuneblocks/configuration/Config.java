package com.ziluck.fortuneblocks.configuration;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.handlers.trackers.TrackerType;
import com.ziluck.fortuneblocks.utils.MutableEnum;
import com.ziluck.fortuneblocks.utils.MutableString;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.commons.lang.mutable.MutableDouble;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config {
    /**
     * The plugin's version
     */
    public static final MutableString VERSION = new MutableString("${project.version}");

    /**
     * Whether or not block placing should be saved between restarts.
     */
    public static final MutableBoolean TRACKING_ENABLED = new MutableBoolean(false);

    /**
     * If survival is required for the plugin to function.
     */
    public static final MutableBoolean REQUIRE_SURVIVAL = new MutableBoolean(true);

    /**
     * If the items should go straight to the player's inventory.
     */
    public static final MutableBoolean PICKUP = new MutableBoolean(true);

    /**
     * If a message should be sent to the player alerting them of their full inventory.
     */
    public static final MutableBoolean FULL_MESSAGE_USE = new MutableBoolean(true);

    /**
     * The delay between messages related to having a full inventory. This is measured in seconds.
     */
    public static final MutableInt FULL_MESSAGE_COOLDOWN = new MutableInt(5);

    /**
     * Whether or not items should be dropped when the player has a full inventory.
     */
    public static final MutableBoolean FULL_DROP = new MutableBoolean(true);

    public static final MutableEnum<TrackerType> TRACKER_TYPE = new MutableEnum<>(TrackerType.MYSQL);

    public static final MutableString TRACKER_FILE_NAME = new MutableString("placed-blocks.db");

    public static final MutableString TRACKER_DATABASE_USERNAME = new MutableString("admin");
    public static final MutableString TRACKER_DATABASE_PASSWORD = new MutableString("password");
    public static final MutableString TRACKER_DATABASE_HOSTNAME = new MutableString("localhost");
    public static final MutableInt TRACKER_DATABASE_PORT = new MutableInt(3306);
    public static final MutableString TRACKER_DATABASE_DATABASE = new MutableString("fortune_blocks");


    public static void update() {
        FileConfiguration config = FortuneBlocks.getInstance().getConfig();

        MutableBoolean save = new MutableBoolean(false);

        updateValue(config, save, "version", VERSION);

        // the options related to persisting blocks between restarts
        updateValue(config, save, "require_survival", REQUIRE_SURVIVAL);
        updateValue(config, save, "pickup", PICKUP);
        updateValue(config, save, "full.message.use", FULL_MESSAGE_USE);
        updateValue(config, save, "full.message.cooldown", FULL_MESSAGE_COOLDOWN);
        updateValue(config, save, "full.drop", FULL_DROP);
        updateValue(config, save, "tracking.type", TRACKER_TYPE);
        updateValue(config, save, "tracking.file-name", TRACKER_FILE_NAME);
        updateValue(config, save, "tracking.enabled", TRACKING_ENABLED);
        updateValue(config, save, "tracking.database.username", TRACKER_DATABASE_USERNAME);
        updateValue(config, save, "tracking.database.password", TRACKER_DATABASE_PASSWORD);
        updateValue(config, save, "tracking.database.hostname", TRACKER_DATABASE_HOSTNAME);
        updateValue(config, save, "tracking.database.port", TRACKER_DATABASE_PORT);
        updateValue(config, save, "tracking.database.database", TRACKER_DATABASE_DATABASE);
    }

    /**
     * Updates the configuration with the given information. If the value fails to load from the config because it does
     * not exist or it is in an invalid format, the system will notify the console.
     *
     * @param config   the config file to load/update.
     * @param location the location in the config.
     * @param mutable  the mutable value to update.
     */
    private static <T extends Enum<T>> void updateValue(FileConfiguration config, MutableBoolean save, String location, MutableEnum<T> mutable) {
        if (!config.isSet(location) || !successful(() -> mutable.setValue(Enum.valueOf(mutable.getType(), config.getString(location).toUpperCase())))) {
            config.set(location, mutable.getValue().toString());
            error(location);
            if (!save.booleanValue()) {
                save.setValue(true);
            }
        }
    }

    /**
     * Updates the configuration with the given information. If the value fails to load from the config because it does
     * not exist or it is in an invalid format, the system will notify the console.
     *
     * @param config   the config file to load/update.
     * @param location the location in the config.
     * @param mutable  the mutable value to update.
     */
    private static void updateValue(FileConfiguration config, MutableBoolean save, String location, MutableString mutable) {
        if (!config.isSet(location) || !successful(() -> mutable.setValue(config.getString(location)))) {
            config.set(location, mutable.getValue());
            error(location);
            if (!save.booleanValue()) {
                save.setValue(true);
            }
        }
    }

    /**
     * Updates the configuration with the given information. If the value fails to load from the config because it does
     * not exist or it is in an invalid format, the system will notify the console.
     *
     * @param config   the config file to load/update.
     * @param location the location in the config.
     * @param mutable  the mutable value to update.
     */
    private static void updateValue(FileConfiguration config, MutableBoolean save, String location, MutableInt mutable) {
        if (!config.isSet(location) || !successful(() -> mutable.setValue(config.getInt(location)))) {
            config.set(location, mutable.intValue());
            error(location);
            if (!save.booleanValue()) {
                save.setValue(true);
            }
        }
    }

    /**
     * Updates the configuration with the given information. If the value fails to load from the config because it does
     * not exist or it is in an invalid format, the system will notify the console.
     *
     * @param config   the config file to load/update.
     * @param location the location in the config.
     * @param mutable  the mutable value to update.
     */
    private static void updateValue(FileConfiguration config, MutableBoolean save, String location, MutableDouble mutable) {
        if (!config.isSet(location) || !successful(() -> mutable.setValue(config.getDouble(location)))) {
            config.set(location, mutable.doubleValue());
            error(location);
            if (!save.booleanValue()) {
                save.setValue(true);
            }
        }
    }

    /**
     * Updates the configuration with the given information. If the value fails to load from the config because it does
     * not exist or it is in an invalid format, the system will notify the console.
     *
     * @param config   the config file to load/update.
     * @param location the location in the config.
     * @param mutable  the mutable value to update.
     */
    private static void updateValue(FileConfiguration config, MutableBoolean save, String location, MutableBoolean mutable) {
        if (!config.isSet(location) || !successful(() -> mutable.setValue(config.getBoolean(location)))) {
            config.set(location, mutable.booleanValue());
            error(location);
            if (!save.booleanValue()) {
                save.setValue(true);
            }
        }
    }

    /**
     * Used to check if an operation throws an exception with ease.
     *
     * @param runnable the operation to run.
     * @return {@code true} if the operation does NOT throw an exception.<br>
     * {@code false} if the operation DOES throw an exception.
     */
    protected static boolean successful(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Alerts the console that there was an error loading a config value.
     *
     * @param location the location that caused an error.
     */
    private static void error(String location) {
        FortuneBlocks.getInstance().getLogger().severe("Error loading the config value '" + location + "'. Reverted it to default.");
    }
}
