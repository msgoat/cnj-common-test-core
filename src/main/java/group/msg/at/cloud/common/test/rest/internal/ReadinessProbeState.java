package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

interface ReadinessProbeState {
    ReadinessProbeState check(CommonTestConfig config);
}
