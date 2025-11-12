package com.kintone.client.api.space;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.space.SpaceStatistics;
import java.util.List;
import lombok.Value;

/** A response object for Get Spaces Statistics API. */
@Value
public class GetSpacesStatisticsResponseBody implements KintoneResponseBody {

    /** A list of space statistics. */
    private final List<SpaceStatistics> spaces;
}
