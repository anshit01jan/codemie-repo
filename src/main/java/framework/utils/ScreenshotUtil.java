package framework.utils;

import framework.drivers.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ScreenshotUtil {
    private ScreenshotUtil() {}

    public static String capture(String fileName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver == null) return null;
            if (!(driver instanceof TakesScreenshot ts)) return null;

            File src = ts.getScreenshotAs(OutputType.FILE);
            Path screenshotsDir = Path.of("target", "screenshots");
            Files.createDirectories(screenshotsDir);

            Path dest = screenshotsDir.resolve(fileName);
            Files.copy(src.toPath(), dest);
            return dest.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
