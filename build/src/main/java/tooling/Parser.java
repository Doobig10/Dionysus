package tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.function.Function;
public final class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    private static final HashMap<Class<?>, Function<String, ?>> parser = new HashMap<>();

    static {
        LOGGER.atTrace().log("Inserting parser methods");

        parser.put(String.class,    String::valueOf);

        parser.put(boolean.class,   Boolean::parseBoolean);
        parser.put(Boolean.class,   Boolean::valueOf);

        parser.put(int.class,       Integer::parseInt);
        parser.put(Integer.class,   Integer::valueOf);

        parser.put(long.class,      Long::parseLong);
        parser.put(Long.class,      Long::valueOf);

        parser.put(Double.class,    Double::valueOf);

        parser.put(Float.class,     Float::valueOf);
    }

    public static Function<String, ?> getParser(Class<?> clazz) throws ParserNotFoundException {
        if (parser.containsKey(clazz)) {
            return parser.get(clazz);
        }
        else {
            throw new ParserNotFoundException();
        }
    }

    public static class ParserNotFoundException extends Exception {}

}
