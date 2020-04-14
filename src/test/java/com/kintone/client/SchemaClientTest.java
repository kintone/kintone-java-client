package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.api.schema.GetApiListResponseBody;
import com.kintone.client.api.schema.GetApiSchemaResponseBody;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class SchemaClientTest {

    private InternalClientMock mockClient = new InternalClientMock();
    private SchemaClient sut = new SchemaClient(mockClient, Collections.emptyList());

    @Test
    public void getApiList() {
        GetApiListResponseBody resp = new GetApiListResponseBody("", Collections.emptyMap());
        mockClient.setResponseBody(resp);

        assertThat(sut.getApiList()).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_API_LIST);
        assertThat(mockClient.getLastBody()).isNull();
    }

    @Test
    public void getApiSchema_KintoneApi() {
        GetApiSchemaResponseBody resp =
                new GetApiSchemaResponseBody("", "", "", "", null, null, Collections.emptyMap());
        mockClient.setResponseBody(resp);

        assertThat(sut.getApiSchema(KintoneApi.GET_RECORD)).usingRecursiveComparison().isEqualTo(resp);
        assertThat(mockClient.getLastMethod()).isEqualTo(KintoneHttpMethod.GET);
        assertThat(mockClient.getLastPath()).isEqualTo("/k/v1/apis/record/get.json");
        assertThat(mockClient.getLastBody()).isNull();
    }

    @Test
    public void getApiSchema_String() {
        GetApiSchemaResponseBody resp =
                new GetApiSchemaResponseBody("", "", "", "", null, null, Collections.emptyMap());
        mockClient.setResponseBody(resp);

        assertThat(sut.getApiSchema("apis/record/get.json")).usingRecursiveComparison().isEqualTo(resp);
        assertThat(mockClient.getLastMethod()).isEqualTo(KintoneHttpMethod.GET);
        assertThat(mockClient.getLastPath()).isEqualTo("/k/v1/apis/record/get.json");
        assertThat(mockClient.getLastBody()).isNull();
    }

    @Test
    public void getApiSchema_String_invalidLink() {
        assertThat(sut.getApiSchema("apis/unknown/get.json")).isNull();
        assertThat(mockClient.getLastMethod()).isNull();
    }
}
