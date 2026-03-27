package Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import config.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportListener - TestNG Listener for Extent Reports HTML reporting
 */
public class ExtentReportListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(ExtentReportListener.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static void initReport() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
//        String reportPath = ConfigReader.getReportPath() + "TestReport_" + timestamp + ".html";
        String reportPath = ConfigReader.getReportPath() + "TestReport" + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Selenium TestNG Framework Report");
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Tester", System.getProperty("user.name"));

        log.info("Extent Report initialized: {}", reportPath);
    }

    public static void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("Extent Report flushed successfully");
        }
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void attachScreenshot(String screenshotPath) {
        if (extentTest.get() != null && screenshotPath != null) {
            extentTest.get().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReports.createTest(
            result.getMethod().getMethodName(),
            result.getMethod().getDescription()
        );
        extentTest.set(test);
        test.info("Test Started: " + result.getMethod().getMethodName());
        log.info(">>> TEST STARTED: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test PASSED: " + result.getName());
        log.info(">>> TEST PASSED: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, "Test FAILED: " + result.getName());
        extentTest.get().log(Status.FAIL, result.getThrowable());
        log.error(">>> TEST FAILED: {} | Reason: {}", result.getName(), result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test SKIPPED: " + result.getName());
        log.warn(">>> TEST SKIPPED: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Suite Finished | Pass: {} | Fail: {} | Skip: {}",
            context.getPassedTests().size(),
            context.getFailedTests().size(),
            context.getSkippedTests().size()
        );
    }
}
