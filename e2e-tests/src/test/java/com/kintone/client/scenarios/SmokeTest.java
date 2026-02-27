package com.kintone.client.scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.common.*;
import com.kintone.client.api.record.AddRecordRequest;
import com.kintone.client.api.schema.GetApiListResponseBody;
import com.kintone.client.api.space.GetSpaceResponseBody;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.helper.Space;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.record.FileFieldValue;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * InternalClientImplの実装が違う部分を一通り通り、クライアントも一通り触る
 *
 * <ul>
 *   <li>アプリを作成してレコード取得
 *   <li>bulkRequest
 *   <li>ファイルアップロード、ダウンロード
 *   <li>スペース系API
 *   <li>APIスキーマ取得
 * </ul>
 *
 * このテストはプロキシ認証との組み合わせでの動作確認に使うものだが、 HTTP Keep
 * Aliveでコネクションを使いまわすと、プロキシ認証をリクエストごとにやり直さず確認漏れになる可能性があるので、 毎回clientを作り直している
 */
public class SmokeTest extends ApiTestBase {

    @Test
    public void test() throws InterruptedException, IOException {
        final String testName = setupTestName();
        // スペース取得
        Space space = Space.singleThread(this);
        try (KintoneClient client = setupDefaultClient()) {
            GetSpaceResponseBody resp1 = client.space().getSpace(space.id());
            assertThat(resp1.getName()).isNotEmpty();
        }

        // アプリ作成
        App app;
        FieldProperty file = Fields.file();
        FieldProperty text = Fields.text();
        try (KintoneClient client = setupDefaultClient()) {
            app = App.create(client, testName, space.id(), space.getDefaultThread());
            app.addFields(file, text).deploy();
        }

        // ファイルアップロード
        final String content1 = "aaa bbb ccc";
        final String content2 = "あああ　いいい　ううう";
        String fileKey1;
        String fileKey2;
        try (KintoneClient client = setupDefaultClient()) {
            fileKey1 = uploadText(client, "test.txt", content1);
            fileKey2 = uploadText(client, "日本語.txt", content2);
        }

        // 　bulkRequestでレコード追加
        BulkRequestsRequest req = new BulkRequestsRequest();
        req.registerAddRecord(setupAddRecordRequest(app.id(), text, "あいうえお", file, fileKey1));
        req.registerAddRecord(setupAddRecordRequest(app.id(), text, "abc xyz", file, fileKey2));
        try (KintoneClient client = setupDefaultClient()) {
            BulkRequestsResponseBody resp2 = client.bulkRequests(req);
            assertThat(resp2.getResults()).hasSize(2);
        }

        // レコード取得とダウンロード
        List<Record> records;
        try (KintoneClient client = setupDefaultClient()) {
            records = client.record().getRecords(app.id(), "order by $id asc");
            assertThat(records).hasSize(2);
            assertThat(records.get(0).getSingleLineTextFieldValue(text.getCode())).isEqualTo("あいうえお");
            assertThat(records.get(1).getSingleLineTextFieldValue(text.getCode())).isEqualTo("abc xyz");
        }
        try (KintoneClient client = setupDefaultClient()) {
            downloadTest(
                    client, records.get(0).getFileFieldValue(file.getCode()).get(0).getFileKey(), content1);
            downloadTest(
                    client, records.get(1).getFileFieldValue(file.getCode()).get(0).getFileKey(), content2);
        }

        // APIスキーマ一覧を取得
        try (KintoneClient client = setupDefaultClient()) {
            GetApiListResponseBody resp = client.schema().getApiList();
            assertThat(resp.getBaseUrl()).isEqualTo(getBaseURL() + "/k/v1/");
            assertThat(resp.getApis().get("app/get").getLink()).isEqualTo("apis/app/get.json");
        }
    }

    private String setupTestName() {
        final TestSettings settings = getSettings();
        StringBuilder sb = new StringBuilder("SmokeTest");
        if (!settings.getBasicAuthUser().isEmpty()) {
            sb.append(" BasicAuth");
        }
        if (!settings.getClientCertPath().isEmpty()) {
            sb.append(" ClientCert");
        }
        if (!settings.getProxyUrl().isEmpty()) {
            if (settings.getProxyUser().isEmpty()) {
                sb.append(" Proxy");
            } else {
                sb.append(" ProxyAuth");
            }
        }
        return sb.toString();
    }

    private AddRecordRequest setupAddRecordRequest(
            long appId, FieldProperty text, String value, FieldProperty file, String key) {
        FileBody fileBody = new FileBody().setFileKey(key);
        Record record = new Record();
        record.putField(text.getCode(), new SingleLineTextFieldValue(value));
        record.putField(file.getCode(), new FileFieldValue(fileBody));
        return new AddRecordRequest().setApp(appId).setRecord(record);
    }

    private String uploadText(KintoneClient client, String fileName, String content) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes())) {
            UploadFileRequest req = new UploadFileRequest();
            req.setFilename(fileName);
            req.setContentType("text/plain");
            req.setContent(in);
            return client.file().uploadFile(req).getFileKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadTest(KintoneClient client, String key, String expected) {
        try (InputStream in = client.file().downloadFile(key)) {
            byte[] buffer = new byte[64];
            int size = in.read(buffer);
            String body = new String(buffer, 0, size, StandardCharsets.UTF_8);
            assertThat(body).isEqualTo(expected);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
