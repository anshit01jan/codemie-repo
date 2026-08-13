package framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import framework.utils.Config;
import framework.utils.Logger;

public final class ExtentManager {
    private static ExtentReports extentReports;

    private ExtentManager() {}

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            init();
        }
        return extentReports;
    }

    private static void init() {
        try {
            ExtentSparkReporter spark = new ExtentSparkReporter(Config.getExtentReportPath());
            spark.config().setDocumentTitle("CodeMie Automation Report");
            spark.config().setReportName("UI Automation");

            extentReports = new ExtentReports();
            extentReports.attachReporter(spark);
        } catch (Exception e) {
            Logger.error("Failed to initialize ExtentReports", e);
            throw new RuntimeException(e);
        }
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
