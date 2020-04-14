package com.kintone.client.model.record;

import com.kintone.client.model.FileBody;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for an Attachment field. */
@Value
public class FileFieldValue implements FieldValue {

    /** The list of objects that contain information of attached files. */
    private final List<FileBody> values;

    public FileFieldValue(List<FileBody> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public FileFieldValue(FileBody... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.FILE;
    }
}
