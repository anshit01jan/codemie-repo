package framework.utils.environment;

import framework.utils.Config;

import java.io.InputStream;
import java.util.Properties;

public final class Env {
    private static final Properties envProps = new Properties();
    private static boolean loaded = false;

    private Env() {}

    private static synchronized void load() {
        if (loaded) return;
        try (InputStream is = Env.class.getClassLoader().getResourceAsStream("env.properties")) {
            if (is == null) {
                throw new RuntimeException("env.properties not found");
            }
            envProps.load(is);
            loaded = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load env.properties", e);
        }
    }

    private static String getFromEnv(String suffix) {
        load();
        String key = Config.getEnv() + "." + suffix;
        String v = envProps.getProperty(key);
        return v == null ? "" : v;
    }

    public static String baseUrl() {
        String v = getFromEnv("baseUrl");
        return (v == null || v.isBlank()) ? Config.getBaseUrl() : v;
    }

    public static String apiUrl() {
        return getFromEnv("apiUrl");
    }
}
