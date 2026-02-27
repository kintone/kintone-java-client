# kintone-java-client E2E Tests

End-to-end tests that send actual requests to a kintone environment using kintone-java-client.

## How to Run

Tests are JUnit tests and can be run from IntelliJ or via Gradle command.

### Running from IntelliJ

- Run test classes or methods as usual
- Set the `KINTONE_BASE_URL` environment variable to specify the target kintone environment

### Using Gradle

From the project root directory:

```bash
# Run all E2E tests
./gradlew :e2e-tests:test

# Run specific tests only
./gradlew :e2e-tests:test --tests "*.SmokeTest"
```

### Environment Variables

Copy `.env.example` to `.env` and set the required values.

| Variable | Description |
|----------|-------------|
| `KINTONE_BASE_URL` | URL of the kintone environment to test against |
| `KINTONE_DEFAULT_USER` | Login name for the default user |
| `KINTONE_DEFAULT_PASSWORD` | Password for the default user |
| `KINTONE_TEST_USER` | Login name for the test user |
| `KINTONE_TEST_PASSWORD` | Password for the test user |
| `KINTONE_SPACE_ID`, etc. | See `.env.example` for details |
| `KINTONE_TEST_APP_ID` | Pre-created app for E2E tests (see below) |

### Basic Authentication and Client Certificates

If the domain has Basic authentication or client certificates enabled, set the following environment variables:

| Variable | Description |
|----------|-------------|
| `KINTONE_BASIC_USER` | Basic authentication username |
| `KINTONE_BASIC_PASS` | Basic authentication password |
| `KINTONE_CLIENT_CERT` | Path to client certificate file |
| `KINTONE_CLIENT_CERT_PASS` | Client certificate password |

### Proxy Tests

Proxy configuration is supported via the `KINTONE_PROXY_URL` environment variable.

A Squid container for testing is available in `docker/proxy/`.

```bash
# Build and run the proxy server
$ docker build -t test-proxy docker/proxy
$ docker run --rm -p3128:3128 test-proxy

# (In another terminal) Run tests via proxy
$ KINTONE_PROXY_URL=http://localhost:3128 \
  KINTONE_BASE_URL=https://example.cybozu.com ./gradlew :e2e-tests:test
```

Stop the proxy server with `Ctrl+C`.

#### Authenticated Proxy Tests

To enable Basic authentication on the proxy, pass `-e proxy_auth=basic`, `-e proxy_user=<username>`, and `-e proxy_pass=<password>` to `docker run`.

```bash
# Start proxy with Basic authentication
$ docker run --rm -p3128:3128 -e proxy_auth=basic \
  -e proxy_user=user1 -e proxy_pass=password1 -it test-proxy

# Run tests with proxy credentials
$ KINTONE_PROXY_URL=http://localhost:3128 \
  KINTONE_PROXY_USER=user1 \
  KINTONE_PROXY_PASS=password1 \
  KINTONE_BASE_URL=https://example.cybozu.com ./gradlew :e2e-tests:test
```

Setting `proxy_auth=digest` enables Digest authentication. Since kintone-java-client only supports Basic authentication explicitly, Digest auth is used to verify that it *does not work*.

## Package Structure

Tests are organized into the following packages:

```
com.kintone.client
  + app:       Tests for AppClient APIs
  + bulk:      Tests for bulkRequest API
  + file:      Tests for FileClient APIs
  + record:    Tests for RecordClient APIs
  + schema:    Tests for SchemaClient APIs
  + space:     Tests for SpaceClient APIs
  + plugin:    Tests for PluginClient APIs
  + scenarios: Scenario tests combining multiple APIs
  + helper:    Helper classes for test setup (app/space operations)
```

### Client-specific Packages (app, record, etc.)

These packages contain tests for individual API operations.

Classes like `app.AppApiTest` and `record.RecordApiTest` are provided. As tests grow, they may be split into multiple files within each package.

Only APIs that take `*Request` objects (e.g., `AddRecordRequest`) and return `*ResponseBody` objects (e.g., `AddRecordResponseBody`) are tested here. Other variations (taking appId or recordId directly) are covered by unit tests for request construction.

