package group.msg.at.cloud.common.test.config.internal.source;

import group.msg.at.cloud.common.test.config.internal.spi.ConfigSource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class SysPropConfigSource implements ConfigSource, Serializable {

    private static final long serialVersionUID = 7112548779237972319L;

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap(System.getProperties());
        return map;
    }

    @Override
    public int getOrdinal() {
        return 400;
    }

    @Override
    public String getValue(String s) {
        return System.getProperty(s);
    }

    @Override
    public String getName() {
        return "SysPropConfigSource";
    }
}
