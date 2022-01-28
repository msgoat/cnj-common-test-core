package group.msg.at.cloud.common.test.config.internal.source;

import group.msg.at.cloud.common.test.config.internal.spi.ConfigSource;
import group.msg.at.cloud.common.test.config.internal.spi.ConfigSourceProvider;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class PropertiesConfigSourceProvider implements ConfigSourceProvider {

    private final List<ConfigSource> configSources = new ArrayList<>();

    public PropertiesConfigSourceProvider(String propertyFileName, boolean optional, ClassLoader classLoader) {
        try {
            Enumeration<URL> propertyFileUrls = classLoader.getResources(propertyFileName);

            if (!optional && !propertyFileUrls.hasMoreElements()) {
                throw new IllegalStateException(propertyFileName + " wasn't found.");
            }

            while (propertyFileUrls.hasMoreElements()) {
                URL propertyFileUrl = propertyFileUrls.nextElement();
                this.configSources.add(new PropertiesConfigSource(propertyFileUrl));
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("problem while loading microprofile-config.properties files", ioe);
        }

    }

    @Override
    public List<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        return this.configSources;
    }
}
