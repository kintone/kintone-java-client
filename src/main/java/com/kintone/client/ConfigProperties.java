package com.kintone.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class ConfigProperties {

    private static final String PROPERTIES_FILE = "/config.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = ConfigProperties.class.getResourceAsStream(PROPERTIES_FILE)) {
            PROPS.load(input);
        } catch (IOException e) {
            throw new AssertionError("Failed to load resource", e);
        }
    }

    public static String getVersion() {
        return PROPS.getProperty("kintone.client.version");
    }
}
