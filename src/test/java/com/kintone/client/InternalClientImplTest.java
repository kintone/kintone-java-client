package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Charsets;
import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.api.record.AddRecordRequest;
import com.kintone.client.api.record.AddRecordResponseBody;
import com.kintone.client.api.record.DeleteRecordsRequest;
import com.kintone.client.api.record.DeleteRecordsResponseBody;
import com.kintone.client.api.record.GetRecordRequest;
import com.kintone.client.api.record.GetRecordResponseBody;
import com.kintone.client.api.record.UpdateRecordRequest;
import com.kintone.client.api.record.UpdateRecordResponseBody;
import com.kintone.client.model.Auth;
import com.kintone.client.model.BasicAuth;
import com.kintone.client.model.User;
import com.kintone.client.model.record.CreatedTimeFieldValue;
import com.kintone.client.model.record.CreatorFieldValue;
import com.kintone.client.model.record.ModifierFieldValue;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RecordNumberFieldValue;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import com.kintone.client.model.record.UpdatedTimeFieldValue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.mockserver.verify.VerificationTimes;

public class InternalClientImplTest {
    private static final int timeout = 60 * 1000;
    private static final String userName = "user";
    private static final String password = "password";
    private static final String passwordAuth = "dXNlcjpwYXNzd29yZA==";
    private ClientAndServer server;

