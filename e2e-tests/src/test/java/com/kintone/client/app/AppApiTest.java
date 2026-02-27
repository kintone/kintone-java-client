package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.helper.App;
import com.kintone.client.helper.AppCustomizeBuilder;
import com.kintone.client.helper.AppSettingsBuilder;
import com.kintone.client.helper.Space;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * AppClientのテスト
 *
 * <p>AppClientのテストは数が多くなると思われるので、fieldsやviewsなど適当な単位でファイル分割している。
 *
 * <p>このファイルには個別に分類する程でもないもの（アプリ全般設定など）を集めている。
 */
public class AppApiTest extends ApiTestBase {
    @Test
    public void getApp() {
        Space space = Space.singleThread(this);
        long spaceId = space.id();
        long threadId = space.getDefaultThread();

        KintoneClient client = setupDefaultClient();
        String appName = "getApp_" + System.currentTimeMillis();
        App app = App.create(client, appName, spaceId, threadId).deploy();

        GetAppRequest req = new GetAppRequest();
        req.setId(app.id());
        GetAppResponseBody resp = client.app().getApp(req);
        assertThat(resp.getAppId()).isEqualTo(app.id());
        assertThat(resp.getCode()).isEqualTo("");
        assertThat(resp.getName()).isEqualTo(appName);
        assertThat(resp.getDescription()).isEqualTo("");
        assertThat(resp.getSpaceId()).isEqualTo(spaceId);
        assertThat(resp.getThreadId()).isEqualTo(threadId);
        assertThat(resp.getCreator().getCode()).isEqualTo(getDefaultUser());
        assertThat(resp.getModifier().getCode()).isEqualTo(getDefaultUser());
        assertThat(resp.getCreatedAt()).isNotNull();
        assertThat(resp.getModifiedAt()).isNotNull();
    }

    @Test
    public void getAppCustomize_getAppCustomizePreview() {
        KintoneClient client = setupDefaultClient();
        String key1 = uploadMockFile(client, "desktop.js", "application/javascript", "// desktop.js");
        String key2 = uploadMockFile(client, "desktop.css", "text/css", "// desktop.css");
        String key3 = uploadMockFile(client, "mobile.js", "application/javascript", "// mobile.js");
        String key4 = uploadMockFile(client, "mobile.css", "text/css", "// mobile.css");

        App app = App.create(client, "getAppCustomize_getAppCustomizePreview");
        AppCustomizeBuilder builder =
                new AppCustomizeBuilder()
                        .scope(CustomizeScope.ALL)
                        .desktopJsUrl("https://localhost/desktop.js")
                        .desktopJsFile(key1)
                        .desktopCssUrl("https://localhost/desktop.css")
                        .desktopCssFile(key2)
                        .mobileJsUrl("https://localhost/mobile.js")
                        .mobileJsFile(key3)
                        .mobileCssUrl("https://localhost/mobile.css")
                        .mobileCssFile(key4);
        app.updateAppCustomize(builder).deploy();
        long revision = app.getAppRevision(false);

        GetAppCustomizeRequest req1 = new GetAppCustomizeRequest();
        req1.setApp(app.id());
        GetAppCustomizeResponseBody resp1 = client.app().getAppCustomize(req1);
        assertThat(resp1.getScope()).isEqualTo(CustomizeScope.ALL);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        CustomizeBody desktop = resp1.getDesktop();
        assertCustomizeResources(
                desktop.getJs(),
                CustomizeType.URL,
                "https://localhost/desktop.js",
                CustomizeType.FILE,
                "desktop.js");
        assertCustomizeResources(
                desktop.getCss(),
                CustomizeType.URL,
                "https://localhost/desktop.css",
                CustomizeType.FILE,
                "desktop.css");
        CustomizeBody mobile = resp1.getMobile();
        assertCustomizeResources(
                mobile.getJs(),
                CustomizeType.URL,
                "https://localhost/mobile.js",
                CustomizeType.FILE,
                "mobile.js");
        assertCustomizeResources(
                mobile.getCss(),
                CustomizeType.URL,
                "https://localhost/mobile.css",
                CustomizeType.FILE,
                "mobile.css");

        app.updateAppCustomize(new AppCustomizeBuilder().scope(CustomizeScope.NONE));

        GetAppCustomizePreviewRequest req2 = new GetAppCustomizePreviewRequest();
        req2.setApp(app.id());
        GetAppCustomizePreviewResponseBody resp2 = client.app().getAppCustomizePreview(req2);
        assertThat(resp2.getScope()).isEqualTo(CustomizeScope.NONE);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        desktop = resp1.getDesktop();
        assertCustomizeResources(
                desktop.getJs(),
                CustomizeType.URL,
                "https://localhost/desktop.js",
                CustomizeType.FILE,
                "desktop.js");
        assertCustomizeResources(
                desktop.getCss(),
                CustomizeType.URL,
                "https://localhost/desktop.css",
                CustomizeType.FILE,
                "desktop.css");
        mobile = resp1.getMobile();
        assertCustomizeResources(
                mobile.getJs(),
                CustomizeType.URL,
                "https://localhost/mobile.js",
                CustomizeType.FILE,
                "mobile.js");
        assertCustomizeResources(
                mobile.getCss(),
                CustomizeType.URL,
                "https://localhost/mobile.css",
                CustomizeType.FILE,
                "mobile.css");
    }

