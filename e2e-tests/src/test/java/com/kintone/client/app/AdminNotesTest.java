package com.kintone.client.app;

import static org.assertj.core.api.Assertions.*;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.GetAdminNotesPreviewRequest;
import com.kintone.client.api.app.GetAdminNotesPreviewResponseBody;
import com.kintone.client.api.app.GetAdminNotesRequest;
import com.kintone.client.api.app.GetAdminNotesResponseBody;
import com.kintone.client.api.app.UpdateAdminNotesRequest;
import com.kintone.client.exception.KintoneApiRuntimeException;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Space;
import org.junit.jupiter.api.Test;

/** AppClientの管理者用メモに関するテスト */
public class AdminNotesTest extends ApiTestBase {
    @Test
    public void getAdminNotes_getAdminNotesPreview_updateAdminNotes() {
        KintoneClient client = setupDefaultClient();

        // アプリ作成、管理者用メモを設定、デプロイ
        App app = App.create(client, "getAdminNotes_getAdminNotesPreview");
        UpdateAdminNotesRequest req = new UpdateAdminNotesRequest();
        String adminNotes1 = "Admin Notes For Get Admin Notes";
        req.setApp(app.id());
        req.setContent(adminNotes1);
        req.setIncludeInTemplateAndDuplicates(false);
        client.app().updateAdminNotes(req);

        app.deploy();
        long revision = app.getAppRevision(true);

        // previewの管理者用メモを更新
        String adminNotes2 = "Admin Notes For Get Admin Notes Preview";
        req.setApp(app.id());
        req.setContent(adminNotes2);
        req.setIncludeInTemplateAndDuplicates(true);
        long updatedRevision = client.app().updateAdminNotes(req).getRevision();

        GetAdminNotesRequest req1 = new GetAdminNotesRequest();
        req1.setApp(app.id());
        GetAdminNotesResponseBody resp1 = client.app().getAdminNotes(req1);
        assertThat(resp1.getContent()).isEqualTo(adminNotes1);
        assertThat(resp1.isIncludeInTemplateAndDuplicates()).isFalse();
        assertThat(resp1.getRevision()).isEqualTo(revision);

        GetAdminNotesPreviewRequest req2 = new GetAdminNotesPreviewRequest();
        req2.setApp(app.id());
        GetAdminNotesPreviewResponseBody resp2 = client.app().getAdminNotesPreview(req2);
        assertThat(resp2.getContent()).isEqualTo(adminNotes2);
        assertThat(resp2.isIncludeInTemplateAndDuplicates()).isTrue();
        assertThat(resp2.getRevision()).isEqualTo(updatedRevision);
    }

    @Test
    public void getAdminNotes_getAdminNotesPreview_1() {
        // 管理者用メモが1文字以上あり、アプリテンプレートが含まれる場合
        KintoneClient client = setupDefaultClient();

        // アプリ作成、管理者用メモを設定、デプロイ
        App app = App.create(client, "getAdminNotes_getAdminNotesPreview_1");
        long appId = app.id();
        UpdateAdminNotesRequest updateReq = new UpdateAdminNotesRequest();
        String adminNotesContent = "<div>アプリの管理者用メモ</div>";
        updateReq.setApp(appId);
        updateReq.setContent(adminNotesContent);
        updateReq.setIncludeInTemplateAndDuplicates(true);
        client.app().updateAdminNotes(updateReq);
        app.deploy();

        GetAdminNotesRequest req = new GetAdminNotesRequest();
        req.setApp(appId);
        GetAdminNotesResponseBody resp = client.app().getAdminNotes(req);
        assertThat(resp.getContent()).isEqualTo(adminNotesContent);
        assertThat(resp.isIncludeInTemplateAndDuplicates()).isTrue();

        GetAdminNotesPreviewRequest previewReq = new GetAdminNotesPreviewRequest();
        previewReq.setApp(appId);
        GetAdminNotesPreviewResponseBody previewResp = client.app().getAdminNotesPreview(previewReq);
        assertThat(previewResp.getContent()).isEqualTo(adminNotesContent);
        assertThat(previewResp.isIncludeInTemplateAndDuplicates()).isTrue();
    }

    @Test
    public void getAdminNotes_getAdminNotesPreview_2() {
        // 管理者用メモが空で、アプリテンプレートに含まれない場合
        Space guestSpace = Space.guest(this);
        KintoneClient client = setupDefaultClient(guestSpace.id());

        // アプリ作成、管理者用メモを設定、デプロイ
        App app =
                App.create(
                        client,
                        "getAdminNotes_" + System.currentTimeMillis(),
                        guestSpace.id(),
                        guestSpace.getDefaultThread());
        long appId = app.id();
        UpdateAdminNotesRequest updateReq = new UpdateAdminNotesRequest();
        updateReq.setApp(appId);
        updateReq.setIncludeInTemplateAndDuplicates(false);
        client.app().updateAdminNotes(updateReq);
        app.deploy();

        GetAdminNotesRequest req = new GetAdminNotesRequest();
        req.setApp(appId);
        GetAdminNotesResponseBody resp = client.app().getAdminNotes(req);
        assertThat(resp.getContent()).isEqualTo("");
        assertThat(resp.isIncludeInTemplateAndDuplicates()).isFalse();

        GetAdminNotesPreviewRequest previewReq = new GetAdminNotesPreviewRequest();
        previewReq.setApp(appId);
        GetAdminNotesPreviewResponseBody previewResp = client.app().getAdminNotesPreview(previewReq);
        assertThat(previewResp.getContent()).isEqualTo("");
        assertThat(previewResp.isIncludeInTemplateAndDuplicates()).isFalse();
    }

