package com.kintone.client.model.record;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** An object for specifying the record to be updated. */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UpdateKey {

    /** The field code of the unique key (required). */
    @Setter private String field;

    /** The value of the unique key (required). */
    private Object value;

    public UpdateKey(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public UpdateKey(String field, Number value) {
        this.field = field;
        this.value = value;
    }

    public UpdateKey setValue(String value) {
        this.value = value;
        return this;
    }

    public UpdateKey setValue(Number value) {
        this.value = value;
        return this;
    }
}
