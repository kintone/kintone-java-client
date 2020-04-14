package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class KintoneClientTest {
    private InternalClientMock mockClient = new InternalClientMock();
    private KintoneClient sut = new KintoneClient(mockClient);

    @Test
    public void bulkRequests_BulkRequestsRequest() {
        BulkRequestsRequest req = new BulkRequestsRequest();
        BulkRequestsResponseBody resp = new BulkRequestsResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.bulkRequests(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.BULK_REQUESTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }
}
