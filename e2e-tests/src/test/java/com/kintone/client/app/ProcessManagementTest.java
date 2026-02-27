package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.helper.ProcessManagementBuilder;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.*;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.*;
import org.junit.jupiter.api.Test;

/** AppClientのstatus.jsonのテスト */
public class ProcessManagementTest extends ApiTestBase {
    @Test
    public void getProcessManagement_getProcessManagementPreview() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "getProcessManagement_getProcessManagementPreview");
        app.applyExampleProcessManagement().deploy();
        long revision = app.getAppRevision(false);

        ProcessAssignee assignee = assignee(ProcessAssigneeType.ONE, Collections.emptyList());
        Map<String, ProcessState> states = new HashMap<>();
        states.put(
                "state A", new ProcessState().setName("state A").setIndex("0").setAssignee(assignee));
        states.put(
                "state B", new ProcessState().setName("state B").setIndex("1").setAssignee(assignee));
        states.put(
                "state C", new ProcessState().setName("state C").setIndex("2").setAssignee(assignee));

        List<ProcessAction> actions = new ArrayList<>();
        actions.add(
                new ProcessAction()
                        .setFrom("state A")
                        .setTo("state B")
                        .setName("action 1")
                        .setFilterCond("")
                        .setType(ProcessActionType.PRIMARY));
        actions.add(
                new ProcessAction()
                        .setFrom("state B")
                        .setTo("state C")
                        .setName("action 2")
                        .setFilterCond("")
                        .setType(ProcessActionType.PRIMARY));

        GetProcessManagementRequest req1 = new GetProcessManagementRequest();
        req1.setApp(app.id());
        GetProcessManagementResponseBody resp1 = client.app().getProcessManagement(req1);
        assertThat(resp1.isEnable()).isTrue();
        assertThat(resp1.getStates()).usingRecursiveComparison().isEqualTo(states);
        assertThat(resp1.getActions()).usingRecursiveComparison().isEqualTo(actions);
        assertThat(resp1.getRevision()).isEqualTo(revision);

        app.updateProcessManagement(new ProcessManagementBuilder().enable(false));

        GetProcessManagementPreviewRequest req2 = new GetProcessManagementPreviewRequest();
        req2.setApp(app.id());
        GetProcessManagementPreviewResponseBody resp2 = client.app().getProcessManagementPreview(req2);
        assertThat(resp2.isEnable()).isFalse();
        assertThat(resp2.getStates()).usingRecursiveComparison().isEqualTo(states);
        assertThat(resp2.getActions()).usingRecursiveComparison().isEqualTo(actions);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
    }

    @Test
    public void updateProcessManagement() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        App app = App.create(client, "updateProcessManagement").addFields(number);
        long revision = app.getAppRevision(true);

        List<ProcessEntity> entities = new ArrayList<>();
        entities.add(entity(EntityType.USER, getDefaultUser(), false));
        entities.add(entity(EntityType.GROUP, "everyone", false));
        ProcessAssignee assignee1 = assignee(ProcessAssigneeType.ONE, entities);
        ProcessAssignee assignee2 = assignee(ProcessAssigneeType.ALL, entities);
        ProcessAssignee assignee3 = assignee(ProcessAssigneeType.ANY, entities);
        Map<String, ProcessState> states = new HashMap<>();
        states.put("S0", new ProcessState().setName("S0").setIndex("0"));
        states.put("S1", new ProcessState().setName("S1").setIndex("1").setAssignee(assignee1));
        states.put("S2", new ProcessState().setName("S2").setIndex("2").setAssignee(assignee2));
        states.put("S3", new ProcessState().setName("S3").setIndex("3").setAssignee(assignee3));

        List<ProcessAction> actions = new ArrayList<>();
        String query = number.getCode() + " >= 10";
        actions.add(
                new ProcessAction()
                        .setFrom("S0")
                        .setTo("S1")
                        .setName("0_1")
                        .setFilterCond("")
                        .setType(ProcessActionType.PRIMARY));
        actions.add(
                new ProcessAction()
                        .setFrom("S1")
                        .setTo("S2")
                        .setName("1_2")
                        .setFilterCond(query)
                        .setType(ProcessActionType.PRIMARY));
        actions.add(
                new ProcessAction()
                        .setFrom("S2")
                        .setTo("S3")
                        .setName("2_3")
                        .setFilterCond("")
                        .setType(ProcessActionType.PRIMARY));

        UpdateProcessManagementRequest req = new UpdateProcessManagementRequest();
        req.setApp(app.id());
        req.setEnable(true);
        req.setStates(states);
        req.setActions(actions);
        req.setRevision(revision);
        UpdateProcessManagementResponseBody resp = client.app().updateProcessManagement(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        App.ProcessManagement settings = app.getProcessManagement(true);
        // assignee設定が空の場合の初期値を入れる
        ProcessAssignee assignee0 = assignee(ProcessAssigneeType.ONE, Collections.emptyList());
        states.get("S0").setAssignee(assignee0);
        assertThat(settings.isEnable()).isTrue();
        assertThat(settings.getStates()).usingRecursiveComparison().isEqualTo(states);
        assertThat(settings.getActions()).usingRecursiveComparison().isEqualTo(actions);
        assertThat(settings.getRevision()).isEqualTo(revision + 1);
    }

    private ProcessEntity entity(EntityType type, String code, boolean includeSubs) {
        ProcessEntity e = new ProcessEntity();
        e.setEntity(new Entity(type, code)).setIncludeSubs(includeSubs);
        return e;
    }

    private ProcessAssignee assignee(ProcessAssigneeType type, List<ProcessEntity> entities) {
        return new ProcessAssignee().setType(type).setEntities(entities);
    }
}
