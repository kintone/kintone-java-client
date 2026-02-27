package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.app.*;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.helper.App;
import com.kintone.client.helper.AppCustomizeBuilder;
import com.kintone.client.helper.AppSettingsBuilder;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private KintoneClient client;
    private App app;
    private App.AppCustomize originalCustomize;
    private App.AppSettings originalSettings;

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
            originalCustomize = app.getAppCustomize(false);
            originalSettings = app.getAppSettings(false);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
    }

    @AfterEach
    public void cleanupApp() {
        if (app != null) {
            try {
                restoreAppCustomize();
                restoreAppSettings();
                client.app().deployApp(app.id());
                app.waitDeploy();
            } catch (Exception e) {
                // ignore cleanup errors
            }
        }
    }

    private void restoreAppCustomize() {
        AppCustomizeBuilder builder = new AppCustomizeBuilder().scope(originalCustomize.getScope());
        addCustomizeResources(builder, originalCustomize.getDesktop(), true);
        addCustomizeResources(builder, originalCustomize.getMobile(), false);
        app.updateAppCustomize(builder);
    }

    private void addCustomizeResources(
            AppCustomizeBuilder builder, CustomizeBody body, boolean isDesktop) {
        if (body == null) return;
        for (CustomizeResource res : body.getJs()) {
            if (res.getType() == CustomizeType.URL) {
                if (isDesktop) {
                    builder.desktopJsUrl(((CustomizeUrlResource) res).getUrl());
                } else {
                    builder.mobileJsUrl(((CustomizeUrlResource) res).getUrl());
                }
            } else {
                if (isDesktop) {
                    builder.desktopJsFile(((CustomizeFileResource) res).getFile().getFileKey());
                } else {
                    builder.mobileJsFile(((CustomizeFileResource) res).getFile().getFileKey());
                }
            }
        }
        for (CustomizeResource res : body.getCss()) {
            if (res.getType() == CustomizeType.URL) {
                if (isDesktop) {
                    builder.desktopCssUrl(((CustomizeUrlResource) res).getUrl());
                } else {
                    builder.mobileCssUrl(((CustomizeUrlResource) res).getUrl());
                }
            } else {
                if (isDesktop) {
                    builder.desktopCssFile(((CustomizeFileResource) res).getFile().getFileKey());
                } else {
                    builder.mobileCssFile(((CustomizeFileResource) res).getFile().getFileKey());
                }
            }
        }
    }

    private void restoreAppSettings() {
        AppSettingsBuilder builder =
                new AppSettingsBuilder()
                        .name(originalSettings.getName())
                        .description(originalSettings.getDescription())
                        .theme(originalSettings.getTheme());
        if (originalSettings.getIcon() instanceof AppPresetIcon) {
            builder.presetIcon(((AppPresetIcon) originalSettings.getIcon()).getKey());
        }
        app.updateAppSettings(builder);
    }

    @Test
    public void getApp() {
        Long spaceAppId = TestSettings.get().getTestSpaceAppId();
        App spaceApp = App.fromExisting(client, spaceAppId);

        GetAppRequest req = new GetAppRequest();
        req.setId(spaceApp.id());
        GetAppResponseBody resp = client.app().getApp(req);
        assertThat(resp.getAppId()).isEqualTo(spaceApp.id());
        assertThat(resp.getSpaceId()).isNotNull();
        assertThat(resp.getThreadId()).isNotNull();
        assertThat(resp.getCreator().getCode()).isNotNull();
        assertThat(resp.getModifier().getCode()).isNotNull();
        assertThat(resp.getCreatedAt()).isNotNull();
        assertThat(resp.getModifiedAt()).isNotNull();
    }

    @Test
    public void getAppCustomize_getAppCustomizePreview() {
        String key1 = uploadMockFile(client, "desktop.js", "application/javascript", "// desktop.js");
        String key2 = uploadMockFile(client, "desktop.css", "text/css", "// desktop.css");
        String key3 = uploadMockFile(client, "mobile.js", "application/javascript", "// mobile.js");
        String key4 = uploadMockFile(client, "mobile.css", "text/css", "// mobile.css");

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
        app.updateAppCustomize(builder);
        client.app().deployApp(app.id());
        app.waitDeploy();
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
    }

    @Test
    public void getApps() {
        Long spaceAppId = TestSettings.get().getTestSpaceAppId();

        GetAppsRequest req = new GetAppsRequest();
        req.setIds(Arrays.asList(app.id(), spaceAppId));
        GetAppsResponseBody resp = client.app().getApps(req);
        List<com.kintone.client.model.app.App> apps = resp.getApps();
        assertThat(apps).hasSize(2);

        long appId = app.id();
        com.kintone.client.model.app.App testApp =
                apps.stream().filter(a -> a.getAppId() == appId).findFirst().get();
        assertThat(testApp.getAppId()).isEqualTo(appId);
        assertThat(testApp.getCreator().getCode()).isNotNull();
        assertThat(testApp.getModifier().getCode()).isNotNull();
        assertThat(testApp.getCreatedAt()).isNotNull();
        assertThat(testApp.getModifiedAt()).isNotNull();

        long spaceAppIdValue = spaceAppId;
        com.kintone.client.model.app.App spaceAppModel =
                apps.stream().filter(a -> a.getAppId() == spaceAppIdValue).findFirst().get();
        assertThat(spaceAppModel.getAppId()).isEqualTo(spaceAppId);
        assertThat(spaceAppModel.getSpaceId()).isNotNull();
        assertThat(spaceAppModel.getThreadId()).isNotNull();
        assertThat(spaceAppModel.getCreator().getCode()).isNotNull();
        assertThat(spaceAppModel.getModifier().getCode()).isNotNull();
        assertThat(spaceAppModel.getCreatedAt()).isNotNull();
        assertThat(spaceAppModel.getModifiedAt()).isNotNull();
    }

    @Test
    public void move() {
        Long spaceAppId = TestSettings.get().getTestSpaceAppId();
        Long spaceId = TestSettings.get().getSingleThreadSpaceId();
        App spaceApp = App.fromExisting(client, spaceAppId);
        Long originalSpaceId = client.app().getApp(spaceApp.id()).getSpaceId();

        try {
            client.app().move(spaceApp.id(), null);
            assertThat(client.app().getApp(spaceApp.id()).getSpaceId()).isNull();
            client.app().move(spaceApp.id(), spaceId);
            assertThat(client.app().getApp(spaceApp.id()).getSpaceId()).isEqualTo(spaceId);
        } finally {
            client.app().move(spaceApp.id(), originalSpaceId);
        }
    }

    @Test
    public void getAppSettings_getAppSettingsPreview() {
        AppSettingsBuilder builder =
                new AppSettingsBuilder().description("description").theme("GREEN").presetIcon("APP60");
        app.updateAppSettings(builder);
        client.app().deployApp(app.id());
        app.waitDeploy();

        GetAppSettingsRequest req1 = new GetAppSettingsRequest();
        req1.setApp(app.id());
        req1.setLang("default");
        GetAppSettingsResponseBody resp1 = client.app().getAppSettings(req1);
        assertThat(resp1.getDescription()).isEqualTo("description");
        assertThat(resp1.getTheme()).isEqualTo("GREEN");
        assertThat(resp1.getIcon().getType()).isEqualTo(AppIconType.PRESET);
        assertThat(((AppPresetIcon) resp1.getIcon()).getKey()).isEqualTo("APP60");

        builder =
                new AppSettingsBuilder().description("description 2").theme("BLUE").presetIcon("APP59");
        app.updateAppSettings(builder);

        GetAppSettingsPreviewRequest req2 = new GetAppSettingsPreviewRequest();
        req2.setApp(app.id());
        req2.setLang("default");
        GetAppSettingsPreviewResponseBody resp2 = client.app().getAppSettingsPreview(req2);
        assertThat(resp2.getDescription()).isEqualTo("description 2");
        assertThat(resp2.getTheme()).isEqualTo("BLUE");
        assertThat(resp2.getIcon().getType()).isEqualTo(AppIconType.PRESET);
        assertThat(((AppPresetIcon) resp2.getIcon()).getKey()).isEqualTo("APP59");
    }

    @Test
    public void updateAppCustomize() {
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
    }

    @Test
    public void updateAppSettings() {
        // アプリ名やアイコンを変更するテストは専用アプリを使用
        Long updateSettingsAppId = TestSettings.get().getTestAppIdForUpdateAppSettings();
        if (updateSettingsAppId == null) {
            System.out.println("KINTONE_TEST_APP_ID_FOR_UPDATE_APP_SETTINGS is not set, skipping test");
            return;
        }
        App settingsApp = App.fromExisting(client, updateSettingsAppId);

        // 前回のテスト実行で残った未デプロイの変更をリバート
        try {
            client.app().revertApp(settingsApp.id());
        } catch (Exception e) {
            // ignore if no changes to revert
        }

        try {
            long revision = settingsApp.getAppRevision(true);
            boolean supportNumberPrecision =
                    client.app().getAppSettingsPreview(settingsApp.id()).getNumberPrecision() != null;

            UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
            req.setApp(settingsApp.id());
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

            App.AppSettings previewSettings = settingsApp.getAppSettings(true);
            assertThat(previewSettings.getName()).isEqualTo("updateAppSettings_updated");
            assertThat(previewSettings.getDescription()).isEqualTo("app description");
            assertThat(previewSettings.getTheme()).isEqualTo("YELLOW");
            assertThat(((AppPresetIcon) previewSettings.getIcon()).getKey()).isEqualTo("APP60");

            if (supportNumberPrecision) {
                GetAppSettingsPreviewResponseBody response =
                        client.app().getAppSettingsPreview(settingsApp.id());
                assertThat(response.getNumberPrecision().getDigits()).isEqualTo(30);
                assertThat(response.getNumberPrecision().getDecimalPlaces()).isEqualTo(10);
                assertThat(response.getNumberPrecision().getRoundingMode()).isEqualTo(RoundingMode.UP);
                assertThat(response.getFirstMonthOfFiscalYear()).isEqualTo(6);
                assertThat(response.isEnableThumbnails()).isFalse();
                assertThat(response.isEnableComments()).isFalse();
                assertThat(response.isEnableDuplicateRecord()).isFalse();
                assertThat(response.isEnableBulkDeletion()).isTrue();
            }
        } finally {
            // 変更をリバート
            try {
                client.app().revertApp(settingsApp.id());
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Test
    public void updateAppSettingsFileIcon() throws IOException {
        // アプリアイコンを変更するテストは専用アプリを使用
        Long updateSettingsAppId = TestSettings.get().getTestAppIdForUpdateAppSettings();
        if (updateSettingsAppId == null) {
            System.out.println("KINTONE_TEST_APP_ID_FOR_UPDATE_APP_SETTINGS is not set, skipping test");
            return;
        }
        App settingsApp = App.fromExisting(client, updateSettingsAppId);

        // 前回のテスト実行で残った未デプロイの変更をリバート
        try {
            client.app().revertApp(settingsApp.id());
        } catch (Exception e) {
            // ignore if no changes to revert
        }

        try {
            Path path = new File(AppApiTest.class.getResource("fileicon.png").getFile()).toPath();
            String fileKey = client.file().uploadFile(path, "image/png");
            long revision = settingsApp.getAppRevision(true);

            UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
            req.setApp(settingsApp.id());
            req.setIcon(new AppFileIcon().setFile(new FileBody().setFileKey(fileKey)));

            UpdateAppSettingsResponseBody resp = client.app().updateAppSettings(req);
            assertThat(resp.getRevision()).isEqualTo(revision + 1);

            App.AppSettings settings = settingsApp.getAppSettings(true);
            assertThat(((AppFileIcon) settings.getIcon()).getFile().getName()).isEqualTo("fileicon.png");
            assertThat(((AppFileIcon) settings.getIcon()).getFile().getContentType())
                    .isEqualTo("image/png");
        } finally {
            // 変更をリバート
            try {
                client.app().revertApp(settingsApp.id());
            } catch (Exception e) {
                // ignore
            }
        }
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
