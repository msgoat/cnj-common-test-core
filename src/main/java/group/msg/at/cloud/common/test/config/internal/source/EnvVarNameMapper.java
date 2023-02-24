package group.msg.at.cloud.common.test.config.internal.source;

import java.util.Map;

import static java.util.Map.entry;

/**
 * Simple solution to map environment variable names to property names of well-known test configuration properties.
 */
final class EnvVarNameMapper {

    private static final Map<String, String> ENV_VAR_NAMES_BY_PROPERTY_NAMES = Map.ofEntries(
            entry("test.target.route", "TEST_TARGET_ROUTE"),
            entry("test.target.readinessProbe.skip", "TEST_TARGET_READINESS_PROBE_SKIP"),
            entry("test.target.readinessProbe.path", "TEST_TARGET_READINESS_PROBE_PATH"),
            entry("test.target.readinessProbe.initialDelaySeconds", "TEST_TARGET_READINESS_PROBE_INITIAL_DELAY_SECONDS"),
            entry("test.target.readinessProbe.failureThreshold", "TEST_TARGET_READINESS_PROBE_FAILURE_THRESHOLD"),
            entry("test.target.readinessProbe.periodSeconds", "TEST_TARGET_READINESS_PROBE_PERIOD_SECONDS"),
            entry("test.target.readinessProbe.timeoutSeconds", "TEST_TARGET_READINESS_PROBE_TIMEOUT_SECONDS"),
            entry("test.oidc.skip", "TEST_OIDC_SKIP"),
            entry("test.oidc.client.clientId", "TEST_OIDC_CLIENT_CLIENT_ID"),
            entry("test.oidc.client.clientSecret", "TEST_OIDC_CLIENT_CLIENT_SECRET"),
            entry("test.oidc.client.accessTokenUri", "TEST_OIDC_CLIENT_ACCESS_TOKEN_URI"),
            entry("test.oidc.client.user", "TEST_OIDC_CLIENT_USER"),
            entry("test.oidc.client.password", "TEST_OIDC_CLIENT_PASSWORD")
    );
    private static final Map<String, String> PROPERTY_NAMES_BY_ENV_VAR_NAMES = Map.ofEntries(
            entry("TEST_TARGET_ROUTE", "test.target.route"),
            entry("TEST_TARGET_READINESS_PROBE_SKIP", "test.target.readinessProbe.skip"),
            entry("TEST_TARGET_READINESS_PROBE_PATH", "test.target.readinessProbe.path"),
            entry("TEST_TARGET_READINESS_PROBE_INITIAL_DELAY_SECONDS", "test.target.readinessProbe.initialDelaySeconds"),
            entry("TEST_TARGET_READINESS_PROBE_FAILURE_THRESHOLD", "test.target.readinessProbe.failureThreshold"),
            entry("TEST_TARGET_READINESS_PROBE_PERIOD_SECONDS", "test.target.readinessProbe.periodSeconds"),
            entry("TEST_TARGET_READINESS_PROBE_TIMEOUT_SECONDS", "test.target.readinessProbe.timeoutSeconds"),
            entry("TEST_OIDC_SKIP", "test.oidc.skip"),
            entry("TEST_OIDC_CLIENT_CLIENT_ID", "test.oidc.client.clientId"),
            entry("TEST_OIDC_CLIENT_CLIENT_SECRET", "test.oidc.client.clientSecret"),
            entry("TEST_OIDC_CLIENT_ACCESS_TOKEN_URI", "test.oidc.client.accessTokenUri"),
            entry("TEST_OIDC_CLIENT_USER", "test.oidc.client.user"),
            entry("TEST_OIDC_CLIENT_PASSWORD", "test.oidc.client.password")
    );

    /**
     * Maps the given env var name to a property name, if possible.
     */
    public String mapEnvVarNameToPropertyName(String envVarName) {
        return PROPERTY_NAMES_BY_ENV_VAR_NAMES.getOrDefault(envVarName, envVarName);
    }

    /**
     * Maps the given property name to an environment variable name, if possible.
     */
    public String mapPropertyNameToEnvVarName(String propertyName) {
        return ENV_VAR_NAMES_BY_PROPERTY_NAMES.getOrDefault(propertyName, propertyName);
    }
}
