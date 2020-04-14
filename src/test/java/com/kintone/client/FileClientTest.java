package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.model.HttpResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class FileClientTest {

    private InternalClientMock mockClient = new InternalClientMock();
    private FileClient sut = new FileClient(mockClient, Collections.emptyList());

    @Test
    public void downloadFile_String() throws IOException {
        HttpResponse resp = new HttpResponseMock("test");
        mockClient.setResponseBody(new DownloadFileResponseBody(resp));

        try (InputStream in = sut.downloadFile("key")) {
            String result = new Scanner(in).useDelimiter("\\A").next();
            assertThat(result).isEqualTo("test");
        }
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DOWNLOAD_FILE);
        assertThat(mockClient.getLastBody()).isEqualTo(new DownloadFileRequest().setFileKey("key"));
    }

    @Test
    public void downloadFile_DownloadFileRequest() {
        mockClient.setResponseBody(new DownloadFileResponseBody(null));

        assertThat(sut.downloadFile(new DownloadFileRequest().setFileKey("key")))
                .isInstanceOf(DownloadFileResponseBody.class);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DOWNLOAD_FILE);
        assertThat(mockClient.getLastBody()).isEqualTo(new DownloadFileRequest().setFileKey("key"));
    }

    @Test
    public void uploadFile_File_String() throws IOException {
        mockClient.setResponseBody(new UploadFileResponseBody("key"));

        File tempFile = File.createTempFile("kintone", ".tmp");
        try {
            Files.write(tempFile.toPath(), "test".getBytes());

            assertThat(sut.uploadFile(tempFile.toPath(), "text/html")).isEqualTo("key");
            assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPLOAD_FILE);
            assertThat(mockClient.getLastBody()).isInstanceOf(UploadFileRequest.class);
            UploadFileRequest request = (UploadFileRequest) mockClient.getLastBody();
            assertThat(request.getFilename()).isEqualTo(tempFile.getName());
            assertThat(request.getContentType()).isEqualTo("text/html");
        } finally {
            tempFile.delete();
        }
    }

    @Test
    public void uploadFile_UploadFileRequest() {
        mockClient.setResponseBody(new UploadFileResponseBody("key"));

        UploadFileRequest req =
                new UploadFileRequest().setFilename("name").setContentType("type").setContent(null);
        assertThat(sut.uploadFile(req)).isInstanceOf(UploadFileResponseBody.class);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPLOAD_FILE);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UploadFileRequest().setFilename("name").setContentType("type").setContent(null));
    }

    private static class HttpResponseMock implements HttpResponse {
        private final String content;

        private HttpResponseMock(String content) {
            this.content = content;
        }

        @Override
        public Long getContentLength() {
            return (long) content.length();
        }

        @Override
        public String getContentType() {
            return "text/plain";
        }

        @Override
        public InputStream getContent() throws IOException {
            return new ByteArrayInputStream(content.getBytes());
        }

        @Override
        public void close() throws IOException {}
    }
}
