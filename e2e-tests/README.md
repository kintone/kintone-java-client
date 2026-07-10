# kintone-java-client E2E Tests

End-to-end tests that send actual requests to a kintone environment using kintone-java-client.

## Prerequisites

These tests use **pre-created kintone resources** (apps, spaces, users) instead of creating new ones for each test run. This approach:
- Prevents accumulation of test apps that cannot be deleted via API
- Enables consistent test execution in CI

You need to set up the following in your kintone environment before running tests:
- Test apps with required fields
- Spaces (single-thread, multi-thread, guest)
- Test users and groups

See `.env.example` for the full list of required resources.

## How to Run

### Setup

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` with your kintone environment settings

### Running Tests

From the project root directory:

```bash
# Run all E2E tests
./gradlew :e2e-tests:test

# Run specific test class
./gradlew :e2e-tests:test --tests "RecordApiTest"

# Run specific test method
./gradlew :e2e-tests:test --tests "RecordApiTest.addRecord"
```

### Running from IntelliJ

- Run test classes or methods as usual
- Ensure environment variables are configured (use EnvFile plugin or run configuration)

## Environment Variables

### Required

| Variable | Description |
|----------|-------------|
| `KINTONE_BASE_URL` | URL of the kintone environment (e.g., `https://example.cybozu.com`) |
| `KINTONE_DEFAULT_USER` | Login name for the default user |
| `KINTONE_DEFAULT_PASSWORD` | Password for the default user |
| `KINTONE_TEST_APP_ID` | Pre-created app ID for general tests |
| `KINTONE_SPACE_ID` | Single-thread space ID |

See `.env.example` for the complete list.

### Optional (Basic Auth / Client Cert)

| Variable | Description |
|----------|-------------|
| `KINTONE_BASIC_USER` | Basic authentication username |
| `KINTONE_BASIC_PASS` | Basic authentication password |
| `KINTONE_CLIENT_CERT` | Path to client certificate file |
| `KINTONE_CLIENT_CERT_PASS` | Client certificate password |

## Proxy Tests

Proxy configuration is supported via the `KINTONE_PROXY_URL` environment variable.

A Squid container for testing is available in `docker/proxy/`.

```bash
# Build and run the proxy server
docker build -t test-proxy docker/proxy
docker run --rm -p3128:3128 test-proxy

# (In another terminal) Run tests via proxy
KINTONE_PROXY_URL=http://localhost:3128 ./gradlew :e2e-tests:test
```

### Authenticated Proxy

```bash
# Start proxy with Basic authentication
docker run --rm -p3128:3128 -e proxy_auth=basic \
  -e proxy_user=user1 -e proxy_pass=password1 test-proxy

# Run tests with proxy credentials
KINTONE_PROXY_URL=http://localhost:3128 \
  KINTONE_PROXY_USER=user1 \
  KINTONE_PROXY_PASS=password1 \
  ./gradlew :e2e-tests:test
```

## Package Structure

```
com.kintone.client
  + app/       AppClient API tests (settings, ACL, fields, views, etc.)
  + bulk/      bulkRequest API tests
  + file/      FileClient API tests (upload/download)
  + plugin/    PluginClient API tests
  + record/    RecordClient API tests (CRUD, comments, cursor)
  + schema/    SchemaClient API tests
  + space/     SpaceClient API tests
  + helper/    Test setup helpers (App, Space, Fields, builders)
```

Tests focus on APIs that take `*Request` objects and return `*ResponseBody` objects. Convenience method variations are covered by unit tests.