    @BeforeEach
    public void setup() {
        server = ClientAndServer.startClientAndServer();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    private String loadResource(String name) {
        try (InputStream in = getClass().getResourceAsStream(name)) {
            Scanner s = new Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private InternalClientImpl setupClient() {
        String baseUrl = "http://localhost:" + server.getLocalPort();
        Auth auth = Auth.byPassword(userName, password);
        return new InternalClientImpl(
                baseUrl, auth, null, null, null, null, null, "", timeout, timeout, timeout);
    }

    private InternalClientImpl setupClient(long guestSpaceId) {
        String baseUrl = "http://localhost:" + server.getLocalPort();
        Auth auth = Auth.byPassword(userName, password);
        return new InternalClientImpl(
                baseUrl, auth, null, null, null, null, guestSpaceId, "", timeout, timeout, timeout);
    }

    private Record setupExpectedRecord() {
        ZonedDateTime time = ZonedDateTime.parse("2020-01-01T00:00:00Z");
        User user = new User("User", "user");
        return new Record(1L, 1L)
                .putField("record_number", new RecordNumberFieldValue("1"))
                .putField("created_datetime", new CreatedTimeFieldValue(time))
                .putField("created_by", new CreatorFieldValue(user))
                .putField("updated_datetime", new UpdatedTimeFieldValue(time))
                .putField("updated_by", new ModifierFieldValue(user))
                .putField("text", new SingleLineTextFieldValue("test value"));
    }

    private Map<String, String> getMultipartHeaders(String multipartBody) {
        Map<String, String> headers = new HashMap<>();
        String[] lines = multipartBody.split("\r\n");
        for (int i = 1; i < lines.length - 1 && !lines[i].isEmpty(); i++) {
            String[] parts = lines[i].split(":", 2);
            headers.put(parts[0].trim(), parts[1].trim());
        }
        return headers;
    }

    private String getMultipartContent(String multipartBody) {
        List<String> lines = Arrays.asList(multipartBody.split("\r\n"));
        int endOfHeaders = lines.indexOf("");
        return String.join("", lines.subList(endOfHeaders + 1, lines.size() - 1));
    }

    @Test
    public void call_GET() {
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp = sut.call(KintoneApi.GET_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        Record expectedRecord = setupExpectedRecord();
        assertThat(resp.getRecord()).usingRecursiveComparison().isEqualTo(expectedRecord);
    }

    @Test
    public void call_POST() {
        String request = loadResource("InternalClientImplTest_call_POST_request.json");
        String response = loadResource("InternalClientImplTest_call_POST_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        Record record = new Record().putField("text", new SingleLineTextFieldValue("test value"));
        AddRecordRequest req = new AddRecordRequest().setApp(1L).setRecord(record);
        AddRecordResponseBody resp = sut.call(KintoneApi.ADD_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getId()).isEqualTo(1);
        assertThat(resp.getRevision()).isEqualTo(1);
    }

    @Test
    public void call_PUT() {
        String request = loadResource("InternalClientImplTest_call_PUT_request.json");
        String response = loadResource("InternalClientImplTest_call_PUT_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("PUT")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        Record record = new Record().putField("text", new SingleLineTextFieldValue("test value"));
        UpdateRecordRequest req = new UpdateRecordRequest().setApp(1L).setId(1L).setRecord(record);
        UpdateRecordResponseBody resp =
                sut.call(KintoneApi.UPDATE_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getRevision()).isEqualTo(2);
    }

    @Test
    public void call_DELETE() {
        String request = loadResource("InternalClientImplTest_call_DELETE_request.json");
        String response = loadResource("InternalClientImplTest_call_DELETE_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/records.json")
                        .withMethod("DELETE")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        DeleteRecordsRequest req =
                new DeleteRecordsRequest().setApp(1L).setIds(Arrays.asList(1L, 2L, 3L));
        DeleteRecordsResponseBody resp =
                sut.call(KintoneApi.DELETE_RECORDS, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp).usingRecursiveComparison().isEqualTo(new DeleteRecordsResponseBody());
    }

    @Test
    public void call_error() {
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(404)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp =
                sut.call(
                        KintoneApi.GET_RECORD,
                        req,
                        Collections.singletonList(r -> assertThat(r.getStatusCode()).isEqualTo(404)));

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp).isNull();
    }

    @Test
    public void bulkRequest() {
        String request = loadResource("InternalClientImplTest_bulkRequest_request.json");
        String response = loadResource("InternalClientImplTest_bulkRequest_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/bulkRequest.json")
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        Record record1 = new Record().putField("text", new SingleLineTextFieldValue("test value 1"));
        Record record2 = new Record().putField("text", new SingleLineTextFieldValue("test value 2"));
        BulkRequestsRequest req = new BulkRequestsRequest();
        req.registerAddRecord(new AddRecordRequest().setApp(1L).setRecord(record1));
        req.registerUpdateRecord(new UpdateRecordRequest().setApp(2L).setId(1L).setRecord(record2));

        BulkRequestsResponseBody resp = sut.bulkRequest(req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getResults()).hasSize(2);

        AddRecordResponseBody resp1 = (AddRecordResponseBody) resp.getResults().get(0);
        assertThat(resp1.getId()).isEqualTo(1);
        assertThat(resp1.getRevision()).isEqualTo(1);

        UpdateRecordResponseBody resp2 = (UpdateRecordResponseBody) resp.getResults().get(1);
        assertThat(resp2.getRevision()).isEqualTo(2);
    }

    @Test
    public void download() throws IOException {
        String request = loadResource("InternalClientImplTest_download_request.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/file.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "text/plain")
                                .withHeader("Content-Length", "4")
                                .withHeader("Content-Disposition", "attachment; filename=\"test.txt\"")
                                .withBody("test"));

        InternalClientImpl sut = setupClient();
        DownloadFileRequest req = new DownloadFileRequest().setFileKey("testkey");
        DownloadFileResponseBody resp = sut.download(req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getContentType()).isEqualTo("text/plain");
        assertThat(resp.getContentLength()).isEqualTo(4L);
        String content;
        try (InputStream in = resp.getContent()) {
            content = new Scanner(in).useDelimiter("\\A").next();
        }
        assertThat(content).isEqualTo("test");
    }

    @Test
    public void upload() throws IOException {
        String boundary = "__END_OF_PART__";
        String response = loadResource("InternalClientImplTest_upload_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/file.json")
                        .withMethod("POST")
                        .withHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                        .withHeader("X-Cybozu-Authorization", passwordAuth);
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        UploadFileResponseBody resp;
        try (InputStream in = new ByteArrayInputStream("test".getBytes())) {
            resp = sut.upload("test.txt", "text/plain", in, Collections.emptyList()).getBody();
        }
        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getFileKey()).isEqualTo("testkey");

        HttpRequest recordedRequest = (HttpRequest) server.retrieveRecordedRequests(httpRequest)[0];
        String body = recordedRequest.getBodyAsString();
        assertThat(body).startsWith("--" + boundary + "\r\n");
        assertThat(body).endsWith("\r\n--" + boundary + "--\r\n");
        Map<String, String> headers = getMultipartHeaders(body);
        assertThat(headers).containsEntry("Content-Type", "text/plain");
        assertThat(headers)
                .containsEntry("Content-Disposition", "form-data; name=\"file\"; filename=\"test.txt\"");
        assertThat(getMultipartContent(body)).isEqualTo("test");
    }

    @Test
    public void upload_multibyte_character() throws IOException {
        String boundary = "__END_OF_PART__";
        String response = loadResource("InternalClientImplTest_upload_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/file.json")
                        .withMethod("POST")
                        .withHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                        .withHeader("X-Cybozu-Authorization", passwordAuth);
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        UploadFileResponseBody resp;
        try (InputStream in = new ByteArrayInputStream("test".getBytes())) {
            resp = sut.upload("日本語ファイル名.jpg", "image/jpeg", in, Collections.emptyList()).getBody();
        }
        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getFileKey()).isEqualTo("testkey");

        HttpRequest recordedRequest = (HttpRequest) server.retrieveRecordedRequests(httpRequest)[0];
        String body = recordedRequest.getBodyAsString();
        assertThat(body).startsWith("--" + boundary + "\r\n");
        assertThat(body).endsWith("\r\n--" + boundary + "--\r\n");
        Map<String, String> headers = getMultipartHeaders(body);
        assertThat(headers).containsEntry("Content-Type", "image/jpeg");
        String expect = "form-data; name=\"file\"; filename=\"日本語ファイル名.jpg\"";
        expect = new String(expect.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
        assertThat(headers).containsEntry("Content-Disposition", expect);
        assertThat(getMultipartContent(body)).isEqualTo("test");
    }

    @Test
    public void call_authByApiToken() {
        String apiToken = "abcdefghijklmnopqrstuvwxyz1234567890ABCD";
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-API-Token", apiToken)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        String baseUrl = "http://localhost:" + server.getLocalPort();
        Auth auth = Auth.byApiToken(apiToken);
        InternalClientImpl sut =
                new InternalClientImpl(
                        baseUrl, auth, null, null, null, null, null, "", timeout, timeout, timeout);
        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp = sut.call(KintoneApi.GET_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        Record expectedRecord = setupExpectedRecord();
        assertThat(resp.getRecord()).usingRecursiveComparison().isEqualTo(expectedRecord);
    }

    @Test
    public void call_authByApiTokenList() {
        String apiToken1 = "abcdefghijklmnopqrstuvwxyz1234567890ABC1";
        String apiToken2 = "abcdefghijklmnopqrstuvwxyz1234567890ABC2";
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-API-Token", apiToken1 + "," + apiToken2)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        String baseUrl = "http://localhost:" + server.getLocalPort();
        Auth auth = Auth.byApiToken(Arrays.asList(apiToken1, apiToken2));
        InternalClientImpl sut =
                new InternalClientImpl(
                        baseUrl, auth, null, null, null, null, null, "", timeout, timeout, timeout);

        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp = sut.call(KintoneApi.GET_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        Record expectedRecord = setupExpectedRecord();
        assertThat(resp.getRecord()).usingRecursiveComparison().isEqualTo(expectedRecord);
    }

    @Test
    public void call_authWithBasic() {
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withHeader("Authorization", "Basic YmFzaWNVc2VyOmJhc2ljUGFzc3dvcmQ=")
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        String baseUrl = "http://localhost:" + server.getLocalPort();
        Auth auth = Auth.byPassword(userName, password);
        BasicAuth basicAuth = new BasicAuth("basicUser", "basicPassword");
        InternalClientImpl sut =
                new InternalClientImpl(
                        baseUrl, auth, basicAuth, null, null, null, null, "", timeout, timeout, timeout);
        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp = sut.call(KintoneApi.GET_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        Record expectedRecord = setupExpectedRecord();
        assertThat(resp.getRecord()).usingRecursiveComparison().isEqualTo(expectedRecord);
    }

    @Test
    public void call_guestSpace() {
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/guest/123/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient(123);
        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp = sut.call(KintoneApi.GET_RECORD, req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        Record expectedRecord = setupExpectedRecord();
        assertThat(resp.getRecord()).usingRecursiveComparison().isEqualTo(expectedRecord);
    }

    @Test
    public void bulkRequest_guestSpace() {
        String request = loadResource("InternalClientImplTest_bulkRequest_guestSpace_request.json");
        String response = loadResource("InternalClientImplTest_bulkRequest_guestSpace_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/guest/123/v1/bulkRequest.json")
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient(123);
        Record record1 = new Record().putField("text", new SingleLineTextFieldValue("test value 1"));
        Record record2 = new Record().putField("text", new SingleLineTextFieldValue("test value 2"));
        BulkRequestsRequest req = new BulkRequestsRequest();
        req.registerAddRecord(new AddRecordRequest().setApp(1L).setRecord(record1));
        req.registerUpdateRecord(new UpdateRecordRequest().setApp(2L).setId(1L).setRecord(record2));

        BulkRequestsResponseBody resp = sut.bulkRequest(req, Collections.emptyList());

        server.verify(httpRequest, VerificationTimes.once());
        assertThat(resp.getResults()).hasSize(2);

        AddRecordResponseBody resp1 = (AddRecordResponseBody) resp.getResults().get(0);
        assertThat(resp1.getId()).isEqualTo(1);
        assertThat(resp1.getRevision()).isEqualTo(1);

        UpdateRecordResponseBody resp2 = (UpdateRecordResponseBody) resp.getResults().get(1);
        assertThat(resp2.getRevision()).isEqualTo(2);
    }

    @Test
    public void call_dontRedirect() {
        String request = loadResource("InternalClientImplTest_call_GET_request.json");
        String response = loadResource("InternalClientImplTest_call_GET_response.json");
        HttpRequest httpRequest =
                HttpRequest.request("/k/v1/record.json")
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-Cybozu-Authorization", passwordAuth)
                        .withBody(JsonBody.json(request, MatchType.STRICT));
        server
                .when(httpRequest)
                .respond(
                        HttpResponse.response().withStatusCode(301).withHeader("Location", "/redirected.json"));
        server
                .when(HttpRequest.request("/redirected.json"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response));

        InternalClientImpl sut = setupClient();
        GetRecordRequest req = new GetRecordRequest().setApp(1L).setId(1L);
        GetRecordResponseBody resp =
                sut.call(
                        KintoneApi.GET_RECORD,
                        req,
                        Collections.singletonList(r -> assertThat(r.getStatusCode()).isEqualTo(301)));

        server.verify(httpRequest, VerificationTimes.once());
        server.verify(HttpRequest.request("/redirected.json"), VerificationTimes.exactly(0));
        assertThat(resp).isNull();
    }
}
