package group.msg.at.cloud.common.test.config.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

/**
 * Ecapsulates all the configuration part of the system test components.
 */
public final class CommonTestConfigImpl implements CommonTestConfig {

    private Config config;
    private boolean skipOpenIdConnectLogin;
    private String oidcClientId;
    private String oidcClientSecret;
    private String oidcAccessTokenUri;
    private String oidcUserName;
    private String oidcPassword;
    private String targetRoute;
    private boolean skipReadinessProbe;
    private String readinessProbePath;
    private int initialDelaySeconds;
    private int failureThreshold;
    private int periodSeconds;
    private int timeoutSeconds;

    public CommonTestConfigImpl() {
        load();
    }

    private void load() {
        if (this.config == null) {
            this.config = ConfigProvider.getConfig();
        }
        targetRoute = this.config.getOptionalValue("target.route", String.class).orElse(this.config.getValue("test.target.route", String.class));
        skipReadinessProbe = this.config.getOptionalValue("test.target.readinessProbe.skip", Boolean.class).orElse(false);
        readinessProbePath = this.config.getOptionalValue("test.target.readinessProbe.path", String.class).orElse("api/v1/probes/readiness");
        initialDelaySeconds = this.config.getOptionalValue("test.target.readinessProbe.initialDelaySeconds", Integer.class).orElse(10);
        failureThreshold = this.config.getOptionalValue("test.target.readinessProbe.failureThreshold", Integer.class).orElse(3);
        periodSeconds = this.config.getOptionalValue("test.target.readinessProbe.periodSeconds", Integer.class).orElse(10);
        timeoutSeconds = this.config.getOptionalValue("test.target.readinessProbe.timeoutSeconds", Integer.class).orElse(1);
        skipOpenIdConnectLogin = this.config.getOptionalValue("test.oidc.skip", Boolean.class).orElse(false);
        if (!skipOpenIdConnectLogin) {
            oidcClientId = this.config.getValue("test.oidc.client.clientId", String.class);
            oidcClientSecret = this.config.getValue("test.oidc.client.clientSecret", String.class);
            oidcAccessTokenUri = this.config.getValue("test.oidc.client.accessTokenUri", String.class);
            oidcUserName = this.config.getValue("test.oidc.client.user", String.class);
            oidcPassword = this.config.getValue("test.oidc.client.password", String.class);
        }
    }

    public Config getConfig() {
        return config;
    }

    @Override
    public boolean isSkipOpenIdConnectLogin() {
        return skipOpenIdConnectLogin;
    }

    @Override
    public String getOidcClientId() {
        return oidcClientId;
    }

    @Override
    public String getOidcClientSecret() {
        return oidcClientSecret;
    }

    @Override
    public String getOidcAccessTokenUri() {
        return oidcAccessTokenUri;
    }

    @Override
    public String getOidcUserName() {
        return oidcUserName;
    }

    @Override
    public String getOidcPassword() {
        return oidcPassword;
    }

    @Override
    public String getTargetRoute() {
        return targetRoute;
    }

    @Override
    public boolean isSkipReadinessProbe() {
        return skipReadinessProbe;
    }

    @Override
    public String getReadinessProbePath() {
        return readinessProbePath;
    }

    @Override
    public int getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    @Override
    public int getFailureThreshold() {
        return failureThreshold;
    }

    @Override
    public int getPeriodSeconds() {
        return periodSeconds;
    }

    @Override
    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }
}
