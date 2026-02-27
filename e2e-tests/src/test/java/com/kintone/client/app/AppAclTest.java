package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.AppAclBuilder;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.AppRightEntity;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** AppClientのアプリアクセス権設定に関するテスト */
public class AppAclTest extends ApiTestBase {
    @Test
    public void getAppAcl_getAppAclPreview() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "getAppAcl_getAppAclPreview");

        AppAclBuilder builder = new AppAclBuilder();
        builder.everyone().all(false);
        builder.creator().all(false).appEditable(true);
        builder.user(getDefaultUser()).all(true);
        app.updateAppAcl(builder).deploy();
        long revision = app.getAppRevision(true);

        Entity creator = new Entity(EntityType.CREATOR, null);
        Entity user = new Entity(EntityType.USER, getDefaultUser());
        Entity everyone = new Entity(EntityType.GROUP, "everyone");

        AppRightEntity r1 = right(creator, false, false, false, false, true, false, false);
        AppRightEntity r2 = right(user, true, true, true, true, true, true, true);
        AppRightEntity r3 = right(everyone, false, false, false, false, false, false, false);
        AppRightEntity r4 = right(creator, true, true, true, true, true, false, false);

        GetAppAclRequest req1 = new GetAppAclRequest();
        req1.setApp(app.id());
        GetAppAclResponseBody resp1 = client.app().getAppAcl(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getRights()).containsExactly(r1, r2, r3);

        builder = new AppAclBuilder();
        builder.creator().applyDefault().everyone().all(false);
        app.updateAppAcl(builder);

        GetAppAclPreviewRequest req2 = new GetAppAclPreviewRequest();
        req2.setApp(app.id());
        GetAppAclPreviewResponseBody resp2 = client.app().getAppAclPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getRights()).containsExactly(r4, r3);
    }

    @Test
    public void updateAppAcl() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateAppAcl").deploy();
        long revision = app.getAppRevision(true);

        Entity everyone = new Entity(EntityType.GROUP, "everyone");
        Entity creator = new Entity(EntityType.CREATOR, null);
        Entity user = new Entity(EntityType.USER, getDefaultUser());

        AppRightEntity r1 = right(everyone, false, false, false, false, false, false, false);
        AppRightEntity r2 = right(creator, true, true, true, true, true, true, true);
        AppRightEntity r3 = right(user, true, false, false, false, true, false, false);

        UpdateAppAclRequest req = new UpdateAppAclRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setRights(Arrays.asList(r1, r2, r3));
        UpdateAppAclResponseBody resp = client.app().updateAppAcl(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        List<AppRightEntity> previewRights = app.getAppAcl(true);
        assertThat(previewRights).containsExactly(r2, r3, r1); // everyoneの設定は最後になる

        List<AppRightEntity> deployedRights = app.getAppAcl(false);
        AppRightEntity default1 = right(everyone, true, true, true, true, false, false, false);
        AppRightEntity default2 = right(creator, true, true, true, true, true, true, true);
        assertThat(deployedRights).containsExactly(default2, default1);
    }

    private AppRightEntity right(
            Entity entity,
            boolean recordViewable,
            boolean recordAddable,
            boolean recordEditable,
            boolean recordDeletable,
            boolean appEditable,
            boolean recordImportable,
            boolean recordExportable) {
        AppRightEntity r = new AppRightEntity().setEntity(entity);
        r.setIncludeSubs(false);
        r.setRecordViewable(recordViewable);
        r.setRecordAddable(recordAddable);
        r.setRecordEditable(recordEditable);
        r.setRecordDeletable(recordDeletable);
        r.setAppEditable(appEditable);
        r.setRecordImportable(recordImportable);
        r.setRecordExportable(recordExportable);
        return r;
    }
}
