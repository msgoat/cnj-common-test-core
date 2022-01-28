package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

final class FailedReadinessProbeState extends AbstractReadinessProbeState {
    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        logger.error(String.format("readiness check on probe at [%s] failed!", extractProbePath(config)));
        return this;
    }
}
