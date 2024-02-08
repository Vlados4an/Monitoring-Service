package ru.erma.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The PropertyLoader class is used to load properties from a file.
 * It contains a Properties object that holds the loaded properties.
 * The properties are loaded from the file specified by the filePath parameter in the constructor.
 */
public class PropertyLoader {
    private final Properties properties;

    /**
     * Constructs a new PropertyLoader instance and loads properties from the specified file.
     * The properties are loaded using the Properties class's load method.
     * If the file is not found, it throws a RuntimeException.
     * If an error occurs during the reading of the file, it throws a RuntimeException.
     *
     * @param filePath the path of the file from which the properties are loaded
     * @throws RuntimeException if the file is not found or an error occurs during the reading of the file
     */
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

    /**
     * Returns the Properties object that holds the loaded properties.
     *
     * @return the Properties object that holds the loaded properties
     */
    public Properties getProperties() {
        return properties;
    }
}