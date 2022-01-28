package group.msg.at.cloud.common.test.config.internal.source;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UserPropertiesConfigSourceProviderTest {

    @Test
    public void getConfigSourcesWithFileAtConfigEnvVarReturnsNonEmptyList() {
        UserPropertiesConfigSourceProvider.Environment env = new UserPropertiesConfigSourceProvider.Environment();
        env.putEnvironmentVariable(UserPropertiesConfigSourceProvider.USER_PROPERTIES_ENVVAR_NAME, Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "META-INF", "test-config.properties").toString());
        UserPropertiesConfigSourceProvider provider = new UserPropertiesConfigSourceProvider(env);
        assertNotNull(provider.getConfigSources(null), "config sources list must not be null");
        assertFalse(provider.getConfigSources(null).isEmpty(), "config sources list must not be empty");
    }

    @Test
    public void getConfigSourcesWithFileAtUserHomeReturnsNonEmptyList() {
        UserPropertiesConfigSourceProvider.Environment env = new UserPropertiesConfigSourceProvider.Environment();
        env.putSystemProperty("user.home", Paths.get(System.getProperty("user.dir"), "src", "test", "resources").toString());
        UserPropertiesConfigSourceProvider provider = new UserPropertiesConfigSourceProvider(env);
        assertNotNull(provider.getConfigSources(null), "config sources list must not be null");
        assertFalse(provider.getConfigSources(null).isEmpty(), "config sources list must not be empty");
    }

    @Test
    public void getConfigSourcesWithBothAlternativesOnlyReturnsOneConfigSource() {
        UserPropertiesConfigSourceProvider.Environment env = new UserPropertiesConfigSourceProvider.Environment();
        env.putEnvironmentVariable(UserPropertiesConfigSourceProvider.USER_PROPERTIES_ENVVAR_NAME, Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "META-INF", "test-config.properties").toString());
        env.putSystemProperty("user.home", Paths.get(System.getProperty("user.dir"), "src", "test", "resources").toString());
        UserPropertiesConfigSourceProvider provider = new UserPropertiesConfigSourceProvider(env);
        assertNotNull(provider.getConfigSources(null), "config sources list must not be null");
        assertEquals(1, provider.getConfigSources(null).size(), "config sources list must contain exactly one config source");
    }

}