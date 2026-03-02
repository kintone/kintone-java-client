#!/bin/bash
#
# kintone-java-clientをビルドしてE2Eテストを実行する
#
# * 実行時に指定する環境変数
# 必須:
#   KINTONE_BASE_URL - テストに使うkintoneのURL
#   KINTONE_DEFAULT_USER / KINTONE_DEFAULT_PASSWORD - デフォルトユーザー認証情報
#   KINTONE_TEST_USER / KINTONE_TEST_PASSWORD - テストユーザー認証情報
#   KINTONE_SPACE_ID 等 - 詳細は .env.example を参照
#
# オプション:
#   KINTONE_PROXY_URL - プロキシサーバーのURL
#   KINTONE_BASIC_USER / KINTONE_BASIC_PASS - Basic認証
#   SKIP_CLIENT_BUILD - "true"を設定するとクライアントのビルドをスキップ
#
# * 実行例
# - .envファイルを使ってテスト実行
# $ source .env && ./bin/run-test.sh
#
# - 特定のテストのみ実行
# $ source .env && ./bin/run-test.sh --tests SmokeTest
#
# - クライアントビルドをスキップ（jarがすでにある場合）
# $ SKIP_CLIENT_BUILD=true ./bin/run-test.sh

set -e

KINTONE_BASE_URL=${KINTONE_BASE_URL:-http://localhost}
KINTONE_BASE_URL=$(echo "$KINTONE_BASE_URL" | sed -e 's|/\+$||')
KINTONE_PROXY_URL=${KINTONE_PROXY_URL:-""}
BASE_DIR=$(cd "$(dirname "$0")"/../; pwd)
CLIENT_DIR=$(cd "$BASE_DIR"/.. ; pwd)

echo "=== E2E Test Configuration ==="
echo "KINTONE_BASE_URL=$KINTONE_BASE_URL"
echo "KINTONE_DEFAULT_USER=$KINTONE_DEFAULT_USER"
echo "KINTONE_TEST_USER=$KINTONE_TEST_USER"
echo "BASE_DIR=$BASE_DIR"
echo "CLIENT_DIR=$CLIENT_DIR"
echo "SKIP_CLIENT_BUILD=${SKIP_CLIENT_BUILD:-false}"
echo

cd "$BASE_DIR"

# テスト結果を消す
rm -fr "build/test-results/test"

# kintone-java-clientをビルド（スキップ指定がない場合）
if [ "${SKIP_CLIENT_BUILD}" != "true" ]; then
  echo "=== Building kintone-java-client ==="
  cd "$CLIENT_DIR"
  ./gradlew ${GRADLE_OPTS} clean jar
  echo

  echo "=== Copying built jar ==="
  cd "$BASE_DIR"
  rm -f kintone-java-client.jar
  cp "$CLIENT_DIR"/build/libs/kintone-java-client-*.jar kintone-java-client.jar
  ls -la kintone-java-client.jar
  echo
fi

# 環境変数をそのまま継承してgradleを実行
echo "=== Running E2E tests ==="
./gradlew ${GRADLE_OPTS} clean test "$@"
