package com.kintone.client;

import com.kintone.client.api.KintoneResponse;
import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.exception.KintoneApiRuntimeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;

/**
* A {@link KintoneClient} is the client for operating Kintone APIs.
*
* <p>To obtain an instance of this class, must use {@link KintoneClientBuilder}.
*
* <pre>{@code
* KintoneClient client = KintoneClientBuilder.create("https://{your Kintone domain}.cybozu.com")
*                                            .authByPassword(username, password)
*                                            .build();
* }</pre>
*
* <p>This class provides Kintone APIs via sub-components: {@link RecordClient}, {@link AppClient},
* and so on. For example, if you want to get a record of an App, use the {@link #record()} method
* of this class to get the record client.
*
* <pre>{@code
* Record record = client.record().getRecord(appId, recordId);
* }</pre>
*
* <p>See each sub-component to find the documents of specific APIs.
*
* <p><b>Note:</b>
*
* <p>The instance of this class holds an HTTP connection inside. Call {@link #close()} to releases
* any resources associated with the connection when the instance is no longer used.
*/
public class KintoneClient implements AutoCloseable {
    private final InternalClient client;
    private final AppClient appClient;
    private final RecordClient recordClient;
    private final SpaceClient spaceClient;
    private final FileClient fileClient;
    private final SchemaClient schemaClient;

    private final List<ResponseHandler> handlers = new ArrayList<>();

    KintoneClient(InternalClient client) {
        handlers.add(new DefaultErrorHandler());

        this.client = client;
        appClient = new AppClient(client, handlers);
        recordClient = new RecordClient(client, handlers);
        spaceClient = new SpaceClient(client, handlers);
        fileClient = new FileClient(client, handlers);
        schemaClient = new SchemaClient(client, handlers);
    }

    /**
    * Get the client for app APIs.
    *
    * @return the app client
    */
    public AppClient app() {
        return appClient;
    }

    /**
    * Get the client for record APIs.
    *
    * @return the record client
    */
    public RecordClient record() {
        return recordClient;
    }

    /**
    * Get the client for space APIs.
    *
    * @return the space client
    */
    public SpaceClient space() {
        return spaceClient;
    }

    /**
    * Get the client for file APIs.
    *
    * @return the file client
    */
    public FileClient file() {
        return fileClient;
    }

    /**
    * Get the client for schema APIs.
    *
    * @return the schema client
    */
    public SchemaClient schema() {
        return schemaClient;
    }

    /**
    * Runs multiple API requests sequentially to multiple Apps.
    *
    * @param request the request parameters. See {@link BulkRequestsRequest}
    * @return the response data. See {@link BulkRequestsResponseBody}
    */
    public BulkRequestsResponseBody bulkRequests(BulkRequestsRequest request) {
        return client.bulkRequest(request, handlers);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private static class DefaultErrorHandler implements ResponseHandler {
        @Override
        public void handle(KintoneResponse<?> response) {
            if (!isSuccess(response.getStatusCode())) {
                throw new KintoneApiRuntimeException(
                        response.getStatusCode(), response.getHeaders(), response.getErrorBody());
            }
        }

        private boolean isSuccess(int statusCode) {
            return HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
        }
    }
}
