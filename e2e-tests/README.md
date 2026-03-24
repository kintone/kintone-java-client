# E2E Tests

End-to-end tests for kintone-java-client that run against a real kintone environment.

## Prerequisites

- Java 8 or later
- A kintone environment for testing

## Environment Variables

Set the following environment variables before running tests:

| Variable | Description |
|----------|-------------|
| `KINTONE_BASE_URL` | Base URL of your kintone environment |
| `KINTONE_DEFAULT_USER` | Default user login name |
| `KINTONE_DEFAULT_PASSWORD` | Default user password |
| `KINTONE_TEST_USER` | Test user login name |
| `KINTONE_TEST_PASSWORD` | Test user password |
| `KINTONE_SPACE_ID` | Space ID for testing |
| `KINTONE_GUEST_SPACE_ID` | Guest space ID for testing |
| `KINTONE_BASIC_USER` | Basic auth username (if enabled) |
| `KINTONE_BASIC_PASS` | Basic auth password (if enabled) |

## Running Tests

From the project root directory:

```bash
# Run all E2E tests
./gradlew :e2e-tests:test

# Run a specific test class
./gradlew :e2e-tests:test --tests "*.RecordApiTest"

# Run with environment variables inline
KINTONE_BASE_URL=https://example.cybozu.com \
KINTONE_DEFAULT_USER=user \
KINTONE_DEFAULT_PASSWORD=pass \
./gradlew :e2e-tests:test
```

## CI/CD

Tests are automatically run via GitHub Actions:

- On push to `master` branch
- On pull requests to `master` branch
- Manually via workflow dispatch (with optional specific test class)

See `.github/workflows/e2e.yml` for the workflow configuration.
