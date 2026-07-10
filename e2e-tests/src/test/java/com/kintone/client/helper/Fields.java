package com.kintone.client.helper;

import com.kintone.client.model.app.field.*;

public class Fields {
    public static SingleLineTextFieldProperty text() {
        return text("文字列__1行_", "文字列 (1行)");
    }

    public static SingleLineTextFieldProperty text(String code) {
        return text(code, code);
    }

    public static SingleLineTextFieldProperty text(String code, String label) {
        return new SingleLineTextFieldProperty().setCode(code).setLabel(label);
    }

    public static LinkFieldProperty link() {
        return link("リンク__URL_", "リンク (URL)");
    }

    public static LinkFieldProperty link(String code) {
        return link(code, code);
    }

    public static LinkFieldProperty link(String code, String label) {
        return link(code, label, LinkProtocol.WEB);
    }

    public static LinkFieldProperty link(String code, String label, LinkProtocol protocol) {
        return new LinkFieldProperty().setCode(code).setLabel(label).setProtocol(protocol);
    }

    public static FileFieldProperty file() {
        return file("添付ファイル");
    }

    public static FileFieldProperty file(String code) {
        return file(code, code);
    }

    public static FileFieldProperty file(String code, String label) {
        return new FileFieldProperty().setCode(code).setLabel(label);
    }

    public static NumberFieldProperty number() {
        return number("数値", "数値");
    }

    public static NumberFieldProperty number(String code) {
        return number(code, code);
    }

    public static NumberFieldProperty number(String code, String label) {
        return new NumberFieldProperty().setCode(code).setLabel(label);
    }

    public static UserSelectFieldProperty userSelect() {
        return userSelect("ユーザー選択", "ユーザー選択");
    }

    public static UserSelectFieldProperty userSelect(String code) {
        return userSelect(code, code);
    }

    public static UserSelectFieldProperty userSelect(String code, String label) {
        return new UserSelectFieldProperty().setCode(code).setLabel(label);
    }

    public static DateFieldProperty date() {
        return date("日付", "日付");
    }

    public static DateFieldProperty date(String code) {
        return date(code, code);
    }

    public static DateFieldProperty date(String code, String label) {
        return new DateFieldProperty().setCode(code).setLabel(label);
    }

    public static DateTimeFieldProperty datetime() {
        return datetime("日時", "日時");
    }

    public static DateTimeFieldProperty datetime(String code) {
        return datetime(code, code);
    }

    public static DateTimeFieldProperty datetime(String code, String label) {
        return new DateTimeFieldProperty().setCode(code).setLabel(label);
    }
}
