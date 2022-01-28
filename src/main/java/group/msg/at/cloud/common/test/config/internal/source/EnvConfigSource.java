package group.msg.at.cloud.common.test.config.internal.source;

import group.msg.at.cloud.common.test.config.internal.spi.ConfigSource;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public final class EnvConfigSource implements ConfigSource, Serializable {

    private static final long serialVersionUID = 4577136784726659703L;

    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(System.getenv());
    }

    @Override
    public int getOrdinal() {
        return 300;
    }

    @Override
    public String getValue(String name) {
        return System.getenv(name);
    }

    @Override
    public String getName() {
        return "EnvConfigSource";
    }
}
