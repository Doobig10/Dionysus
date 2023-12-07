package tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EnvironmentReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(EnvironmentReader.class);

    public static Variable getEnvVariable(String identifier) {
        return new Variable(System.getenv(identifier));
    }

    public static String getEnv(String identifier, String defaultValue) {
        return getEnvVariable(identifier)
                .setDefaultValue(defaultValue)
                .getValue();
    }
}
