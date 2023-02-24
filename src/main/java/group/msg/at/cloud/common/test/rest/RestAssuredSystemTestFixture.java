package group.msg.at.cloud.common.test.rest;

import group.msg.at.cloud.common.test.config.CommonTestConfig;
import group.msg.at.cloud.common.test.config.internal.CommonTestConfigImpl;
import group.msg.at.cloud.common.test.rest.internal.ReadinessProbe;
import group.msg.at.cloud.common.test.rest.internal.ReadinessProbeImpl;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common test fixture for {@code RestAssured} based system tests.
 * <p>
 * Performs an OpenID Connect login to the OpenID Connect provider specified in either properties file
 * {@code META-INF/test-config.properties} or in system properties or in environment variables.
 * </p>
 * <p>
 * Tokens obtained during this login can be retrieved via {@code #getToken}.
 * Performs an OpenID Connect login to the
 * OpenID Connect provider specified in either properties file {@code META-INF/test-config.properties} or
 * in system properties or in environment variables.
 * </p>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 2.0
 * @since release 0.8.0
 */
public class RestAssuredSystemTestFixture {

    private static final Logger logger = LoggerFactory.getLogger(RestAssuredSystemTestFixture.class.getName());

    private CommonTestConfig config;
    private ReadinessProbe probe;
    private String accessToken;
    private String idToken;

    /**
     * Resets this fixture after all tests have been executed
     */
    public void onAfter() {
        RestAssured.reset();
    }

    /**
     * Sets up this fixture before all tests are run.
     * <p>
     * A typical setup consists of the following steps:
     * </p>
     * <ul>
     * <li>Retrieve configuration from properties file {@code META-INF/test-config.properties} or
     * from system properties or from environment variables.</li>
     * <li>Retrieve target route to REST endpoint to be tested from system property {@code target.route}
     * or environment variable {@code TARGET_ROUTE}.</li>
     * <li>Performs a login to the specified OpenID Connect provider obtaining an access accessToken and an ID accessToken.</li>
     * <li>Waits to the specified target service to become ready by checking its readiness probe
     * at URI {@code api/v1/probes/readiness}.</li>
     * </ul>
     */
    public void onBefore() {
        ensureConfiguration();
        LogConfig augmentedLogConfig = RestAssured.config.getLogConfig();
        augmentedLogConfig.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()).logConfig(augmentedLogConfig);
        RestAssured.baseURI = config.getTargetRoute();
        ensureApplicationReadiness();
        ensureTokens();
    }

    /**
     * Returns the access token obtained during login.
     */
    public String getToken() {
        return this.accessToken;
    }

    /**
     * Returns the access token obtained during login.
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Returns the ID token obtained during login.
     */
    public String getIdToken() {
        return this.idToken;
    }

    /**
     * Returns the common test configuration this fixture uses.
     */
    public CommonTestConfig getConfig() {
        if (this.config == null) {
            throw new IllegalStateException("Fixture has not been properly initialized! Please call onBefore() first.");
        }
        return config;
    }

    /**
     * Reads some configuration from a config datasource.
     */
    private void ensureConfiguration() {
        if (this.config == null) {
            this.config = new CommonTestConfigImpl();
        }
    }

    /**
     * Waits until the application is actually ready to accept requests.
     */
    private void ensureApplicationReadiness() {
        if (this.probe == null) {
            this.probe = new ReadinessProbeImpl(config);
        }
        this.probe.ensureApplicationReadiness();
    }

    private void ensureTokens() {
        if (this.accessToken == null && !this.config.isSkipOpenIdConnectLogin()) {
            ExtractableResponse response = RestAssured.given()
                    .param("scope", "openid microprofile-jwt")
                    .param("grant_type", "password")
                    .param("username", this.config.getOidcUserName())
                    .param("password", this.config.getOidcPassword())
                    .param("client_id", this.config.getOidcClientId())
                    .param("client_secret", this.config.getOidcClientSecret())
                    .when().post(this.config.getOidcAccessTokenUri()).then().contentType
                            (ContentType.JSON).extract();
            this.accessToken = response.jsonPath().getString("access_token");
            this.idToken = response.jsonPath().getString("id_token");
            if (this.accessToken == null) {
                throw new IllegalStateException("expected authentication provider to return access token but got none");
            } else {
                logger.info(String.format("got access token: \"%s\"", this.accessToken));
            }
            if (this.idToken == null) {
                logger.warn("expected authentication provider to return ID token but got none");
            } else {
                logger.info(String.format("got ID token: \"%s\"", this.idToken));
            }
        }
    }
}
