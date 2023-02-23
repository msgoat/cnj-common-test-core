package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;
import io.restassured.RestAssured;

/**
 * Concrete implementation of a {@code ReadinessProbeState} which indicates
 * that the application under test needs to be checked for availability.
 */
final class InitialReadinessProbeState extends AbstractReadinessProbeState {

    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        ReadinessProbeState result = new SucceededReadinessProbeState();
        String probePath = extractProbePath(config);
        logger.info(
                String.format("waiting for application on readiness probe at [%s] with initial delay of [%d] second(s) and a failure threshold of [%d] to become ready",
                        probePath,
                        config.getInitialDelaySeconds(),
                        config.getFailureThreshold()));
        boolean succeeded = false;
        if (config.getInitialDelaySeconds() > 0) {
            logger.info(String.format("sleeping [%d] second(s) before checking readiness probe at [%s] for the first time", config.getInitialDelaySeconds(), probePath));
            try {
                Thread.sleep(config.getInitialDelaySeconds() * 1000L);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(String.format("got interrupted while sleeping before checking readiness probe at [%s] for the first time", probePath), ex);
            }
        }
        int failureThreshold = config.getFailureThreshold();
        while (!succeeded && failureThreshold > 0) {
            try {
                RestAssured.given()
                        .get(config.getReadinessProbePath())
                        .then()
                        .assertThat()
                        .statusCode(200);
                succeeded = true;
            } catch (AssertionError | Exception ex) {
                // explicitly ignore any exceptions
                logger.info(String.format("checking readiness probe at [%s] failed (assuming application is still booting): %s", probePath, ex));
            }
            if (!succeeded) {
                if (--failureThreshold > 0) {
                    logger.info(String.format("sleeping [%d] second(s) before checking readiness probe at [%s] again", config.getPeriodSeconds(), probePath));
                    try {
                        Thread.sleep(config.getPeriodSeconds() * 1000L);
                    } catch (InterruptedException ex) {
                        throw new IllegalStateException(String.format("got interrupted while sleeping before checking readiness probe at [%s] again", probePath), ex);
                    }
                }
            }
        }
        if (!succeeded) {
            result = new FailedReadinessProbeState();
        }
        return result;
    }

    @Override
    public boolean hasSucceeded() {
        return false;
    }

    @Override
    public boolean hasFailed() {
        return false;
    }
}
