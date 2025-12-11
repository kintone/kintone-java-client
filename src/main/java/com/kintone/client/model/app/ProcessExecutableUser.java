package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** Executable user settings for Actions with SECONDARY type. */
@Data
public class ProcessExecutableUser {

    /** A list of the executable users. */
    private List<ProcessEntity> entities;
}
