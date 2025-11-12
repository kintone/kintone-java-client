package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Spaces Statistics API. */
@Data
public class GetSpacesStatisticsRequest implements KintoneRequest {

    /** The number of spaces to skip. Default is 0. */
    private Long offset;

    /** The number of spaces to retrieve. Must be between 1 and 100. Default is 100. */
    private Long limit;
}
