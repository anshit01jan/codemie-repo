package framework.utils;

import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private Config() {}

    public static synchronized void load() {
        if (loaded) return;
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found under src/test/resources");
            }
            props.load(is);
            loaded = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /** Generic getter used by framework (DriverFactory, etc.). */
    public static String get(String key) {
        load();
        return props.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getEnv() {
        String env = get("env");
        return (env == null || env.isBlank()) ? "local" : env;
    }

    public static int getTimeoutSeconds() {
        String v = get("timeoutSeconds");
        return v == null ? 10 : Integer.parseInt(v);
    }

    public static int getPollingMillis() {
        String v = get("pollingMillis");
        return v == null ? 250 : Integer.parseInt(v);
    }

    public static boolean isHeadless() {
        String v = get("headless");
        return v != null && v.equalsIgnoreCase("true");
    }

    /** Latest-run-only: keep this pointing to a stable file in target/ and overwrite on each run. */
    public static String getExtentReportPath() {
        String p = get("extentReportPath");
        return (p == null || p.isBlank()) ? "target/extent-report.html" : p;
    }
}
