package com.kintone.client.model.space;

import lombok.Value;

/** An object contains permission settings of the Space. */
@Value
public class SpacePermissions {

    /**
     * The permission setting whether to allow only space administrators or users to create apps in
     * the space.
     */
    CreateAppSubject createApp;
}
