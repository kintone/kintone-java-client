package com.kintone.client;

import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/** A client that operates file APIs. */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FileClient {

    private final InternalClient client;
    private final List<ResponseHandler> handlers;

    /**
     * Downloads files from an attachment field in an app.
     *
     * @param fileKey the value that is set on the Attachment field in the response data returned when
     *     using the Get Record API
     * @return the content data stream
     * @throws IOException if an I/O error occurs
     */
    public InputStream downloadFile(String fileKey) throws IOException {
        DownloadFileRequest req = new DownloadFileRequest();
        req.setFileKey(fileKey);
        return downloadFile(req).getContent();
    }

    /**
     * Downloads files from an attachment field in an app.
     *
     * @param request the request parameters. See {@link DownloadFileRequest}
     * @return the response data. See {@link DownloadFileResponseBody}
     */
    public DownloadFileResponseBody downloadFile(DownloadFileRequest request) {
        return client.download(request, handlers);
    }

    /**
     * Uploads a file to Kintone.
     *
     * @param path the file to upload
     * @param contentType the Content Type of the file.
     * @return the fileKey of the uploaded file
     * @throws IOException if an I/O error occurs
     */
    public String uploadFile(Path path, String contentType) throws IOException {
        UploadFileRequest req = new UploadFileRequest();
        req.setContentType(contentType);
        req.setFilename(path.getFileName().toString());
        try (InputStream content = Files.newInputStream(path)) {
            req.setContent(content);
            return uploadFile(req).getFileKey();
        }
    }

    /**
     * Uploads a file to Kintone.
     *
     * @param request the request parameters. See {@link UploadFileRequest}
     * @return the response data. See {@link UploadFileResponseBody}
     */
    public UploadFileResponseBody uploadFile(UploadFileRequest request) {
        return client
                .upload(request.getFilename(), request.getContentType(), request.getContent(), handlers)
                .getBody();
    }
}
