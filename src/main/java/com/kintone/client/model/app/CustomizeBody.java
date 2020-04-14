package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** An object containing data of JavaScript and CSS customizations. */
@Data
public class CustomizeBody {

    /** A list of JavaScript files or URLs. */
    private List<CustomizeResource> js;

    /** A list of CSS files or URLs. */
    private List<CustomizeResource> css;
}
