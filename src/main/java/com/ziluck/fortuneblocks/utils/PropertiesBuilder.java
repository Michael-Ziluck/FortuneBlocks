package com.ziluck.fortuneblocks.utils;

import java.util.Properties;

/**
 * Builder for creating a new {@link Properties} object. Each instance of this class can only ever be built once to
 * ensure that any sensitive information doesn't end up in multiple locations.
 */
public class PropertiesBuilder {
    private Properties properties;

    private boolean built;

    /**
     * Constructs a new PropertiesBuilder with an empty {@link Properties}.
     */
    private PropertiesBuilder() {
        properties = new Properties();
    }

    /**
     * Add a new property.
     *
     * @param key   the key of the property
     * @param value the value of the property
     * @return a reference to this object
     */
    public PropertiesBuilder addProperty(String key, String value) {
        if (built) {
            throw new IllegalStateException("Can't add more properties after the builder has been built.");
        }
        properties.setProperty(key, value);
        return this;
    }

    /**
     * Returns the built {@link Properties} instance. This method can only be run once per PropertiesBuilder.
     *
     * @return the built {@link Properties} instance.
     */
    public Properties toProperties() {
        if (built) {
            throw new IllegalStateException("Can only be built once.");
        }
        built = true;
        Properties localProp = this.properties;
        properties = null;
        return localProp;
    }

    /**
     * Returns whether or not {@link #toProperties()} has been called yet.
     *
     * @return whether or not {@link #toProperties()} has been called yet.
     */
    public boolean hasBuilt() {
        return built;
    }

    /**
     * Returns a new PropertiesBuilder with an empty {@link Properties} instance.
     *
     * @return a new PropertiesBuilder with an empty {@link Properties} instance
     */
    public static PropertiesBuilder builder() {
        return new PropertiesBuilder();
    }
}
