package com.kintone.client.model.app.layout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/** A layout information corresponding to a Group field. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class GroupLayout implements Layout {

    /** The field code of the Group field. */
    private String code;

    /** A list of field layouts for each row inside a Group field. */
    private List<Layout> layout;

    /** {@inheritDoc} */
    @Override
    public LayoutType getType() {
        return LayoutType.GROUP;
    }
}
