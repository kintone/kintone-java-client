package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateProcessManagementRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessManagementBuilder {
    private Boolean enabled;
    private final List<ProcessState> states = new ArrayList<>();
    private final List<ProcessAction> actions = new ArrayList<>();

    public ProcessManagementBuilder enable(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ProcessManagementBuilder state(String name) {
        ProcessState state = new ProcessState();
        state.setName(name);
        states.add(state);
        return this;
    }

    public StateBuilder state(String name, ProcessAssigneeType type) {
        return new StateBuilder(this, name, type);
    }

    public ProcessManagementBuilder action(String name, String from, String to) {
        ProcessAction action = new ProcessAction();
        action.setName(name);
        action.setFrom(from);
        action.setTo(to);
        actions.add(action);
        return this;
    }

    public ProcessManagementBuilder action(String name, String from, String to, String cond) {
        ProcessAction action = new ProcessAction();
        action.setName(name);
        action.setFrom(from);
        action.setTo(to);
        action.setFilterCond(cond);
        actions.add(action);
        return this;
    }

    UpdateProcessManagementRequest build(long appId) {
        Map<String, ProcessState> stateMap = new HashMap<>();
        for (int i = 0; i < states.size(); i++) {
            ProcessState state = states.get(i);
            state.setIndex(Integer.toString(i));
            stateMap.put(state.getName(), state);
        }

        UpdateProcessManagementRequest req = new UpdateProcessManagementRequest();
        req.setApp(appId);
        req.setEnable(enabled);
        if (!stateMap.isEmpty()) {
            req.setStates(stateMap);
        }
        if (!actions.isEmpty()) {
            req.setActions(actions);
        }
        return req;
    }

    public static class StateBuilder {
        private final ProcessManagementBuilder parent;
        private final String name;
        private final ProcessAssigneeType type;
        private final List<ProcessEntity> entities = new ArrayList<>();

        private StateBuilder(ProcessManagementBuilder parent, String name, ProcessAssigneeType type) {
            this.parent = parent;
            this.name = name;
            this.type = type;
        }

        public StateBuilder user(String code) {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.USER, code));
            entities.add(entity);
            return this;
        }

        public StateBuilder group(String code) {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.GROUP, code));
            entities.add(entity);
            return this;
        }

        public StateBuilder org(String code) {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.ORGANIZATION, code));
            entities.add(entity);
            return this;
        }

        public StateBuilder org(String code, boolean includeSubs) {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.ORGANIZATION, code));
            entity.setIncludeSubs(includeSubs);
            entities.add(entity);
            return this;
        }

        public StateBuilder field(String code) {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.FIELD_ENTITY, code));
            entities.add(entity);
            return this;
        }

        public StateBuilder creator() {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.CREATOR, null));
            entities.add(entity);
            return this;
        }

        public StateBuilder customField(String code) {
            ProcessEntity entity = new ProcessEntity();
            entity.setEntity(new Entity(EntityType.CUSTOM_FIELD, code));
            entities.add(entity);
            return this;
        }

        ProcessManagementBuilder build() {
            ProcessAssignee assignee = new ProcessAssignee();
            assignee.setType(type);
            assignee.setEntities(entities);

            ProcessState state = new ProcessState();
            state.setName(name);
            state.setAssignee(assignee);
            parent.states.add(state);
            return parent;
        }

        public ProcessManagementBuilder state(String name) {
            return build().state(name);
        }

        public StateBuilder state(String name, ProcessAssigneeType type) {
            return build().state(name, type);
        }

        public ProcessManagementBuilder action(String name, String from, String to) {
            return build().action(name, from, to);
        }

        public ProcessManagementBuilder action(String name, String from, String to, String cond) {
            return build().action(name, from, to, cond);
        }
    }

    public static ProcessManagementBuilder example() {
        return new ProcessManagementBuilder()
                .enable(true)
                .state("state A")
                .state("state B")
                .state("state C")
                .action("action 1", "state A", "state B")
                .action("action 2", "state B", "state C");
    }
}
