package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** A Record permission of a record. */
@Data
public class RecordRight {

    /** The filter condition in a query format. */
    private String filterCond;

    /** A list of the entities that the permissions are granted to. */
    private List<RecordRightEntity> entities;
}
