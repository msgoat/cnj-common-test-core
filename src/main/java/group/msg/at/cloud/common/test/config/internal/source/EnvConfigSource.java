package group.msg.at.cloud.common.test.config.internal.source;

import group.msg.at.cloud.common.test.config.internal.spi.ConfigSource;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code ConfigSource} which pulls configuration values from environment variables.
 */
public final class EnvConfigSource implements ConfigSource, Serializable {

    private static final long serialVersionUID = 4577136784726659703L;

    private final EnvVarNameMapper mapper = new EnvVarNameMapper();

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> result = new HashMap<>();
        System.getenv().forEach((k,v) -> result.put(mapper.mapEnvVarNameToPropertyName(k), v));
        return Collections.unmodifiableMap(result);
    }

    @Override
    public int getOrdinal() {
        return 300;
    }

    @Override
    public String getValue(String name) {
        return System.getenv(mapper.mapPropertyNameToEnvVarName(name));
    }

    @Override
    public String getName() {
        return "EnvConfigSource";
    }
}
