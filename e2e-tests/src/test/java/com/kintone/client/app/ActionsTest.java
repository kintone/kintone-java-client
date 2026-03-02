package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.*;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.model.Entity;
import com.kintone.client.model.app.*;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.RelatedApp;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** AppClientのactions.jsonのテスト */
public class ActionsTest extends ApiTestBase {

    private static final String TEXT_FIELD_CODE = "文字列__1行_";
    private static final String LINK_FIELD_CODE = "リンク__URL_";

    private KintoneClient client;
    private App app;
    private Map<String, AppAction> originalActions;

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
        originalActions = app.getActions(false);
    }

    @AfterEach
    public void cleanupActions() {
        if (app != null) {
            try {
                // 元のアクション設定に戻す
                client.app().updateAppActions(app.id(), originalActions);
                client.app().deployApp(app.id());
                app.waitDeploy();
            } catch (Exception e) {
                // ignore cleanup errors
            }
        }
    }

    @Test
    public void getAppActions_getAppActionsPreview() {
        FieldProperty text = app.field(TEXT_FIELD_CODE);
        AppAction action1 =
                createAction(
                        0L,
                        "action1",
                        app.id(),
                        Collections.singletonList(createFieldMapping(text, text)),
                        Collections.singletonList(Users.user1.toEntity()));
        app.updateActions(action1).deploy();
        long revision = app.getAppRevision(false);

        GetAppActionsRequest req1 = new GetAppActionsRequest();
        req1.setApp(app.id());
        GetAppActionsResponseBody resp1 = client.app().getAppActions(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        Map<String, AppAction> actions = resp1.getActions();
        assertThat(actions).containsOnlyKeys("action1");
        assertThat(actions.get("action1"))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(action1);

        AppAction action2 =
                createAction(
                        0L,
                        "action2",
                        app.id(),
                        Collections.singletonList(createFieldMapping(text, text)),
                        Collections.singletonList(Users.user1.toEntity()));
        app.updateActions(Collections.singletonMap("action1", action2));

        GetAppActionsPreviewRequest req2 = new GetAppActionsPreviewRequest();
        req2.setApp(app.id());
        GetAppActionsPreviewResponseBody resp2 = client.app().getAppActionsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        actions = resp2.getActions();
        assertThat(actions).containsOnlyKeys("action2");
        assertThat(actions.get("action2"))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(action2);
    }

    @Test
    public void updateAppActions() {
        FieldProperty text = app.field(TEXT_FIELD_CODE);
        FieldProperty link = app.field(LINK_FIELD_CODE);
        long revision = app.getAppRevision(true);

        AppAction action1 =
                createAction(
                        0L,
                        "action1",
                        app.id(),
                        Arrays.asList(createRecordURLMapping(link), createFieldMapping(text, text)),
                        Collections.singletonList(Users.user1.toEntity()));
        AppAction action2 =
                createAction(
                        1L,
                        "action2",
                        app.id(),
                        Collections.singletonList(createFieldMapping(text, text)),
                        Arrays.asList(Groups.everyone.toEntity(), Orgs.org1.toEntity()));
        Map<String, AppAction> actions = new HashMap<>();
        actions.put("action1", action1);
        actions.put("action2", action2);

        UpdateAppActionsRequest req = new UpdateAppActionsRequest();
        req.setApp(app.id());
        req.setActions(actions);
        req.setRevision(revision);
        UpdateAppActionsResponseBody resp = client.app().updateAppActions(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);
        assertThat(resp.getActions()).hasSize(2);
        assertThat(resp.getActions().get("action1").getId()).isGreaterThan(0);
        assertThat(resp.getActions().get("action2").getId()).isGreaterThan(0);
        updateActionIds(actions, resp.getActions());

        Map<String, AppAction> settings = app.getActions(true);
        assertThat(settings.get("action1"))
                .usingRecursiveComparison()
                .isEqualTo(actions.get("action1"));
        assertThat(settings.get("action2"))
                .usingRecursiveComparison()
                .isEqualTo(actions.get("action2"));
    }

    private void updateActionIds(Map<String, AppAction> actions, Map<String, ActionId> ids) {
        for (AppAction action : actions.values()) {
            action.setId(ids.get(action.getName()).getId());
        }
    }

    private AppAction createAction(
            long index,
            String name,
            long destAppId,
            List<AppActionMapping> mappings,
            List<Entity> entities) {
        AppAction action = new AppAction();
        action.setName(name);
        action.setIndex(index);
        action.setDestApp(new RelatedApp().setApp(destAppId).setCode(""));
        action.setMappings(mappings);
        action.setEntities(entities);
        action.setFilterCond("");
        return action;
    }

    private AppActionMapping createRecordURLMapping(FieldProperty field) {
        AppActionMapping mapping = new AppActionMapping();
        mapping.setSrcType(AppActionSourceType.RECORD_URL);
        mapping.setDestField(field.getCode());
        return mapping;
    }

    private AppActionMapping createFieldMapping(FieldProperty src, FieldProperty dest) {
        AppActionMapping mapping = new AppActionMapping();
        mapping.setSrcType(AppActionSourceType.FIELD);
        mapping.setSrcField(src.getCode());
        mapping.setDestField(dest.getCode());
        return mapping;
    }
}
