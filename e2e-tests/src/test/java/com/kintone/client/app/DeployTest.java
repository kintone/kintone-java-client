package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.helper.Space;
import com.kintone.client.model.app.AppDeployStatus;
import com.kintone.client.model.app.DeployApp;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/** AppClientのアプリ作成、デプロイに関するテスト */
public class DeployTest extends ApiTestBase {

    private static final int DEPLOY_WAIT_SEC = 300;

    @Test
    @Disabled("Since the app cannot be deleted, it has been temporarily disabled.")
    public void addApp() {
        Space space = Space.guest(this);
        long spaceId = space.id();
        long threadId = space.getDefaultThread();
        KintoneClient client = setupDefaultClient(spaceId);

        AddAppRequest req = new AddAppRequest();
        req.setName("addApp_" + System.currentTimeMillis());
        req.setSpace(spaceId);
        req.setThread(threadId);
        AddAppResponseBody resp = client.app().addApp(req);
        assertThat(resp.getApp()).isGreaterThan(0);
        assertThat(resp.getRevision()).isGreaterThan(0);

        App app = App.fromExisting(client, resp.getApp());
        assertThat(app.getAppSettings(true).getName()).startsWith("addApp_");
    }

    @Test
    @Disabled("Since the app cannot be deleted, it has been temporarily disabled.")
    public void deployApp_getDeployStatus() {
        KintoneClient client = setupDefaultClient();
        DeployApp app1 = createApp(client, "deployApp 1");
        DeployApp app2 = createApp(client, "deployApp 2");

        DeployAppRequest req1 = new DeployAppRequest();
        req1.setApps(Arrays.asList(app1, app2));
        req1.setRevert(false);
        DeployAppResponseBody resp = client.app().deployApp(req1);

        GetDeployStatusRequest req2 = new GetDeployStatusRequest();
        req2.setApps(Arrays.asList(app1.getApp(), app2.getApp()));
        for (int i = 0; i < DEPLOY_WAIT_SEC; i++) {
            GetDeployStatusResponseBody resp2 = client.app().getDeployStatus(req2);
            assertThat(resp2.getApps()).hasSize(2);

            AppDeployStatus status1 = resp2.getApps().get(0);
            assertThat(status1.getApp()).isEqualTo(app1.getApp());
            assertThat(status1.getStatus()).isNotNull();

            AppDeployStatus status2 = resp2.getApps().get(1);
            assertThat(status2.getApp()).isEqualTo(app2.getApp());
            assertThat(status2.getStatus()).isNotNull();

            if (status1.getStatus() == DeployStatus.SUCCESS
                    && status2.getStatus() == DeployStatus.SUCCESS) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        fail("deploy faield");
    }

    @Test
    @Disabled("Since the app cannot be deleted, it has been temporarily disabled.")
    public void deployApp_revert() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "deployApp_revert").deploy();
        FieldProperty text = Fields.text();
        app.addFields(text);

        DeployAppRequest req = new DeployAppRequest();
        req.setApps(Collections.singletonList(new DeployApp().setApp(app.id())));
        req.setRevert(true);
        DeployAppResponseBody resp = client.app().deployApp(req);

        app.waitDeploy();
        Map<String, FieldProperty> fields = app.getFields(false);
        assertThat(fields).doesNotContainKeys(text.getCode());
    }

    private DeployApp createApp(KintoneClient client, String name) {
        AddAppRequest req = new AddAppRequest();
        req.setName(name);
        AddAppResponseBody resp = client.app().addApp(req);
        return new DeployApp().setApp(resp.getApp()).setRevision(resp.getRevision());
    }
}
