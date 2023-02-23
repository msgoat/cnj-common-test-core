package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

/**
 * Concrete implementation of a {@code ReadinessProbeState} which indicates
 * that the application under test is available.
 */
final class SucceededReadinessProbeState extends AbstractReadinessProbeState {

    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        logger.info(String.format("readiness probe at [%s] reported UP", extractProbePath(config)));
        return null;
    }

    @Override
    public boolean hasSucceeded() {
        return true;
    }

    @Override
    public boolean hasFailed() {
        return false;
    }
}