    @Test
    public void getApps() {
        Space space = Space.singleThread(this);
        long spaceId = space.id();
        long threadId = space.getDefaultThread();

        KintoneClient client = setupDefaultClient();
        String appName1 = "getApps1_" + System.currentTimeMillis();
        String appName2 = "getApps2_" + System.currentTimeMillis();
        App app1 = App.create(client, appName1, spaceId, threadId).deploy();
        App app2 = App.create(client, appName2).deploy();

        GetAppsRequest req = new GetAppsRequest();
        req.setIds(Arrays.asList(app1.id(), app2.id()));
        GetAppsResponseBody resp = client.app().getApps(req);
        List<com.kintone.client.model.app.App> apps = resp.getApps();
        assertThat(apps).hasSize(2);

        assertThat(apps.get(0).getAppId()).isEqualTo(app1.id());
        assertThat(apps.get(0).getCode()).isEqualTo("");
        assertThat(apps.get(0).getName()).isEqualTo(appName1);
        assertThat(apps.get(0).getDescription()).isEqualTo("");
        assertThat(apps.get(0).getSpaceId()).isEqualTo(spaceId);
        assertThat(apps.get(0).getThreadId()).isEqualTo(threadId);
        assertThat(apps.get(0).getCreator().getCode()).isEqualTo(getDefaultUser());
        assertThat(apps.get(0).getModifier().getCode()).isEqualTo(getDefaultUser());
        assertThat(apps.get(0).getCreatedAt()).isNotNull();
        assertThat(apps.get(0).getModifiedAt()).isNotNull();

        assertThat(apps.get(1).getAppId()).isEqualTo(app2.id());
        assertThat(apps.get(1).getCode()).isEqualTo("");
        assertThat(apps.get(1).getName()).isEqualTo(appName2);
        assertThat(apps.get(1).getDescription()).isEqualTo("");
        assertThat(apps.get(1).getSpaceId()).isNull();
        assertThat(apps.get(1).getThreadId()).isNull();
        assertThat(apps.get(1).getCreator().getCode()).isEqualTo(getDefaultUser());
        assertThat(apps.get(1).getModifier().getCode()).isEqualTo(getDefaultUser());
        assertThat(apps.get(1).getCreatedAt()).isNotNull();
        assertThat(apps.get(1).getModifiedAt()).isNotNull();
    }

    @Test
    public void move() {
        Space space = Space.singleThread(this);
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "moveToSpace_" + System.currentTimeMillis()).deploy();

