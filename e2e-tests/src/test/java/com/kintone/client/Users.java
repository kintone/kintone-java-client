package com.kintone.client;

public class Users {
    public static UserSetting cybozu() {
        return TestSettings.get().getDefaultUser();
    }

    public static UserSetting user1() {
        return TestSettings.get().getTestUser();
    }

    // 後方互換性のためのフィールド
    public static final UserSetting cybozu = cybozu();
    public static final UserSetting user1 = user1();
}
