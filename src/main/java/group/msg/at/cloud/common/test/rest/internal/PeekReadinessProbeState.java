package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;
import io.restassured.RestAssured;

/**
 * Concrete implementation of a {@code ReadinessProbeState} which peeks for an available application
 * before the actual initialization of the readiness probe starts.
 * <p>
 * Added to optimize test execution times in applications with lots of system test classes.
 * </p>
 */
final class PeekReadinessProbeState extends AbstractReadinessProbeState {

    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        ReadinessProbeState result = new SucceededReadinessProbeState();
        String probePath = extractProbePath(config);
        logger.info(String.format("peeking for readiness probe at [%s] to speed things up", probePath));
        try {
            RestAssured.given()
                    .get(config.getReadinessProbePath())
                    .then()
                    .assertThat()
                    .statusCode(200);
        } catch (AssertionError | Exception ex) {
            result = new InitialReadinessProbeState();
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
