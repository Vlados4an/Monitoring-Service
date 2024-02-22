package ru.erma.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * This class is a factory for creating PropertySource objects from YAML configuration files.
 * It implements the PropertySourceFactory interface provided by Spring.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    /**
     * This method creates a PropertySource object from the specified EncodedResource.
     * The EncodedResource represents a YAML configuration file.
     * The method uses a YamlPropertiesFactoryBean to load the properties from the YAML file.
     * The properties are then used to create a new PropertiesPropertySource.
     *
     * @param name the name of the PropertySource
     * @param encodedResource the EncodedResource representing the YAML configuration file
     * @return the created PropertySource
     * @throws IOException if an I/O error occurs when reading from the YAML file
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource)
            throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties props = factory.getObject();
        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), props);
    }
}
