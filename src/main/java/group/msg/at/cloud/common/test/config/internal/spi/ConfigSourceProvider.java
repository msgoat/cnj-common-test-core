package group.msg.at.cloud.common.test.config.internal.spi;

import group.msg.at.cloud.common.test.config.internal.Config;

public interface ConfigSourceProvider {

    /**
     * Return the collection of {@link ConfigSource}s.
     * For each e.g. property file, we return a single ConfigSource or an empty list if no ConfigSource exists.
     *
     * @param forClassLoader the classloader which should be used if any is needed
     * @return the {@link ConfigSource ConfigSources} to register within the {@link Config}.
     */
    Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader);
}