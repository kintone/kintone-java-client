package com.kintone.client.helper;

import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.layout.*;
import com.kintone.client.model.record.FieldType;
import java.util.ArrayList;
import java.util.List;

public class FormLayoutBuilder {

    private final List<Layout> layout = new ArrayList<>();
    private List<FieldLayout> fields;

    public FormLayoutBuilder row() {
        if (fields != null) {
            layout.add(new RowLayout().setFields(fields));
            fields = null;
        }
        fields = new ArrayList<>();
        return this;
    }

    public List<Layout> build() {
        if (fields != null) {
            layout.add(new RowLayout().setFields(fields));
            fields = null;
        }
        return layout;
    }

    private FormLayoutBuilder addFieldLayout(FieldLayout field) {
        if (fields == null) {
            row();
        }
        fields.add(field);
        return this;
    }

    private FormLayoutBuilder addField(FieldProperty field, Integer w, Integer h) {
        FieldLayout settings = new FieldLayout();
        settings.setType(field.getType());
        settings.setCode(field.getCode());
        if (w != null || h != null) {
            settings.setSize(new FieldSize().setWidth(w).setInnerHeight(h));
        }
        return addFieldLayout(settings);
    }

    public FormLayoutBuilder field(FieldProperty field) {
        return addField(field, null, null);
    }

    public FormLayoutBuilder field(FieldProperty field, int width) {
        return addField(field, width, null);
    }

    public FormLayoutBuilder field(FieldProperty field, int width, int innerHeight) {
        return addField(field, width, innerHeight);
    }

    public FormLayoutBuilder spacer(String elementId, int width, int height) {
        FieldLayout settings = new FieldLayout();
        settings.setType(FieldType.SPACER);
        settings.setElementId(elementId);
        settings.setSize(new FieldSize().setWidth(width).setHeight(height));
        return addFieldLayout(settings);
    }

    public FormLayoutBuilder hr(int width) {
        FieldLayout settings = new FieldLayout();
        settings.setType(FieldType.HR);
        settings.setElementId("");
        settings.setSize(new FieldSize().setWidth(width));
        return addFieldLayout(settings);
    }

    public FormLayoutBuilder label(String label, int width) {
        FieldLayout settings = new FieldLayout();
        settings.setType(FieldType.LABEL);
        settings.setLabel(label);
        settings.setElementId("");
        settings.setSize(new FieldSize().setWidth(width));
        return addFieldLayout(settings);
    }
}
