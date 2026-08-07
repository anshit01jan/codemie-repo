package framework.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.logger;

public final class Logger {
    private static final org.apache.log4j.Logger LOG = LogManager.getLogger();

    private Logger() {}

    public static void info(String message) {
        LOG.info(message);
    }

    public static void debug(String message) {
        LOG.debug(message);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void error(String message, Throwable t) {
        LOG.error(message, t);
    }
}
