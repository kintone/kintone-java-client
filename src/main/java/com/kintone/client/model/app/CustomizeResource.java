package com.kintone.client.model.app;

/**
* JavaScript and CSS customization information. See the implementations of this interface: {@link
* CustomizeFileResource} and {@link CustomizeUrlResource}.
*/
public interface CustomizeResource {

    /**
    * Returns the resource type of this setting.
    *
    * @return the type of customization resource
    */
    CustomizeType getType();
}
