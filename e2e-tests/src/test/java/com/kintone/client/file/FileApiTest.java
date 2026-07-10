package com.kintone.client.file;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.helper.App;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.field.FieldProperty;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** FileClientのテスト */
public class FileApiTest extends ApiTestBase {

    private static final String FILE_FIELD_CODE = "添付ファイル";

    private KintoneClient client;
    private App app;

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
    }

    @AfterEach
    public void cleanupRecords() {
        if (app != null) {
            app.deleteAllRecords();
        }
    }

    @Test
    public void uploadFile_downloadFile() {
        FieldProperty file = app.field(FILE_FIELD_CODE);

        UploadFileResponseBody resp1;
        try (ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes())) {
            UploadFileRequest req1 = new UploadFileRequest();
            req1.setFilename("test.txt");
            req1.setContentType("text/plain");
            req1.setContent(in);
            resp1 = client.file().uploadFile(req1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long recordId = app.addRecord(file, resp1.getFileKey());
        FileBody value = app.getRecord(recordId).getFileFieldValue(FILE_FIELD_CODE).get(0);
        assertThat(value.getName()).isEqualTo("test.txt");
        assertThat(value.getContentType()).isEqualTo("text/plain");
        assertThat(value.getSize()).isEqualTo(4);

        DownloadFileRequest req2 = new DownloadFileRequest();
        req2.setFileKey(value.getFileKey());
        DownloadFileResponseBody resp2 = client.file().downloadFile(req2);

        assertThat(resp2.getContentType()).startsWith("text/plain"); // charsetが付属するため
        assertThat(resp2.getContentLength()).isEqualTo(4);
        String body;
        try (InputStream in = resp2.getContent()) {
            byte[] buffer = new byte[32];
            int size = in.read(buffer);
            body = new String(buffer, 0, size, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat(body).isEqualTo("test");
    }
}
