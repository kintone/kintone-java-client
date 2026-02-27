package com.kintone.client.schema;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.schema.GetApiListResponseBody;
import com.kintone.client.api.schema.GetApiSchemaResponseBody;
import com.kintone.client.model.schema.ApiSchemaLink;
import com.kintone.client.model.schema.RequestSchema;
import com.kintone.client.model.schema.ResponseSchema;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** SchemaClientのテスト */
public class SchemaApiTest extends ApiTestBase {
    @Test
    public void getApiList() {
        KintoneClient client = setupDefaultClient();
        GetApiListResponseBody resp = client.schema().getApiList();

        String baseUrl = getBaseURL() + "/k/v1/";
        assertThat(resp.getBaseUrl()).isEqualTo(baseUrl);

        Map<String, ApiSchemaLink> apis = resp.getApis();
        assertThat(apis).hasSizeGreaterThan(1);
        // すべて確認するのは大変なので1件だけ確認
        ApiSchemaLink schema = apis.get("app/get");
        assertThat(schema.getLink()).isEqualTo("apis/app/get.json");
    }

    @Test
    public void getApiSchema() {
        // すべて確認するのは大変なので比較的簡単なPOST records/cursor.jsonを確認
        KintoneClient client = setupDefaultClient();
        GetApiSchemaResponseBody resp = client.schema().getApiSchema("apis/records/cursor/post.json");

        String baseUrl = getBaseURL() + "/k/v1/";
        assertThat(resp.getBaseUrl()).isEqualTo(baseUrl);
        assertThat(resp.getHttpMethod()).isEqualTo("POST");
        assertThat(resp.getPath()).isEqualTo("records/cursor.json");
        assertThat(resp.getId()).isEqualTo("records/cursor/post");

        RequestSchema requestSchema = resp.getRequest();
        assertThat(requestSchema.getType()).isEqualTo("object");
        assertThat(requestSchema.getRequired()).containsExactly("app");
        assertThat(requestSchema.getProperties()).containsOnlyKeys("app", "size", "query", "fields");

        ResponseSchema responseSchema = resp.getResponse();
        assertThat(responseSchema.getType()).isEqualTo("object");
        assertThat(responseSchema.getProperties()).containsOnlyKeys("id", "totalCount");

        assertThat(resp.getSchemas()).isEmpty();
    }
}
