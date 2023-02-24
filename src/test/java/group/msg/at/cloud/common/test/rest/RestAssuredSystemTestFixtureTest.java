package group.msg.at.cloud.common.test.rest;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link RestAssuredSystemTestFixture}.
 */
public class RestAssuredSystemTestFixtureTest {

    private final RestAssuredSystemTestFixture underTest = new RestAssuredSystemTestFixture();

    @Test
    public void checkIfFixtureWorks() {
        this.underTest.onBefore();
        this.underTest.onAfter();
    }
}
