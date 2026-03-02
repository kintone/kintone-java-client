#!/bin/bash
#
# ../kintone-java-clientをビルドし、作成されたjarをE2Eテストで使う用にコピーする
#
# * 実行例
# $ ./bin/update-client-jar.sh

BASE_DIR=$(cd $(dirname "$0")/../; pwd)

echo "### ../kintone-java-client をビルド ###"
cd "$BASE_DIR/../kintone-java-client"
./gradlew clean jar

echo "### ビルド結果をコピー ###"
cd "$BASE_DIR"
cp ../kintone-java-client/build/libs/kintone-java-client-*.jar kintone-java-client.jar
