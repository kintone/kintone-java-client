package com.kintone.client.file;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.field.FieldProperty;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** FileClientのテスト */
public class FileApiTest extends ApiTestBase {
    @Test
    public void uploadFile_downloadFile() {
        KintoneClient client = setupDefaultClient();
        FieldProperty file = Fields.file();
        App app = App.create(client, "uploadFile_downloadFile");
        app.addFields(file).deploy();

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
        FileBody value = app.getRecord(recordId).getFileFieldValue(file.getCode()).get(0);
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
