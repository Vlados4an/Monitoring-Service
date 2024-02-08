package ru.erma.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    private final Properties properties;

    public PropertyLoader(String filePath) {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(filePath));
        } catch (FileNotFoundException e ) {
            throw new RuntimeException("Property file not found!");
        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file: " + e.getMessage());
        }
    }

    public Properties getProperties() {
        return properties;
    }
}