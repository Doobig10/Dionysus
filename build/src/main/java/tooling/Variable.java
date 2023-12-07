package tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Variable {
    private final static Logger LOGGER = LoggerFactory.getLogger(Variable.class);

    private String value;

    public Variable(String env) {
        this.value = env;
    }

    public Variable setDefaultValue(String defaultValue) {
        if (this.value == null) {
            this.value = defaultValue;
        }
        return this;
    }

    public String getValue() {
        return value;
    }
}
