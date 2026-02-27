package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.app.Device;
import com.kintone.client.model.app.View;
import com.kintone.client.model.app.ViewId;
import com.kintone.client.model.app.ViewType;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** AppClientのviews.jsonのテスト */
public class ViewsTest extends ApiTestBase {
    @Test
    public void getViews_getViewsPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty number = Fields.number();
        App app = App.create(client, "getViews_getViewsPreview").addFields(text, number);
        View view = listView("v1", 0, "", number.getCode() + " asc", text.getCode(), number.getCode());
        app.updateViews(view).deploy();
        long revision = app.getAppRevision(false);

        GetViewsRequest req1 = new GetViewsRequest();
        req1.setApp(app.id());
        GetViewsResponseBody resp1 = client.app().getViews(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        Map<String, View> views = resp1.getViews();
        assertThat(views).containsOnlyKeys("v1");
        assertThat(views.get("v1"))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(view);

        View view2 = new View().setType(ViewType.LIST).setName("v2").setIndex(0L);
        app.updateViews(Collections.singletonMap("v1", view2));

        GetViewsPreviewRequest req2 = new GetViewsPreviewRequest();
        req2.setApp(app.id());
        GetViewsPreviewResponseBody resp2 = client.app().getViewsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        views = resp2.getViews();
        view.setName("v2");
        assertThat(views).containsOnlyKeys("v2");
        assertThat(views.get("v2"))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(view);
    }

    @Test
    public void updateViews() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty date = Fields.date();
        App app = App.create(client, "updateViews").addFields(text, date);
        long revision = app.getAppRevision(true);

        Map<String, View> views = new HashMap<>();
        String sort = date.getCode() + " desc";
        views.put("v1", listView("v1", 0, "", sort, text.getCode()));
        views.put("v2", calendarView("v2", 1, text.getCode(), date.getCode(), "", sort));
        views.put("v3", customizeView("v3", 2, "test", "", sort));

        UpdateViewsRequest req = new UpdateViewsRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setViews(views);
        UpdateViewsResponseBody resp = client.app().updateViews(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);
        assertThat(resp.getViews()).hasSize(3);
        assertThat(resp.getViews().get("v1").getId()).isGreaterThan(0);
        assertThat(resp.getViews().get("v2").getId()).isGreaterThan(0);
        assertThat(resp.getViews().get("v3").getId()).isGreaterThan(0);
        updateViewIds(views, resp.getViews());

        Map<String, View> settings = app.getViews(true);
        assertThat(settings.get("v1")).usingRecursiveComparison().isEqualTo(views.get("v1"));
        assertThat(settings.get("v2")).usingRecursiveComparison().isEqualTo(views.get("v2"));
        assertThat(settings.get("v3")).usingRecursiveComparison().isEqualTo(views.get("v3"));
    }

    private void updateViewIds(Map<String, View> views, Map<String, ViewId> ids) {
        for (View view : views.values()) {
            view.setId(ids.get(view.getName()).getId());
        }
    }

    private View listView(String name, long index, String query, String sort, String... fields) {
        View v = new View();
        v.setName(name);
        v.setType(ViewType.LIST);
        v.setIndex(index);
        v.setFields(Arrays.asList(fields));
        v.setFilterCond(query);
        v.setSort(sort);
        return v;
    }

    private View calendarView(
            String name, long index, String titleField, String dateField, String query, String sort) {
        View v = new View();
        v.setName(name);
        v.setType(ViewType.CALENDAR);
        v.setIndex(index);
        v.setTitle(titleField);
        v.setDate(dateField);
        v.setFilterCond(query);
        v.setSort(sort);
        return v;
    }

    private View customizeView(String name, long index, String html, String query, String sort) {
        View v = new View();
        v.setName(name);
        v.setType(ViewType.CUSTOM);
        v.setIndex(index);
        v.setHtml(html);
        v.setFilterCond(query);
        v.setSort(sort);
        v.setDevice(Device.ANY);
        v.setPager(true);
        return v;
    }
}
