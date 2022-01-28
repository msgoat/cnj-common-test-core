package group.msg.at.cloud.common.test.config.internal.source;

import group.msg.at.cloud.common.test.config.internal.spi.ConfigSource;
import group.msg.at.cloud.common.test.config.internal.spi.ConfigSourceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * {@code ConfigSourceProvider} which reads properties files from user-specific locations.
 * <p>
 * There are two possible locations for config sources:
 * </p>
 * <ul>
 * <li>either define an environment variable called {@link #USER_PROPERTIES_ENVVAR_NAME} set to
 * a full pathname of a properties file</li>
 * <li>or add a properties file named {@link #USER_PROPERTIES_FILE_NAME} to a subfolder named {@link #USER_PROPERTIES_FOLDER_NAME}
 * of your user home directory</li>
 * </ul>
 * <p>
 * If both alternatives are present, the properties files specified in the environment variable is preferred over
 * the properties file in your user home directory.
 * </p>
 */
public final class UserPropertiesConfigSourceProvider implements ConfigSourceProvider {

    public static final String USER_PROPERTIES_FOLDER_NAME = ".cnj";

    public static final String USER_PROPERTIES_FILE_NAME = "test-config.properties";

    public static final String USER_PROPERTIES_ENVVAR_NAME = "CNJ_USER_PROPERTIES_CONFIG";

    public static final int DEFAULT_CONFIG_ORDINAL = 200;

    private final List<ConfigSource> configSources = new ArrayList<>();

    /**
     * Default constructor using {@link System} as an environment provider.
     */
    public UserPropertiesConfigSourceProvider() {
        this(new Environment(System.getenv(), new HashMap(System.getProperties())));
    }

    /**
     * Specialized constructor for testing using the given environment provider.
     *
     * @param env environment provider
     */
    UserPropertiesConfigSourceProvider(Environment env) {
        createConfigSourceFromConfigEnvVar(env).ifPresent(cs -> configSources.add(cs));
        if (configSources.isEmpty()) {
            createConfigSourceFromUserHome(env).ifPresent(cs -> configSources.add(cs));
        }
    }

    private Optional<PropertiesConfigSource> createConfigSourceFromUserHome(Environment env) {
        PropertiesConfigSource result = null;
        String userHomeDir = env.getSystemProperty("user.home");
        Path userPropsPath = Paths.get(userHomeDir, USER_PROPERTIES_FOLDER_NAME, USER_PROPERTIES_FILE_NAME);
        if (Files.exists(userPropsPath)) {
            try {
                Properties userProps = loadPropertiesFromPath(userPropsPath);
                result = new PropertiesConfigSource(new HashMap(userProps), userPropsPath.toString(), DEFAULT_CONFIG_ORDINAL);
            } catch (IOException ex) {
                throw new IllegalStateException(String.format("problem while loading user properties [%s] located at user home [%s]", userPropsPath.toString(), userHomeDir), ex);
            }
        }
        return Optional.ofNullable(result);
    }

    private Optional<PropertiesConfigSource> createConfigSourceFromConfigEnvVar(Environment env) {
        PropertiesConfigSource result = null;
        String configEnvVar = env.getEnvironmentVariable(USER_PROPERTIES_ENVVAR_NAME);
        if (configEnvVar != null) {
            Path userPropsPath = Paths.get(configEnvVar);
            if (Files.exists(userPropsPath)) {
                try {
                    Properties userProps = loadPropertiesFromPath(userPropsPath);
                    result = new PropertiesConfigSource(new HashMap(userProps), userPropsPath.toString(), DEFAULT_CONFIG_ORDINAL);
                } catch (IOException ex) {
                    throw new IllegalStateException(String.format("problem while loading user properties [%s] defined by env var [%s]", userPropsPath.toString(), USER_PROPERTIES_ENVVAR_NAME), ex);
                }
            }
        }
        return Optional.ofNullable(result);
    }

    private Properties loadPropertiesFromPath(Path propertiesPath) throws IOException {
        Properties result = null;
        try (InputStream in = Files.newInputStream(propertiesPath)) {
            result = new Properties();
            result.load(in);
        }
        return result;
    }

    @Override
    public List<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        return this.configSources;
    }

    public static final class Environment {
        private final Map<String, String> environmentVariablesByName = new HashMap<>();
        private final Map<String, String> systemPropertiesByName = new HashMap<>();

        public Environment() {
        }

        public Environment(Map<String, String> environmentVariablesSource, Map<String, String> systemPropertiesSource) {
            environmentVariablesByName.putAll(environmentVariablesSource);
            systemPropertiesByName.putAll(systemPropertiesSource);
        }

        public void putEnvironmentVariable(String name, String value) {
            environmentVariablesByName.put(name, value);
        }

        public String getEnvironmentVariable(String name) {
            return environmentVariablesByName.get(name);
        }

        public void putSystemProperty(String name, String value) {
            systemPropertiesByName.put(name, value);
        }

        public String getSystemProperty(String name) {
            return systemPropertiesByName.get(name);
        }
    }
}
