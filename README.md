# cnj-common-test-core

Provides common test classes to run system tests on REST APIs using RestAssured.

__Note:__ Logging is done using [SLF4J](http://www.slf4j.org/index.html). All classes provided by this module do not depend on any specific SL4FJ binding, 
although using the [LOG4J 2 SLF4J binding](https://logging.apache.org/log4j/2.0/log4j-slf4j-impl/index.html) is recommended for your test classes. A sample LOG4J 2 configuration can be found in 
[log4j2-test.xml](src/test/resources/log4j2-test.xml) 

## Status

![Build status](https://codebuild.eu-west-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoicit4ZDk3Y0VoK05xVWpMSU1yaDBKRE1ma0dITVNsSjJ6SnJmVGcwVGcxb2UyOVlkeldQdzRaUzJpZC8zSGcxUGMveEJxTll2dkNKb1lScnFidmwvMVdrPSIsIml2UGFyYW1ldGVyU3BlYyI6Ii9FUlY2cnFMditzVU1WOXMiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=main)

## Release information

A changelog can be found in [changelog.md](changelog.md).

## Exported Test Classes

### RestAssuredSystemTestFixture

Provides a text fixture for system test classes running on REST APIs which performs an OpenID Connect login obtaining an 
access token and an ID token from the specified OpenID Connect provider.

__Prerequisites:__
* expects a system property __test.target.route__ or an environment variable __TEST_TARGET_ROUTE__to be set to the base URL of the REST endpoint to test.
* expects a properties configuration file __META-INF/test-config.properties__ providing the following properties:

| Property Name | Type | Mandatory? | Description |
| --- | --- | --- | --- |
| test.oidc.skip | bool |  | true, if OpenID Connect authentication should be skipped and test.oidc.* properties are not specified (default: false) |
| test.oidc.client.clientId | string | x | OpenID client ID; must match the unique identifier of a registered client on an OpenID Connect provider |
| test.oidc.client.clientSecret | string | x | OpenID client credentials; must match the credentials of a registered client on an OpenID Connect provider |
| test.oidc.client.accessTokenUri | string | x | Target URI of the token endpoint provided by an OpenID Connect provider |
| test.oidc.client.user | string | x | test user name |
| test.oidc.client.password | string | x | test user credentials |
| test.target.route | string | x | target route URL to the application under test (just scheme + hostname + port without path) |
| test.target.readinessProbe.skip | bool |  | true, if application should not be checked for readiness; otherwise false (default: false) |
| test.target.readinessProbe.path | string |   | path of the readiness probe endpoint (default: /api/v1/probes/readiness) |
| test.target.readinessProbe.initialDelaySeconds | int |    | number of seconds to wait before checking readiness probe (default: 10) |
| test.target.readinessProbe.failureThreshold | int |    | number of retries before an application is assumed to be unhealthy (default: 3) |
| test.target.readinessProbe.periodSeconds | int |   | number of seconds to wait between retries (default: 10) |
| test.target.readinessProbe.timeoutSeconds | int |    | number of seconds a readiness check may last (default: 1) |

* __OR__ expects system properties with mentioned property names
* __OR__ expects environment variable with matching names: property names are matched to environment names by converting everything to uppercase and replacing dots with underscores (i.e. test.oidc.client.clientId becomes TEST_OIDC_CLIENT_CLIENTID).

#### Managing sensitive properties

Since version `2.0.0` properties files with sensitive values (like passwords or other secrets) can be kept in
so-called user properties. These user properties are stored in a special properties file which can be located
at different places in your local file system:

* in a user properties file named `test-config.properties` in a subdirectory `.cnj` of your local user home directory: `${HOME}/.cnj/test-config.properties`   
* in a user properties file with any name at any location referenced by environment variable `CNJ_USER_PROPERTIES_CONFIG`

If both files exist, only the user properties files referenced by environment variable `CNJ_USER_PROPERTIES_CONFIG`
is used.

Properties defined in user properties files will be merged with properties located in your project folders. Any
property defined in a user properties file will override a property with the same name provided in a regular
properties file.

### HOW-TO use test fixture RestAssuredSystemTestFixture

#### Step 0: Setup your OpenID Connect Provider (like KeyCloak)

* Make sure your test client has been added as a client to your OpenID Connect provider. 
You should own a client ID and a client credential representing your test client. 
Otherwise your test client will not be able to login!

* Make sure you added a test user with appropriate groups and roles to your OpenID connect provider. 
You should own the user name and credential of your test user.
Otherwise your test client will not be able to login or your test will fail due to missing roles.

#### Step 1: Add required Maven dependencies

Add the following dependency to your POM file:

``` 
<!-- common test support -->
<dependency>
    <groupId>group.msg.at.cloud.common</groupId>
    <artifactId>cnj-common-test-core</artifactId>
    <version>${REPLACE_WITH_CURRENT_VERSION}</version>
    <scope>test</scope>
</dependency>
```

#### Step 2: Add test fixture configuration to your test sources

Add a properties file named __test-config.properties__ to your __/src/test/resources/META-INF__ folder:

``` 
test.oidc.client.clientId=<your-test-client-id>
test.oidc.client.clientSecret=<your-test-client-credential>
test.oidc.client.accessTokenUri=<your-openid-connect-provider-token-endpoint>
test.oidc.client.user=<your-test-user-name>
test.oidc.client.password=<your-test-user-password>
```

#### Step 3: Add test fixture to your system test class

Add a static member to your test class holding a reference to the test fixture. 
Make sure you call __RestAssuredSystemTestFixture#onBefore()__ before running all tests and 
__RestAssuredSystemTestFixture#onAfter()__ after running all tests.

Here's an example of a proper RestAssuredSystemTestFixture setup: 

```java 
public class HelloResourceSystemTest {

    private static final RestAssuredSystemTestFixture fixture = new RestAssuredSystemTestFixture();

    @BeforeAll
    public static void onBeforeAll() {
        fixture.onBefore();
    }

    @AfterAll
    public static void onAfterAll() {
        fixture.onAfter();
    }
    
    /* ... */
}
```

#### Step 4: Pass tokens provided by the test fixture with all your requests

Don't forget to pass at least the access token obtained during __RestAssuredSystemTestFixture#onBefore()__ with all your
requests by invoking __auth().preemptive().oauth2(fixture.getAccessToken())__ on RestAssured:

```java 
@Test
public void getWelcomeMessageWithTokenMustReturn200() {
    given().auth().preemptive().oauth2(fixture.getAccessToken())
            .get("api/v1/hello")
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("text", response -> equalTo("Dear \"cnj-tester\", welcome to a cloud native java application protected by OpenID Connect"));
}
```


