package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

/**
 * Concrete implementation of a {@code ReadinessProbeState} which indicates
 * that the application under test is not available.
 */
final class FailedReadinessProbeState extends AbstractReadinessProbeState {
    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        logger.error(String.format("readiness check on probe at [%s] failed!", extractProbePath(config)));
        return null;
    }

    @Override
    public boolean hasSucceeded() {
        return false;
    }

    @Override
    public boolean hasFailed() {
        return true;
    }
}
