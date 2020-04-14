package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** Field permissions of a field. */
@Data
public class FieldRight {

    /** The field code of a field that has permission settings. */
    private String code;

    /** A list of the entities that the permissions are granted to, in order of priority. */
    private List<FieldRightEntity> entities;
}
