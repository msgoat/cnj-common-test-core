package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractReadinessProbeState implements ReadinessProbeState {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String extractProbePath(CommonTestConfig config) {
        String basePath = config.getTargetRoute();
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        String probeUri = config.getReadinessProbePath();
        if (!probeUri.startsWith("/")) {
            probeUri = "/" + probeUri;
        }
        return basePath + probeUri;
    }

    @Override
    public abstract ReadinessProbeState check(CommonTestConfig config);
}
