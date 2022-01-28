package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;
import io.restassured.RestAssured;

final class InitialReadinessProbeState extends AbstractReadinessProbeState {

    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        ReadinessProbeState result = new SucceededReadinessProbeState();
        if (!config.isSkipReadinessProbe()) {
            String probePath = extractProbePath(config);
            logger.info(String.format("waiting for application on readiness probe at [%s] to become ready", probePath));
            boolean succeeded = false;
            if (config.getInitialDelaySeconds() > 0) {
                logger.info(String.format("sleeping [%d] seconds before checking readiness probe at [%s] for the first time", config.getInitialDelaySeconds(), probePath));
                try {
                    Thread.sleep(config.getInitialDelaySeconds() * 1000);
                } catch (InterruptedException ex) {
                    logger.error(String.format("got interrupted while sleeping before checking readiness probe at [%s] for the first time", probePath));
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
                        logger.info(String.format("sleeping [%d] seconds before checking readiness probe at [%s] again", config.getPeriodSeconds(), probePath));
                        try {
                            Thread.sleep(config.getPeriodSeconds() * 1000);
                        } catch (InterruptedException ex) {
                            logger.info(String.format("got interrupted while sleeping before checking readiness probe at [%s] again", probePath));
                            break;
                        }
                    }
                }
            }
            if (succeeded) {
                logger.info(String.format("readiness probe at [%s] reported UP", probePath));
            } else {
                logger.error(String.format("readiness check on probe at [%s] failed!", probePath));
                result = new FailedReadinessProbeState();
            }
        } else {
            logger.warn(String.format("assuming application at [%s] to be ready without checking readiness probe", config.getTargetRoute()));
        }
        return result;
    }
}
