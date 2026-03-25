package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from config.properties
 * Supports system property overrides for CI/CD pipelines
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    

    static {    
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
            log.info("Configuration loaded from: {}", CONFIG_PATH);
        } catch (IOException e) {
            log.error("Failed to load config.properties: {}", e.getMessage());
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    /**
     * Get property value - System property overrides file property (useful for CI/CD)
     */
    public static String get(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in config", key);
        }
        return value;
    }

    public static String getBrowser() {
        return get("browser") != null ? get("browser") : "chrome";
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(get("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
    }

    public static boolean isHeadless() {
        String val = get("headless");
        return val != null && val.equalsIgnoreCase("true");
    }

    public static boolean isScreenshotOnFailure() {
        String val = get("screenshot.on.failure");
        return val == null || val.equalsIgnoreCase("true");
    }

    public static String getScreenshotPath() {
        return get("screenshot.path");
    }

    public static String getReportPath() {
        return get("report.path");
    }
}
