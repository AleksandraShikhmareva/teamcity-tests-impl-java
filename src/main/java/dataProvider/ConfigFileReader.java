package dataProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private static Logger logger = LoggerFactory.getLogger(ConfigFileReader.class);

    public ConfigFileReader() {
        String propertyFilePath = "src//main//resources//project.properties";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(propertyFilePath))) { //try with resources
            properties = new Properties();
            properties.load(bufferedReader);
        } catch (IOException e) {
            logger.error("Config FileReader Initialization error", e);
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath, e);
        }
    }

    public String getProperty(String name) {
        String propertyValue = properties.getProperty(name);
        if (propertyValue != null) return propertyValue;
        else throw new RuntimeException(name + " not specified in the Configuration.properties file");
    }

}
