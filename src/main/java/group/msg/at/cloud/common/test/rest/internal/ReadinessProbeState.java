package group.msg.at.cloud.common.test.rest.internal;

import group.msg.at.cloud.common.test.config.CommonTestConfig;

/**
 * Common interface of a states a readiness probe can pass through.
 */
interface ReadinessProbeState {

    /**
     * Checks the remote application readiness probe and returns the next readiness probe state, if the end of the
     * readiness probe state machine has not been reached.
     *
     * @param config
     * @return next {@code ReadinessProbeState} of the state machine or {@code null}, if this is the last state of the state machine.
     */
    ReadinessProbeState check(CommonTestConfig config);

    /**
     * Returns {@code true}, if the readiness probe has succeeded.
     */
    boolean hasSucceeded();

    /**
     * Returns {@code true}, if the readiness probe has failed.
     */
    boolean hasFailed();
}
