package com.kintone.client.model.app;

/**
* A {@link AppIcon} represents the icon of an App. The actual data depends on the icon type. See
* implementations of this interface: {@link AppPresetIcon}, {@link AppFileIcon}.
*/
public interface AppIcon {

    /**
    * Returns the type of App icon.
    *
    * @return the type of App icon
    */
    AppIconType getType();
}
