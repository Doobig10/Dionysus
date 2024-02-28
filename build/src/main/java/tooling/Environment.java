package tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public final class Environment {
    private static final Logger LOGGER = LoggerFactory.getLogger(Environment.class);

    private static final Map<String,String> env = System.getenv();

    public static String get(String identifier) {
        return Environment.env.get(identifier);
    }

    public static String getOrDefault(String identifier, String fallback) {
        return Environment.env.getOrDefault(identifier, fallback);
    }

    public static class EnvTool<T> {

        private final Class<T> type;

        public EnvTool(Class<T> clazz) {
            this.type = clazz;
        }

        @SuppressWarnings("unchecked")
        private T parseEnv(String env) {
            try {
                return (T) Parser.getParser(this.type).apply(env);
            } catch (Parser.ParserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        public T get(String identifier) {
            return parseEnv(Environment.get(identifier));
        }

        public T getOrDefault(String identifier, T fallback) {
            String env = Environment.getOrDefault(identifier, null);
            return (env != null) ? parseEnv(env) : fallback;
        }

    }


}