package group.msg.at.cloud.common.test.rest.internal;

/**
 * Checks if the given target application is ready to accept requests using the given
 * readiness probe endpoint.
 */
public interface ReadinessProbe {

    /**
     * Waits until the application is actually ready to accept requests.
     *
     * @throws IllegalStateException if the given application does not report ready
     */
    void ensureApplicationReadiness();
}