        client.app().move(app.id(), space.id());
        assertThat(client.app().getApp(app.id()).getSpaceId()).isEqualTo(space.id());
        client.app().move(app.id(), null);
        assertThat(client.app().getApp(app.id()).getSpaceId()).isNull();
    }

    @Test
    public void getAppSettings_getAppSettingsPreview() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "getAppSettings_getAppSettingsPreview");
        AppSettingsBuilder builder =
                new AppSettingsBuilder().description("description").theme("GREEN").presetIcon("APP60");
        app.updateAppSettings(builder).deploy();

        GetAppSettingsRequest req1 = new GetAppSettingsRequest();
        req1.setApp(app.id());
        req1.setLang("default");
        GetAppSettingsResponseBody resp1 = client.app().getAppSettings(req1);
        assertThat(resp1.getName()).isEqualTo("getAppSettings_getAppSettingsPreview");
        assertThat(resp1.getDescription()).isEqualTo("description");
        assertThat(resp1.getTheme()).isEqualTo("GREEN");
        assertThat(resp1.getIcon().getType()).isEqualTo(AppIconType.PRESET);
        assertThat(((AppPresetIcon) resp1.getIcon()).getKey()).isEqualTo("APP60");
        if (resp1.getNumberPrecision() != null) {
            // NumberPrecisionなどが有効な場合
            assertThat(resp1.getNumberPrecision().getDigits()).isEqualTo(16);
            assertThat(resp1.getNumberPrecision().getDecimalPlaces()).isEqualTo(4);
            assertThat(resp1.getNumberPrecision().getRoundingMode()).isEqualTo(RoundingMode.HALF_EVEN);
            assertThat(resp1.getFirstMonthOfFiscalYear()).isEqualTo(4);
            assertThat(resp1.isEnableThumbnails()).isTrue();
            assertThat(resp1.isEnableComments()).isTrue();
            assertThat(resp1.isEnableDuplicateRecord()).isTrue();
            assertThat(resp1.isEnableBulkDeletion()).isFalse();
        }

        builder =
                new AppSettingsBuilder()
                        .name("changed")
                        .description("description 2")
                        .theme("BLUE")
                        .presetIcon("APP59");
        app.updateAppSettings(builder);

        GetAppSettingsPreviewRequest req2 = new GetAppSettingsPreviewRequest();
        req2.setApp(app.id());
        req2.setLang("default");
        GetAppSettingsPreviewResponseBody resp2 = client.app().getAppSettingsPreview(req2);
        assertThat(resp2.getName()).isEqualTo("changed");
        assertThat(resp2.getDescription()).isEqualTo("description 2");
        assertThat(resp2.getTheme()).isEqualTo("BLUE");
        assertThat(resp2.getIcon().getType()).isEqualTo(AppIconType.PRESET);
        assertThat(((AppPresetIcon) resp2.getIcon()).getKey()).isEqualTo("APP59");
        if (resp2.getNumberPrecision() != null) {
            assertThat(resp2.getNumberPrecision().getDigits()).isEqualTo(16);
            assertThat(resp2.getNumberPrecision().getDecimalPlaces()).isEqualTo(4);
            assertThat(resp2.getNumberPrecision().getRoundingMode()).isEqualTo(RoundingMode.HALF_EVEN);
            assertThat(resp2.getFirstMonthOfFiscalYear()).isEqualTo(4);
            assertThat(resp2.isEnableThumbnails()).isTrue();
            assertThat(resp2.isEnableComments()).isTrue();
            assertThat(resp2.isEnableDuplicateRecord()).isTrue();
            assertThat(resp2.isEnableBulkDeletion()).isFalse();
        }
    }

    @Test
    public void updateAppCustomize() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateAppCustomize").deploy();
        long revision = app.getAppRevision(true);
        String key1 = uploadMockFile(client, "desktop.js", "application/javascript", "// desktop.js");
        String key2 = uploadMockFile(client, "mobile.js", "application/javascript", "// mobile.js");

        UpdateAppCustomizeRequest req = new UpdateAppCustomizeRequest();
        req.setApp(app.id());
        req.setScope(CustomizeScope.ADMIN);
        req.setDesktop(makeCustomizeBody(key1, "https://localhost/desktop.css"));
        req.setMobile(makeCustomizeBody(key2, "https://localhost/mobile.css"));
        req.setRevision(revision);
        UpdateAppCustomizeResponseBody resp = client.app().updateAppCustomize(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        App.AppCustomize previewSettings = app.getAppCustomize(true);
        assertThat(previewSettings.getScope()).isEqualTo(CustomizeScope.ADMIN);
        assertThat(previewSettings.getRevision()).isEqualTo(revision + 1);
        CustomizeBody desktop = previewSettings.getDesktop();
        assertCustomizeResources(desktop.getJs(), CustomizeType.FILE, "desktop.js");
        assertCustomizeResources(desktop.getCss(), CustomizeType.URL, "https://localhost/desktop.css");
        CustomizeBody mobile = previewSettings.getMobile();
        assertCustomizeResources(mobile.getJs(), CustomizeType.FILE, "mobile.js");
        assertCustomizeResources(mobile.getCss(), CustomizeType.URL, "https://localhost/mobile.css");

        App.AppCustomize settings = app.getAppCustomize(false);
        assertThat(settings.getScope()).isEqualTo(CustomizeScope.ALL);
    }

    @Test
    public void updateAppSettings() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateAppSettings").deploy();
        long revision = app.getAppRevision(true);
        boolean supportNumberPrecision =
                client.app().getAppSettingsPreview(app.id()).getNumberPrecision() != null;

        UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
        req.setApp(app.id());
        req.setName("updateAppSettings_updated");
        req.setDescription("app description");
        req.setTheme("YELLOW");
        req.setIcon(new AppPresetIcon().setKey("APP60"));
        req.setRevision(revision);
        if (supportNumberPrecision) {
            req.setNumberPrecision(
                    new NumberPrecision()
                            .setDigits(30)
                            .setDecimalPlaces(10)
                            .setRoundingMode(RoundingMode.UP));
            req.setFirstMonthOfFiscalYear(6);
            req.setEnableThumbnails(false);
            req.setEnableComments(false);
            req.setEnableDuplicateRecord(false);
            req.setEnableBulkDeletion(true);
        }
        UpdateAppSettingsResponseBody resp = client.app().updateAppSettings(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        App.AppSettings previewSettings = app.getAppSettings(true);
        assertThat(previewSettings)
                .usingRecursiveComparison()
                .isEqualTo(
                        new App.AppSettings(
                                "updateAppSettings_updated",
                                "app description",
                                new AppPresetIcon().setKey("APP60"),
                                "YELLOW",
                                revision + 1));

        App.AppSettings settings = app.getAppSettings(false);
        assertThat(settings.getName()).isEqualTo("updateAppSettings");

        if (supportNumberPrecision) {
            GetAppSettingsPreviewResponseBody response = client.app().getAppSettingsPreview(app.id());
            assertThat(response.getNumberPrecision().getDigits()).isEqualTo(30);
            assertThat(response.getNumberPrecision().getDecimalPlaces()).isEqualTo(10);
            assertThat(response.getNumberPrecision().getRoundingMode()).isEqualTo(RoundingMode.UP);
            assertThat(response.getFirstMonthOfFiscalYear()).isEqualTo(6);
            assertThat(response.isEnableThumbnails()).isFalse();
            assertThat(response.isEnableComments()).isFalse();
            assertThat(response.isEnableDuplicateRecord()).isFalse();
            assertThat(response.isEnableBulkDeletion()).isTrue();
        }
    }

    @Test
    public void updateAppSettingsFileIcon() throws IOException {
        KintoneClient client = setupDefaultClient();

        Path path = new File(AppApiTest.class.getResource("fileicon.png").getFile()).toPath();
        String fileKey = client.file().uploadFile(path, "image/png");

        App app = App.create(client, "updateAppSettingsFileIcon").deploy();
        long revision = app.getAppRevision(true);

        UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
        req.setApp(app.id());
        req.setIcon(new AppFileIcon().setFile(new FileBody().setFileKey(fileKey)));

        UpdateAppSettingsResponseBody resp = client.app().updateAppSettings(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        App.AppSettings settings = app.getAppSettings(true);
        assertThat(((AppFileIcon) settings.getIcon()).getFile().getName()).isEqualTo("fileicon.png");
        assertThat(((AppFileIcon) settings.getIcon()).getFile().getContentType())
                .isEqualTo("image/png");
    }

    private String uploadMockFile(KintoneClient client, String name, String type, String data) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes())) {
            UploadFileRequest req1 = new UploadFileRequest();
            req1.setFilename(name);
            req1.setContentType(type);
            req1.setContent(in);
            return client.file().uploadFile(req1).getFileKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CustomizeBody makeCustomizeBody(String fileKey, String url) {
        CustomizeResource r1 = new CustomizeFileResource().setFile(new FileBody().setFileKey(fileKey));
        CustomizeResource r2 = new CustomizeUrlResource().setUrl(url);
        CustomizeBody body = new CustomizeBody();
        body.setJs(Collections.singletonList(r1));
        body.setCss(Collections.singletonList(r2));
        return body;
    }

    private void assertCustomizeResources(List<CustomizeResource> resources, Object... data) {
        // dataは CustomizeType, ファイル名かURL の繰り返し
        int size = data.length / 2;
        assertThat(resources).hasSize(size);
        for (int i = 0; i < size; i++) {
            CustomizeResource resource = resources.get(i);
            CustomizeType type = (CustomizeType) data[i * 2];
            String value = (String) data[i * 2 + 1];
            assertThat(resource.getType()).isEqualTo(type);
            if (type == CustomizeType.FILE) {
                assertThat(((CustomizeFileResource) resource).getFile().getName()).isEqualTo(value);
            } else {
                assertThat(((CustomizeUrlResource) resource).getUrl()).isEqualTo(value);
            }
        }
    }

    @Test
    @Disabled("Skipped until AppStatistics model is updated for new API fields")
    public void getStatistics() {
        KintoneClient client = setupDefaultClient();

        GetAppsStatisticsRequest req = new GetAppsStatisticsRequest();
        req.setLimit(10L);
        req.setOffset(0L);
        GetAppsStatisticsResponseBody resp = client.app().getStatistics(req);

        List<AppStatistics> apps = resp.getApps();
        assertThat(apps).isNotNull();
        assertThat(apps).isNotEmpty();

        AppStatistics stats = apps.get(0);
        assertThat(stats.getId()).isNotNull();
        assertThat(stats.getName()).isNotNull();
        // space はスペースに属していない場合はnull
        // appGroup はnullの可能性あり
        assertThat(stats.getStatus()).isNotNull();
        // recordUpdatedAt はレコードがない場合null
        assertThat(stats.getRecordCount()).isNotNull();
        assertThat(stats.getFieldCount()).isNotNull();
        assertThat(stats.getDailyRequestCount()).isNotNull();
        assertThat(stats.getStorageUsage()).isNotNull();
        assertThat(stats.getCustomized()).isNotNull();
        // creator, modifier はシステムアプリの場合nullの可能性あり
        assertThat(stats.getCreatedAt()).isNotNull();
        // modifiedAt はnullの可能性あり
    }
}
