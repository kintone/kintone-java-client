package com.kintone.client.model.app.layout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/** A layout information corresponding to a Table. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class SubtableLayout implements Layout {

    /** The field code of the Table field. */
    private String code;

    /** A list of fields in the row. */
    private List<FieldLayout> fields;

    /** {@inheritDoc} */
    @Override
    public LayoutType getType() {
        return LayoutType.SUBTABLE;
    }
}
