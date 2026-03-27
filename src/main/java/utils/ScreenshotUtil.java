package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - Captures screenshots on test failure
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "src/main/resources/screenshots/";

    private ScreenshotUtil() {}

    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            log.warn("Driver is null - cannot capture screenshot");
            return null;
        }

        try {
            // Ensure screenshots directory exists
            new File(SCREENSHOT_DIR).mkdirs();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileUtils.copyFile(source, destination);

            log.info("Screenshot captured: {}", filePath);
            return filePath;

        } catch (IOException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
