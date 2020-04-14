package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppRightEntity;
import java.util.List;
import lombok.Value;

/** A response object for Get App Acl API. */
@Value
public class GetAppAclResponseBody implements KintoneResponseBody {

    /** The list of objects that contain data of App permissions, in order of priority. */
    private final List<AppRightEntity> rights;

    /** The revision number of the App settings. */
    private final long revision;
}