    @Test
    public void getAdminNotes_getAdminNotesPreview_3() {
        // 必須パラメータがない場合エラーとなる
        KintoneClient client = setupDefaultClient();

        // アプリ作成、管理者用メモを設定、デプロイ
        App app = App.create(client, "getAdminNotes_getAdminNotesPreview_3");
        long appId = app.id();
        UpdateAdminNotesRequest updateReq = new UpdateAdminNotesRequest();
        String adminNotesContent = "<div>アプリの管理者用メモ</div>";
        updateReq.setApp(appId);
        updateReq.setContent(adminNotesContent);
        updateReq.setIncludeInTemplateAndDuplicates(true);
        client.app().updateAdminNotes(updateReq);
        app.deploy();

        GetAdminNotesRequest req = new GetAdminNotesRequest();
        assertThatThrownBy(() -> client.app().getAdminNotes(req))
                .isInstanceOf(KintoneApiRuntimeException.class);

        GetAdminNotesPreviewRequest previewReq = new GetAdminNotesPreviewRequest();
        assertThatThrownBy(() -> client.app().getAdminNotesPreview(previewReq))
                .isInstanceOf(KintoneApiRuntimeException.class);
    }

    @Test
    public void updateAdminNotes_1() {
        // 必須パラメータのみで管理者用メモが更新できる
        KintoneClient client = setupDefaultClient();

        // アプリ作成、管理者用メモを設定、デプロイ
        App app = App.create(client, "updateAdminNotes_1");
        long appId = app.id();
        UpdateAdminNotesRequest updateReq = new UpdateAdminNotesRequest();
        updateReq.setApp(appId);
        client.app().updateAdminNotes(updateReq);

        GetAdminNotesPreviewRequest req = new GetAdminNotesPreviewRequest();
        req.setApp(appId);
        GetAdminNotesPreviewResponseBody resp = client.app().getAdminNotesPreview(req);
        assertThat(resp.getContent()).isEqualTo("");
        assertThat(resp.isIncludeInTemplateAndDuplicates()).isFalse();
    }

    @Test
    public void updateAdminNotes_2() {
        // contentが0文字、revisionが-1で実行できること
        // アプリテンプレートに含むかの項目を変更できること
        Space guestSpace = Space.guest(this);
        KintoneClient client = setupDefaultClient(guestSpace.id());

        // アプリ作成、管理者用メモを設定、デプロイ
        String appName = "updateAdminNotes_" + System.currentTimeMillis();
        App app = App.create(client, appName, guestSpace.id(), guestSpace.getDefaultThread());
        long appId = app.id();
        UpdateAdminNotesRequest preUpdateReq = new UpdateAdminNotesRequest();
        preUpdateReq.setApp(appId);
        preUpdateReq.setContent("updateAdminNotes_2");
        client.app().updateAdminNotes(preUpdateReq);

        // 管理者用メモの事前設定
        GetAdminNotesPreviewRequest preGetReq = new GetAdminNotesPreviewRequest();
        preGetReq.setApp(appId);
        assertThat(client.app().getAdminNotesPreview(preGetReq).getContent())
                .isEqualTo("updateAdminNotes_2");

        // 管理者用メモの更新
        UpdateAdminNotesRequest updateReq = new UpdateAdminNotesRequest();
        updateReq.setApp(appId);
        updateReq.setContent("");
        updateReq.setIncludeInTemplateAndDuplicates(true);
        updateReq.setRevision(-1L);
        client.app().updateAdminNotes(updateReq);

        GetAdminNotesPreviewRequest getReq = new GetAdminNotesPreviewRequest();
        getReq.setApp(appId);
        GetAdminNotesPreviewResponseBody resp = client.app().getAdminNotesPreview(getReq);
        assertThat(resp.getContent()).isEqualTo("");
        assertThat(resp.isIncludeInTemplateAndDuplicates()).isTrue();
    }

    @Test
    public void updateAdminNotes_3() {
        // 必須パラメータがない場合エラーとなる
        KintoneClient client = setupDefaultClient();

        // アプリ作成、管理者用メモを設定、デプロイ
        App app = App.create(client, "updateAdminNotes_3");
        UpdateAdminNotesRequest updateReq = new UpdateAdminNotesRequest();
        assertThatThrownBy(() -> client.app().updateAdminNotes(updateReq))
                .isInstanceOf(KintoneApiRuntimeException.class);
    }
}
