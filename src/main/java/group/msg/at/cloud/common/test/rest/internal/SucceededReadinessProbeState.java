package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

final class SucceededReadinessProbeState extends AbstractReadinessProbeState {

    @Override
    public ReadinessProbeState check(CommonTestConfig config) {
        logger.info(String.format("readiness probe at [%s] reported UP", extractProbePath(config)));
        return this;
    }
}
