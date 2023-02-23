package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks if the given target application is ready to accept requests using the given
 * readiness probe endpoint.
 */
public final class ReadinessProbeImpl implements ReadinessProbe {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CommonTestConfig config;

    private ReadinessProbeState state = new PeekReadinessProbeState();

    public ReadinessProbeImpl(CommonTestConfig config) {
        this.config = config;
    }

    /**
     * Waits until the application is actually ready to accept requests.
     *
     * @throws IllegalStateException if the given application does not report ready
     */
    @Override
    public void ensureApplicationReadiness() {
        if (!config.isSkipReadinessProbe()) {
            ReadinessProbeState nextState = this.state.check(this.config);
            while (nextState != null) {
                this.state = nextState;
                nextState = this.state.check(this.config);
            }
            if (this.state.hasFailed()) {
                throw new IllegalStateException(String.format("failed to ensure readiness of application at [%s]", RestAssured.baseURI));
            }
        } else {
            logger.warn(String.format("assuming application at [%s] to be ready without checking readiness probe", RestAssured.baseURI));
        }
    }
}
