package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.space.GuestUser;
import java.util.List;
import lombok.Data;

/** A request object for Add Guests API. */
@Data
public class AddGuestsRequest implements KintoneRequest {

    /** A list of Guest user data (required). */
    private List<GuestUser> guests;
}
