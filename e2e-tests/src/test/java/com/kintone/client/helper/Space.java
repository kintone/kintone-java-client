package com.kintone.client.helper;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;

public class Space {
    private final KintoneClient client;
    private final long spaceId;
    private final boolean isGuestSpace;

    private Long defaultThreadId;

    private Space(ApiTestBase base, long spaceId, boolean isGuest) {
        if (isGuest) {
            client = base.setupDefaultClient(spaceId);
        } else {
            client = base.setupDefaultClient();
        }
        this.spaceId = spaceId;
        isGuestSpace = isGuest;
    }

    public static Space singleThread(ApiTestBase base) {
        TestSettings settings = TestSettings.get();
        Long spaceId = settings.getSingleThreadSpaceId();
        if (spaceId == null) {
            throw new IllegalStateException("KINTONE_SPACE_ID is not set");
        }
        return new Space(base, spaceId, false);
    }

    public static Space multiThread(ApiTestBase base) {
        TestSettings settings = TestSettings.get();
        Long spaceId = settings.getMultiThreadSpaceId();
        if (spaceId == null) {
            throw new IllegalStateException("KINTONE_MULTI_THREAD_SPACE_ID is not set");
        }
        Space space = new Space(base, spaceId, false);
        space.defaultThreadId = settings.getMultiThreadDefaultThreadId();
        return space;
    }

    public static Space guest(ApiTestBase base) {
        TestSettings settings = TestSettings.get();
        Long spaceId = settings.getGuestSpaceId();
        if (spaceId == null) {
            throw new IllegalStateException("KINTONE_GUEST_SPACE_ID is not set");
        }
        return new Space(base, spaceId, true);
    }

    public static Space fromExisting(ApiTestBase base, long spaceId, boolean isGuest) {
        return new Space(base, spaceId, isGuest);
    }

    public long id() {
        return spaceId;
    }

    public String getName() {
        return client.space().getSpace(spaceId).getName();
    }

    public String getBody() {
        return client.space().getSpace(spaceId).getBody();
    }

    public Long getDefaultThread() {
        if (defaultThreadId == null) {
            defaultThreadId = client.space().getSpace(spaceId).getDefaultThread();
        }
        return defaultThreadId;
    }

    public boolean isGuestSpace() {
        return isGuestSpace;
    }
}
